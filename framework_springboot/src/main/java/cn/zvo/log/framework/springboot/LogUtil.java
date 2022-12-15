package cn.zvo.log.framework.springboot;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import cn.zvo.log.datasource.console.ConsoleDataSource;
import cn.zvo.log.vo.LogListVO;

/**
 * 日志
 * @author 管雷鸣
 */
@EnableConfigurationProperties(ApplicationConfig.class)
@Configuration
public class LogUtil{
	public static Log log;
    @Resource
    private ApplicationConfig config;
	
    /**
     * springboot启动成功后自动执行初始化
     */
    @PostConstruct
	public void init() {
    	log = new Log();
    	loadConfig(this.config); //加载application配置
	}
    

    /**
     * 加载配置 {@link ApplicationConfig} （aplication.properties/yml）文件的配置数据，通过其属性来决定使用何种配置。
     * <br>这个其实就相当于用java代码来动态决定配置
     * @param config
     */
    public void loadConfig(ApplicationConfig config) {
    	log.loadConfig(this.config); //加载application配置
    }
    
    /**
     * 获取log对象
     */
    public static Log getLog() {
//    	System.out.println("log -- getLog()");
    	if(log == null) {
    		com.xnx3.Log.debug("log -- LogUtil().init();");
    		new LogUtil().init();
    	}
    	return log;
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
	public static boolean isDataSource(Class datasourceClass){
		return getLog().isDataSource(datasourceClass);
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
	public static boolean isDataSource(String datasourceClassName){
		return getLog().isDataSource(datasourceClassName);
	}
	

	/**
	 * 添加一条日志。
	 * <br/>（记录在缓存中，还未提交，达到多少条、或多少秒后自动提交，也或者执行 cacheCommit() 手动提交）
	 * @param params 日志主体，传入要保存的键值对。
	 */
	public static synchronized void add(Map<String, Object> params){
		if(params == null){
			return;
		}
		if(params.size() == 0){
			return;
		}
		
		getLog().add(params);
	}

	/**
	 * 将缓存中的日志提交
	 * @return true:成功
	 */
	public static boolean commit(){
		return getLog().commit();
	}
	
	/**
	 * 将日志查询出来，以列表+分页的数据输出
	 * @param query 要查询的单词或者文字、又或者传入如： name:guanleiming AND age:123
	 * @param everyPageNumber 每页显示多少条，也就是list返回多少条数据
	 * @param currentPage 当前要获取第几页得数据
	 * @return {@link LogListVO}
	 */
	public static LogListVO list(String query, int everyPageNumber, int currentPage){
		return getLog().list(query, everyPageNumber, currentPage);
	}
	
}
