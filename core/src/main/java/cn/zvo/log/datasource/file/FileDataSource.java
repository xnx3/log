package cn.zvo.log.datasource.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.xnx3.BaseVO;
import com.xnx3.DateUtil;
import com.xnx3.FileUtil;
import com.xnx3.StringUtil;

import cn.zvo.log.DatasourceInterface;
import cn.zvo.log.vo.LogListVO;
import cn.zvo.page.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 写出到文件输出
 * @author 管雷鸣
 */
public class FileDataSource implements DatasourceInterface{
	public String path;	//保存日志的路径，格式如 /mnt/tomcat/webapp_log/ 如果不设置此，则不开启，日志不会写出到日志文件，没任何动作。
//	public String name; //保存日志的文件名，传入如 useraction ，则会自动保存出 useraction_20221206.log  。如果name不设置，默认是 log ，按小时生成的日志文件名字为 log_2022-12-13_14.log
	
	/**
	 * 初始化文件方式存储
	 * @param config 如果传入 null，则是不启用
	 */
	public FileDataSource(Map<String, String> config) {
		if(config == null) {
			return;
		}
		
		this.path = config.get("path");
		if(this.path != null && this.path.trim().equals("")) {
			this.path = null;
		}
		
//		this.name = config.get("name");
//		//如果name不设置，默认是 log ，按小时生成的日志文件名字为 log_2022-12-13_14.log
//		if(this.path != null) {
//			if(this.name == null || this.name.trim().length() == 0) {
//				this.name = "log";
//			}
//		}
	}
	
	@Override
	public boolean commit(String table, List<Map<String, Object>> list) {
		if(this.path == null || this.path.equals("")) {
			//未开启日志
			return true;
		}
		
		StringBuffer sb = new StringBuffer();
		
//		System.out.println("submit -- "+list.size());
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			sb.append(JSONObject.fromObject(map).toString()+"\n");
		}
		if(sb.length() > 0) {
			FileUtil.appendText(new File(path+table+"_"+DateUtil.currentDate("yyyy-MM-dd")+".log"), sb.toString());
		}
		
		return true;
	}

	/**
	 * 同上面的 list,只不过这里可以自定义操作 indexName。
	 * <p>获取到的数据排序规则：这里是按照数据加入的顺序，倒序排列，插入越往后的，显示越靠前</p>
	 * @param indexName 索引名字
	 * @param query 不允许传入
	 * @param everyPageNumber 每页显示几条，最大200
	 * @param request
	 * @return 如果失败， vo.getResult() == ActionLogListVO.FAILURE
	 */
	public LogListVO list(String table, String query,int everyPageNumber, int currentPage) {
		LogListVO vo = new LogListVO();
		
		//文件方式存储的，禁止使用查询命令
		if(query != null && query.trim().length() > 0) {
			vo.setBaseVO(BaseVO.FAILURE, "log采用默认的文件存储方式，无法根据查询条件筛选");
			return vo;
		}
		
		//取出当前日期的日志，按天存，一天一个日志文件
		//日志文件
		String logPath = this.path+table+"_"+DateUtil.currentDate("yyyy-MM-dd")+".log";
		File file = new File(logPath);
		if(!file.exists()) {
			//当前天内日志文件不存在
			vo.setBaseVO(BaseVO.FAILURE, "log采用默认的文件存储方式，当天的日志文件尚不存在");
			return vo;
		}
		
		//文件存在，直接读取文件
        Scanner sc;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			vo.setBaseVO(BaseVO.FAILURE, "log采用默认的文件存储方式，但当天的日志文件尚不存在");
			return vo;
		}
        
        int i = 0;
        List<String> list = new LinkedList<String>();
        while (i++ < 3001 && sc.hasNext()) {
            String line = sc.nextLine();
//            System.out.println(line);
            list.add(line);
        }
		
        //设置分页信息
        vo.setPage(new Page(list.size(), everyPageNumber, currentPage));
        
        //倒叙，根据写入时间排序，写入时间越晚，显示越靠前
        Collections.reverse(list);
        
        //取当前页所显示的数据信息
        List<String> resultList = new LinkedList<String>();
        for (int j = 0; j < vo.getPage().getEveryNumber(); j++) {
			int index = vo.getPage().getLimitStart() + j;
			if(index < vo.getPage().getAllRecordNumber()) {
				//如果没超过总条数，那么合适，加入
				resultList.add(list.get(index));
			}
		}
        
        //System.out.println(JSONArray.fromObject(list).toString());
		vo.setJsonArray(JSONArray.fromObject(resultList));
		vo.setPage(new Page(list.size(), everyPageNumber, currentPage));
		vo.setInfo("最大显示3000条");
		return vo;
	}
	

	/**
	 * 目录检测，检测是否存在。若不存在，则自动创建目录。适用于使用本地磁盘进行存储，在本身tomcat中创建目录.有一下两种情况:
	 * <ul>
	 * 		<li>在线上的tomcat项目中，创建的目录是在 tomcat/webapps/ROOT/ 目录下</li>
	 * 		<li>在开发环境Eclipse中，创建的目录是在 target/classes/ 目录下</li>
	 * </ul>
	 * @param path 要检测的目录，相对路径，如 jar/file/  创建到file文件，末尾一定加/     或者jar/file/a.jar创建到file文件夹
	 */
	public static void directoryInit(String path){
		if(path == null){
			return;
		}
		
		//windows取的路径是\，所以要将\替换为/
		if(path.indexOf("\\") > 1){
			path = StringUtil.replaceAll(path, "\\\\", "/");
		}
		
		if(path.length() - path.lastIndexOf("/") > 1){
			//path最后是带了具体文件名的，把具体文件名过滤掉，只留文件/结尾
			path = path.substring(0, path.lastIndexOf("/")+1);
		}
		
		//如果目录或文件不存在，再进行创建目录的判断
		if(!new java.io.File(path).exists()){
			String[] ps = path.split("/");
			
			String xiangdui = "";
			//length-1，/最后面应该就是文件名了，所以要忽略最后一个
			for (int i = 0; i < ps.length; i++) {
				if(ps[i].length() > 0){
					xiangdui = xiangdui + ps[i]+"/";
					File file = new File(xiangdui);
					if(!file.exists()){
						file.mkdir();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		directoryInit("C:\\Users\\Administrator\\Desktop\\log1\\");
	}

}