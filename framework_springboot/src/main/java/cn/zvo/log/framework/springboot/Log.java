package cn.zvo.log.framework.springboot;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import com.xnx3.Lang;
import com.xnx3.ScanClassUtil;

import cn.zvo.log.DatasourceInterface;

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
		if(config.getTableName() != null && config.getTableName().trim().length() > 0) {
			this.setTable(config.getTableName());
		}

		if(config.getDataSource() != null) {
			for (Map.Entry<String, Map<String, String>> entry : config.getDataSource().entrySet()) {
				//拼接，取该插件在哪个包
				String datasourcePackage = "cn.zvo.log.datasource."+entry.getKey();
				List<Class<?>> classList = ScanClassUtil.getClasses(datasourcePackage);
				if(classList.size() == 0) {
					System.err.println("====================");
					System.err.println(" 【【【 ERROR 】】】    ");
					System.err.println(" log 未发现 "+datasourcePackage +" 这个包存在，请确认pom.xml是否加入了这个 datasource 支持模块");
					System.err.println("====================");
					continue;
				}else {
					for (int i = 0; i < classList.size(); i++) {
						com.xnx3.Log.debug("class list item : "+classList.get(i).getName());
					}
				}
				
				//搜索继承StorageInterface接口的
//				List<Class<?>> logClassList = ScanClassUtil.searchByInterfaceName(classList, "cn.zvo.log.LogInterface");
//				logClassList.addAll( ScanClassUtil.searchByInterfaceName(classList, "cn.zvo.log.DatasourceInterface"));
				List<Class<?>> logClassList = ScanClassUtil.searchByInterfaceName(classList, "cn.zvo.log.DatasourceInterface");
				for (int i = 0; i < logClassList.size(); i++) {
					Class logClass = logClassList.get(i);
					com.xnx3.Log.debug("log datasource : "+logClass.getName());
					try {
						Object newInstance = logClass.getDeclaredConstructor(Map.class).newInstance(entry.getValue());
						DatasourceInterface datasource = (DatasourceInterface) newInstance;
						this.setDatasource(datasource);
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
