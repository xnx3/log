package cn.zvo.log.datasource.aliyunSLS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.aliyun.openservices.log.common.LogContent;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import cn.zvo.log.LogInterface;
import cn.zvo.log.vo.LogListVO;
import cn.zvo.page.Page;
import net.sf.json.JSONArray;

/**
 * 日志记录，使用阿里云日志服务
 * @author 管雷鸣
 *
 */
public class AliyunSLSDataSource implements LogInterface{
	public static final int CACHE_MAX_TIME_DEFAULT = 120;	//默认缓存最大时间120秒，达到120秒时自动提交缓存
	public static final int CACHE_MAX_NUMBER_DEFAULT = 100;	//默认缓存存储得最大条数，默认100条，缓存中超过100条时会自动提交缓存
	
	public static AliyunLogUtil aliyunLogUtil = null;
	AliyunLogPageUtil logPage;

	public AliyunSLSDataSource(Map<String, String> config) {
//		this.indexName = config.get("indexName");
		
		String accessKeyId = config.get("accessKeyId");
		String accessKeySecret = config.get("accessKeySecret");
		String endpoint = config.get("endpoint");
		String project = config.get("project");
//		String logstore = config.get("logstore");
//		init(hostname, port, scheme, username, password);
	}
	
	public void init(String accessKeyId, String accessKeySecret, String endpoint, String project) {
		aliyunLogUtil = new AliyunLogUtil(endpoint, accessKeyId, accessKeySecret, project);
		//开启触发日志的，其来源类及函数的记录
		aliyunLogUtil.setStackTraceDeep(5);
//		aliyunLogUtil.setCacheAutoSubmit(cacheMaxNumber, cacheMaxTime);
	
		logPage = new AliyunLogPageUtil(aliyunLogUtil);
	}
	
//	
//	@Override
//	public void add(Map<String, Object> map) {
//		if(aliyunLogUtil == null){
//			//不使用日志服务，终止即可
//			return;
//		}
//		
//		//将map转化为 logitem
//		LogItem logItem = aliyunLogUtil.newLogItem();
//		for(Map.Entry<String, Object> entry : map.entrySet()){
//			String value = null;
//			if(entry.getValue() == null){
//				value = "";
//			}else{
//				value = entry.getValue().toString()+"";
//			}
//			logItem.PushBack(entry.getKey(), value);
//		}
//		
//		try {
//			aliyunLogUtil.cacheLog(logItem);
//		} catch (LogException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 将 {@link LogItem} 转化为 {@link Map}
	 */
	public static Map<String, Object> logitemToMap(LogItem logItem){
		if(logItem == null){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<LogContent> array = logItem.GetLogContents();
		for (int i = 0; i < array.size(); i++) {
			LogContent log = array.get(i);
			map.put(log.GetKey(), log.GetValue());
		}
		return map;
	}


	@Override
	public LogListVO list(String table, String query, int everyPageNumber, int currentPage) {
		//得到当前页面的列表数据
		JSONArray jsonArray = null;
		try {
			jsonArray = logPage.list(table, query, "", true, everyPageNumber, currentPage);
		} catch (LogException e) {
			e.printStackTrace();
		}
		if(jsonArray == null){
			jsonArray = new JSONArray();
		}
		//得到当前页面的分页相关数据（必须在执行了list方法获取列表数据之后，才能调用此处获取到分页）
		Page page = logPage.getPage();
		
		LogListVO vo = new LogListVO();
		vo.setJsonArray(jsonArray);
		vo.setPage(page);
		
		return vo;
	}

//	@Override
//	public void setTable(String name) {
//		this.table = name;
//	}

	@Override
	public boolean commit(String table, List<Map<String, Object>> list) {
		if(aliyunLogUtil == null){
			return false;
		}
		
		//将map转化为 logitem
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			LogItem logItem = mapToLogitem(map);
			aliyunLogUtil.logGroupCache.add(logItem);
		}
		try {
			aliyunLogUtil.cacheCommit(table);
		} catch (LogException e) {
			e.printStackTrace();
			return false;
		}
		
//		
//		try {
//				if(aliyunLogUtil.logGroupCache.size() > 0){
//					//如果有日志，那么自动提交日志
//					aliyunLogUtil.cacheCommit();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
		
		return true;
	}

	public static LogItem mapToLogitem(Map<String, Object> map) {
		LogItem logItem = aliyunLogUtil.newLogItem();
		for(Map.Entry<String, Object> entry : map.entrySet()){
			String value = null;
			if(entry.getValue() == null){
				value = "";
			}else{
				value = entry.getValue().toString()+"";
			}
			logItem.PushBack(entry.getKey(), value);
		}
		return logItem;
	}
	
}
