package cn.zvo.log.framework.springboot;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.xnx3.Lang;
import com.xnx3.ScanClassUtil;
import cn.zvo.log.LogInterface;

/**
 * 日志
 * @author 管雷鸣
 */
public class Log extends cn.zvo.log.Log{

    /**
     * 加载配置 {@link ApplicationConfig} （aplication.properties/yml）文件的配置数据，通过其属性来决定使用何种配置。
     * <br>这个其实就相当于用java代码来动态决定配置
     * @param config
     */
    public void loadConfig(ApplicationConfig config) {
    	if(config == null) {
    		return;
    	}
    	
    	if(config.getCacheMaxNumber() != null && config.getCacheMaxNumber().trim().length() > 0) {
			this.setCacheMaxNumber(Lang.stringToInt(config.getCacheMaxNumber(), 100));
		}
		if(config.getCacheMaxTime() != null && config.getCacheMaxTime().trim().length() > 0) {
			this.setCacheMaxTime(Lang.stringToInt(config.getCacheMaxTime(), 60));
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
						this.setLogInterface(logInterface);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException  | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
    }
	
    /**
     * 克隆
     */
	public Log clone() {
		Log log = new Log();
		BeanUtils.copyProperties(this, log);
		return log;
	}
}
