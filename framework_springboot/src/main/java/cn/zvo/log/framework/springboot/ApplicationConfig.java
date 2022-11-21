package cn.zvo.log.framework.springboot;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.properties / yml 中的一些相关配置项目
 * @author 管雷鸣
 */
@Component(value = "FildUploadApplicationConfig")
@ConfigurationProperties(prefix = "log")
public class ApplicationConfig {
	//自定义存储方式时，创建存储方式的初始化相关参数
	private Map<String, Map<String, String>> dataSource;
	
	//缓存日志的最大条数。当达到这个条数后，将自动提交日志。默认为100
	private String cacheMaxNumber;
	
	//缓存日志的最大时间，单位为秒。不设置默认是60。超过这个时间后，将会自动提交日志。即每隔多长时间就自动提交日志，然后重新开始累计即时
	private String cacheMaxTime;
	
	public Map<String, Map<String, String>> getDataSource() {
		return dataSource;
	}
	public void setDataSource(Map<String, Map<String, String>> dataSource) {
		this.dataSource = dataSource;
	}
	public String getCacheMaxNumber() {
		return cacheMaxNumber;
	}
	public void setCacheMaxNumber(String cacheMaxNumber) {
		this.cacheMaxNumber = cacheMaxNumber;
	}
	public String getCacheMaxTime() {
		return cacheMaxTime;
	}
	public void setCacheMaxTime(String cacheMaxTime) {
		this.cacheMaxTime = cacheMaxTime;
	}
	
}