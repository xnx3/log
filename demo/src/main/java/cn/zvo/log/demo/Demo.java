package cn.zvo.log.demo;

import java.util.HashMap;
import java.util.Map;
import cn.zvo.log.Log;
import cn.zvo.log.datasource.elasticsearch.ElasticSearchDataSource;
import cn.zvo.log.datasource.file.FileDataSource;

/**
 * 日志的Demo示例
 * @author 管雷鸣
 */
public class Demo{
	public static void main(String[] args) {
		//log可以用static修饰，一次声明，多次使用。
		Log log = new Log();
		log.setDatasource(new FileDataSource("/mnt/tomcat/log/"));	//将日志保存到本地，日志文件存放于 /mnt/tomcat/log/ 目录下
		log.setCacheMaxNumber(1);
		log.setCacheMaxTime(0);
		log.setTable("test");
		
		//加入一条日志
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("author", "管雷鸣");
		params.put("url", "https://github.com/xnx3/log");
		log.add(params);
	}
}
