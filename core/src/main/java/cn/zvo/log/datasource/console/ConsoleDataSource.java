package cn.zvo.log.datasource.console;

import java.util.Map;
import cn.zvo.log.LogInterface;
import cn.zvo.log.vo.LogListVO;
import net.sf.json.JSONObject;

/**
 * Java控制台输出
 * @author 管雷鸣
 */
public class ConsoleDataSource implements LogInterface{
	public ConsoleDataSource(Map<String, String> config) {
	}
	
	@Override
	public void add(Map<String, Object> map) {
		System.out.println(JSONObject.fromObject(map));
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
		
		return vo;
	}

	@Override
	public int size() {
		return 0;
	}
}