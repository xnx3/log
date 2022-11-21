package cn.zvo.log.framework.springboot;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.xnx3.Lang;
import com.xnx3.ScanClassUtil;
import cn.zvo.log.Log;
import cn.zvo.log.LogInterface;
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
//		
		//Log.debug(config.toString());
		if(config.getCacheMaxNumber() != null && config.getCacheMaxNumber().trim().length() > 0) {
			log.setCacheMaxNumber(Lang.stringToInt(config.getCacheMaxNumber(), 100));
		}
		if(config.getCacheMaxTime() != null && config.getCacheMaxTime().trim().length() > 0) {
			log.setCacheMaxTime(Lang.stringToInt(config.getCacheMaxTime(), 60));
		}

		if(config.getDataSource() != null) {
			for (Map.Entry<String, Map<String, String>> entry : config.getDataSource().entrySet()) {
				//拼接，取该插件在哪个包
				String datasourcePackage = "cn.zvo.log.datasource."+entry.getKey();
				
				List<Class<?>> classList = ScanClassUtil.getClasses(datasourcePackage);
				//搜索继承StorageInterface接口的
				List<Class<?>> logClassList = ScanClassUtil.searchByInterfaceName(classList, "cn.zvo.log.LogInterface");
				for (int i = 0; i < logClassList.size(); i++) {
					Class logClass = logClassList.get(i);
					com.xnx3.Log.debug("log datasource : "+logClass.getName());
					try {
						Object newInstance = logClass.getDeclaredConstructor(Map.class).newInstance(entry.getValue());
						LogInterface logInterface = (LogInterface) newInstance;
						log.setLogInterface(logInterface);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException  | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
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
		
		log.add(params);
	}

	/**
	 * 将缓存中的日志提交
	 * @return true:成功
	 */
	public static boolean commit(){
		return log.commit();
	}
	
	/**
	 * 将日志查询出来，以列表+分页的数据输出
	 * @param query 要查询的单词或者文字、又或者传入如： name:guanleiming AND age:123
	 * @param everyPageNumber 每页显示多少条，也就是list返回多少条数据
	 * @param currentPage 当前要获取第几页得数据
	 * @return {@link LogListVO}
	 */
	public static LogListVO list(String query, int everyPageNumber, int currentPage){
		return log.list(query, everyPageNumber, currentPage);
	}
	
}
