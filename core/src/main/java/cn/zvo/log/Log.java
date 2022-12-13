package cn.zvo.log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.xnx3.DateUtil;
import cn.zvo.log.datasource.console.ConsoleDataSource;
import cn.zvo.log.vo.LogListVO;

/**
 * 
 * 日志
 * @author 管雷鸣
 */
public class Log {
	
	/**
	 * 实现日志服务记录的接口。如 elasticsearch、阿里云SLS日志服务、华为云LTS日志服务……
	 * @deprecated 
	 */
	public LogInterface logInterface = null;
	/**
	 * 实现日志服务记录的接口。如 elasticsearch、阿里云SLS日志服务、华为云LTS日志服务……
	 */
	public DatasourceInterface datasource = null;
	
	//缓存日志的最大条数。当达到这个条数后，将自动提交日志。默认为100
	public int cacheMaxNumber = 100;		
	//缓存日志的最大时间，单位为秒。超过这个时间后，将会自动提交日志。即每隔多长时间就自动提交日志，然后重新开始累计即时
	public int cacheMaxTime = 60;
	//最后一次提交日志的时间 ，动态时间，每次提交日志后，都会重新刷新此时间,13位时间戳
	public long lastSubmitTime = 0;
	
	/**
	 * 缓存。
	 * key:  table
	 * value： 要打包提交的 List
	 */
	public Map<String, List<Map<String, Object>>> cacheMap;

	
	/**
	 * 设置日志存储到哪个日志仓库中
	 * <p>这里以数据库为例解释，以便于理解。数据库有多个表，每个表都会存储不同的数据（结构）</p>
	 * <p>这里便是每个表代表一个数据仓库。通过设置此，可切换将数据存入不同的数据仓库</p>
	 * <p>例如 ：
	 * 	<ul>
	 * 		<li>elasticsearch ： 这里便是设置索引的名字，可以将不同的数据存入不同的索引中</li>
	 * 		<li>阿里云SLS日志服务 ： 这里便是设置的日志库的名字，可将不同的数据存入不同的日志库中</li>
	 * 		<li>华为云LTS日志服务 ： 这里便是设置的日志流的名字</li>
	 * 		<li>...</li>
	 *  </ul>
	 *  </p>
	 */
	public String table;
	
	public Log() {
		cacheMap = new HashMap<String, List<Map<String,Object>>>();
	}
	
	/**
	 * 实现日志服务记录的接口。如 elasticsearch、阿里云SLS日志服务、华为云LTS日志服务……
	 * @deprecated
	 */
	public DatasourceInterface getLogInterface() {
//		if(logInterface == null) {
//			//没有，默认使用控制台输出方式
//			logInterface = new ConsoleDataSource(null);
//		}
//		return logInterface;
		return getDatasource();
	}

	/**
	 * 实现日志服务记录的接口。如 elasticsearch、阿里云SLS日志服务、华为云LTS日志服务……
	 * @param logInterface 
	 */
	public void setLogInterface(DatasourceInterface datasource) {
//		this.logInterface = logInterface;
//		this.logInterface.setTable(this.table);
		setDatasource(datasource);
	}
	
	public DatasourceInterface getDatasource() {
		if(datasource == null) {
			//没有，默认使用控制台输出方式
			datasource = new ConsoleDataSource(null);
		}
		return datasource;
	}

	public void setDatasource(DatasourceInterface datasource) {
		this.datasource = datasource;
	}

	public int getCacheMaxNumber() {
		return cacheMaxNumber;
	}

	public void setCacheMaxNumber(int cacheMaxNumber) {
		this.cacheMaxNumber = cacheMaxNumber;
	}

	public int getCacheMaxTime() {
		return cacheMaxTime;
	}

	public void setCacheMaxTime(int cacheMaxTime) {
		this.cacheMaxTime = cacheMaxTime;
	}

	/**
	 * 设置日志存储到哪个日志仓库中
	 * <p>这里以数据库为例解释，以便于理解。数据库有多个表，每个表都会存储不同的数据（结构）</p>
	 * <p>这里便是每个表代表一个数据仓库。通过设置此，可切换将数据存入不同的数据仓库</p>
	 * <p>例如 ：
	 * 	<ul>
	 * 		<li>elasticsearch ： 这里便是设置索引的名字，可以将不同的数据存入不同的索引中</li>
	 * 		<li>阿里云SLS日志服务 ： 这里便是设置的日志库的名字，可将不同的数据存入不同的日志库中</li>
	 * 		<li>华为云LTS日志服务 ： 这里便是设置的日志流的名字</li>
	 * 		<li>...</li>
	 *  </ul>
	 *  </p>
	 * @param name 要存到哪个（如数据表）中，传入其名字
	 */
	public void setTable(String name) {
		this.table = name;
//		this.logInterface.setTable(name);
	}
	

	/**
	 * 判断当前日志使用的是哪种方式
	 * @param datasourceClass 哪种方式的实现类，如默认带的本地打印为 {@link ConsoleDataSource} ，这里如果要判断是否是使用的控制台打印，可传入 ConsoleDataSource.class
	 * @return 是否使用
	 * 			<ul>
	 * 				<li>true ： 是此种方式</li>
	 * 				<li>false ： 不是此种方式</li>
	 * 			</ul>
	 */
	public boolean isDataSource(Class datasourceClass){
		String name = datasourceClass.getSimpleName();
		return isDataSource(name);
	}
	

	/**
	 * 判断当前日志使用的是哪种方式
	 * @param storageClassName 方式的实现类的名字，如默认带的本地打印为 {@link ConsoleDataSource} ，这里如果要判断是否是使用的控制台打印，可传入  "ConsoleDataSource"
	 * @return 是否使用
	 * 			<ul>
	 * 				<li>true ： 是此种模式</li>
	 * 				<li>false ： 不是此种模式</li>
	 * 			</ul>
	 */
	public boolean isDataSource(String datasourceClassName){
		//取得当前实现的文件的名字，例如本地存储的命名为 LocalServerMode.java ,那这里会取到 LocalServerMode
		String currentModeFileName = this.getLogInterface().getClass().getSimpleName();
		if(currentModeFileName.equalsIgnoreCase(datasourceClassName)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 添加一条日志。将之提交到缓存中，等积累到一定条件（条数、时间）之后，在一起将Java缓存中的日志打包一次性提交上去
	 * <p>默认缓存最大条数是100条，达到100条会自动提交到 。 这个最大条数，可以通过  {@link #setCacheMaxNumber(int)} 进行设置。建议不要超过4000条 </p>
	 * <p>如果你想调用此方法后不经过缓存而立即提交，您可设置 {@link #setCacheMaxNumber(int)} 为1即可</p>
	 * @param params 要增加的日志数据，key-value形式。 其中map.value 支持的类型有 String、int、long、float、double、boolean
	 */
	public synchronized void add(Map<String, Object> params){
		if(params == null){
			return;
		}
		if(params.size() == 0){
			return;
		}
		
		List<Map<String,Object>> list = cacheMap.get(this.table);
		if(list == null){
			list = new LinkedList<Map<String,Object>>();
		}
		list.add(params);
		
//		if(list.size() >= this.cacheMaxNumber){
//			//提交
//			boolean submit = commit();
//			if(submit){
//				//提交成功，那么清空indexName的list
//				list.clear();
//			}
//		}
		
		//重新赋予cacheMap
		cacheMap.put(this.table, list);
		
//		getLogInterface().add(params);
		checkCommit();	//检测是否要提交
	}


	/**
	 * 缓存中得日志条数，也就是通过 add 添加后，但还未调用commit进行提交得日志条数，待提交日志得条数
	 * @return 数字，条数
	 */
	public int size() {
		return cacheMap.get(this.table).size();
	}
	
	private void checkCommit() {
		checkCommit(false);
	}
	
	/**
	 * 
	 * @param submit 是否是要直接进行提交？true则是直接提交缓存数据，不经过时间、条数判断。
	 */
	private void checkCommit(boolean submit) {
		int currentCacheSize = size();	//当前缓存中得日志条数
		long currentTime = DateUtil.timeForUnix13();	//当前时刻得13位时间戳
		
		if(!submit) {
			if(currentCacheSize > this.cacheMaxNumber-1){
				//超过定义的缓存最大值，那么将缓存中的日志数据进行提交
				submit = true;
			}else{
				if(lastSubmitTime + cacheMaxTime*1000 < currentTime){
					submit = true;
				}
			}
		}
		
		if(submit){
			this.lastSubmitTime = currentTime;	//赋予最后一次提交时间
			boolean submitResult = commit();
			if(submitResult){
				//提交成功，那么清空 table 的list
				cacheMap.put(this.table, new LinkedList<Map<String,Object>>());
			}
		}
	}
	
	/**
	 * 将缓存中的日志立即提交
	 * <p>一般情况下，此用不到，有 {@link #add(Map)} 根据设定的提交条件去自动处理提交 </p>
	 * @return true:成功
	 */
	public boolean commit(){
		return getDatasource().commit(this.table, this.cacheMap.get(this.table));
	}
	
	/**
	 * 将日志查询出来，以列表+分页的数据输出
	 * @param query 要查询的单词或者文字、又或者传入如： name:guanleiming AND age:123
	 * @param everyPageNumber 每页显示多少条，也就是list返回多少条数据
	 * @param currentPage 当前要获取第几页得数据
	 * @return {@link LogListVO}
	 */
	public LogListVO list(String query, int everyPageNumber, int currentPage){
		return getLogInterface().list(this.table ,query, everyPageNumber, currentPage);
	}
	
}
