package cn.zvo.log;

import java.util.List;
import java.util.Map;
import cn.zvo.log.vo.LogListVO;

/**
 * 日志记录的接口
 * @author 管雷鸣
 * @deprecated 使用 {@link DatasourceInterface}
 */
public interface LogInterface1 {
	
	/**
	 * 设置日志存储到哪个日志仓库中
	 * <br/>这里以数据库为例，数据库有多个表，每个表都会存储不同的数据（结构）
	 * <br/>这里便是每个表代表一个数据仓库。通过设置此，可切换将数据存入不同的数据仓库
	 * <br/>例如 ：
	 * 	<ul>
	 * 		<li>elasticsearch ： 这里便是设置索引的名字，可以将不同的数据存入不同的索引中</li>
	 * 		<li>阿里云SLS日志服务 ： 这里便是设置的日志库的名字，可将不同的数据存入不同的日志库中</li>
	 * 		<li>华为云LTS日志服务 ： 这里便是设置的日志流的名字</li>
	 * 		<li>...</li>
	 *  </ul>
	 * @param name
	 */
//	public void setTable(String name);
	
	/**
	 * 增加一条日志到缓存中，等待提交。这里增加的并不是立即提交。如果想要立即提交，可以设置。。。
	 * @param map 提交的键值对
	 */
//	public void add(Map<String, Object> map);
	
	/**
	 * 提交日志
	 * @param table 设置日志存储到哪个库中
	 * 	<p>这里以数据库为例，数据库有多个表，每个表都会存储不同的数据（结构）</p>
	 * 	<p>这里便是每个表代表一个数据仓库。通过设置此，可切换将数据存入不同的数据仓库</p>
	 * 	<p>例如 ：
	 * 		<ul>
	 * 			<li>elasticsearch ： 这里便是设置索引的名字，可以将不同的数据存入不同的索引中</li>
	 * 			<li>阿里云SLS日志服务 ： 这里便是设置的日志库的名字，可将不同的数据存入不同的日志库中</li>
	 * 			<li>华为云LTS日志服务 ： 这里便是设置的日志流的名字</li>
	 * 			<li>...</li>
	 *  	</ul>
	 *   </p>
	 *  @param map 要提交的日志集合（多数情况下的日志提交是多条日志数据一块提交，并不是一条日志提交一次，避免被拖慢响应速度）
	 * @return true:成功
	 */
	public boolean commit(String table, List<Map<String, Object>> list);
	
	/**
	 * 缓存中得日志条数，也就是通过 add 添加后，但还未调用commit进行提交得日志条数，待提交日志得条数
	 * @return 数字，条数
	 */
//	public int size();
	
	/**
	 * 将日志查询出来，以列表+分页的数据输出
	 * <p>获取到的数据排序规则：这里是按照数据加入的顺序，倒序排列，插入越往后的，显示越靠前</p>
	 * @param table 设置日志存储到哪个库中
	 * 	<p>这里以数据库为例，数据库有多个表，每个表都会存储不同的数据（结构）</p>
	 * 	<p>这里便是每个表代表一个数据仓库。通过设置此，可切换将数据存入不同的数据仓库</p>
	 * 	<p>例如 ：
	 * 		<ul>
	 * 			<li>elasticsearch ： 这里便是设置索引的名字，可以将不同的数据存入不同的索引中</li>
	 * 			<li>阿里云SLS日志服务 ： 这里便是设置的日志库的名字，可将不同的数据存入不同的日志库中</li>
	 * 			<li>华为云LTS日志服务 ： 这里便是设置的日志流的名字</li>
	 * 			<li>...</li>
	 *  	</ul>
	 *   </p>
	 * @param indexName 索引名字
	 * @param query 查询条件，传入如： name:guanleiming AND age:123
	 * @param everyPageNumber 每页显示几条，最大100
	 * @param currentPage 当前要获取第几页得数据
	 * @return 如果失败， vo.getResult() == ActionLogListVO.FAILURE
	 */
	public LogListVO list(String table, String query,int everyPageNumber, int currentPage);
	
}
