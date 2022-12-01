package cn.zvo.log;

import java.util.Map;

import org.elasticsearch.discovery.zen.MasterFaultDetection.ThisIsNotTheMasterYouAreLookingForException;

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
	 */
	public LogInterface logInterface = null;
	
	//缓存日志的最大条数。当达到这个条数后，将自动提交日志。默认为100
	public int cacheMaxNumber = 100;		
	//缓存日志的最大时间，单位为秒。超过这个时间后，将会自动提交日志。即每隔多长时间就自动提交日志，然后重新开始累计即时
	public int cacheMaxTime = 60;
	//最后一次提交日志的时间 ，动态时间，每次提交日志后，都会重新刷新此时间,13位时间戳
	public long lastSubmitTime = 0;
	
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
	
	/**
	 * 实现日志服务记录的接口。如 elasticsearch、阿里云SLS日志服务、华为云LTS日志服务……
	 */
	public LogInterface getLogInterface() {
		if(logInterface == null) {
			//没有，默认使用控制台输出方式
			logInterface = new ConsoleDataSource(null);
		}
		return logInterface;
	}

	/**
	 * 实现日志服务记录的接口。如 elasticsearch、阿里云SLS日志服务、华为云LTS日志服务……
	 * @param logInterface 
	 */
	public void setLogInterface(LogInterface logInterface) {
		this.logInterface = logInterface;
		this.logInterface.setTable(this.table);
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
		this.logInterface.setTable(name);
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
	 * 添加一条日志。
	 * <br/>（记录在缓存中，还未提交，达到多少条、或多少秒后自动提交，也或者执行 cacheCommit() 手动提交）
	 * @param params 日志主体，传入要保存的键值对。
	 */
	public synchronized void add(Map<String, Object> params){
		if(params == null){
			return;
		}
		if(params.size() == 0){
			return;
		}
		
		getLogInterface().add(params);
		checkCommit();	//检测是否要提交
	}

	private void checkCommit() {
		checkCommit(false);
	}
	
	/**
	 * 
	 * @param submit 是否是要直接进行提交？true则是直接提交缓存数据，不经过时间、条数判断。
	 */
	private void checkCommit(boolean submit) {
		int currentCacheSize = getLogInterface().size();	//当前缓存中得日志条数
		long currentTime = DateUtil.timeForUnix13();	//当前时刻得13位时间戳
		
		if(!submit) {
			if(currentCacheSize > this.cacheMaxNumber){
				//超过定义的缓存最大值，那么将缓存中的日志数据进行提交
				submit = true;
			}else{
				if(lastSubmitTime + cacheMaxTime < currentTime){
					submit = true;
				}
			}
		}
		
		if(submit){
			this.lastSubmitTime = currentTime;	//赋予最后一次提交时间
			commit();
		}
	}
	
	/**
	 * 将缓存中的日志立即提交
	 * @return true:成功
	 */
	public boolean commit(){
		return getLogInterface().commit();
	}
	
	/**
	 * 将日志查询出来，以列表+分页的数据输出
	 * @param query 要查询的单词或者文字、又或者传入如： name:guanleiming AND age:123
	 * @param everyPageNumber 每页显示多少条，也就是list返回多少条数据
	 * @param currentPage 当前要获取第几页得数据
	 * @return {@link LogListVO}
	 */
	public LogListVO list(String query, int everyPageNumber, int currentPage){
		return getLogInterface().list(query, everyPageNumber, currentPage);
	}
	
}
