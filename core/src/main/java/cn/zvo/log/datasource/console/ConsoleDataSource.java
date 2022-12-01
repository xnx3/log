package cn.zvo.log.datasource.console;

import java.util.Map;
import com.xnx3.BaseVO;
import com.xnx3.Log;

import cn.zvo.log.LogInterface;
import cn.zvo.log.vo.LogListVO;
import net.sf.json.JSONObject;

/**
 * Java控制台输出
 * @author 管雷鸣
 */
public class ConsoleDataSource implements LogInterface{
	/**
	 * 当调用add方法时是否将其日志数据打印出来，默认是是true：打印
	 */
	public static boolean print = true;
	
	public ConsoleDataSource(Map<String, String> config) {
	}
	
	@Override
	public void add(Map<String, Object> map) {
		if(!print) {
			return;
		}
		System.out.println("log : "+JSONObject.fromObject(map).toString());
	}
	
	@Override
	public boolean commit() {
		return true;
	}

	/**
	 * 同上面的 list,只不过这里可以自定义操作 indexName。
	 * <p>获取到的数据排序规则：这里是按照数据加入的顺序，倒序排列，插入越往后的，显示越靠前</p>
	 * @param indexName 索引名字
	 * @param query 查询条件，传入如： name:guanleiming AND age:123
	 * @param everyPageNumber 每页显示几条，最大200
	 * @param request
	 * @return 如果失败， vo.getResult() == ActionLogListVO.FAILURE
	 */
	public LogListVO list(String query,int everyPageNumber, int currentPage) {
		LogListVO vo = new LogListVO();
		vo.setBaseVO(BaseVO.FAILURE, "log采用默认的输出到控制台的方式，无获取的方法实现");
		return vo;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public void setTable(String name) {
		Log.debug("The datasource used is ConsoleDataSource. The setTable setting '"+name+"' is invalid and has been ignored.");
	}
}