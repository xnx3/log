package cn.zvo.log.demo;

import java.util.HashMap;
import java.util.Map;
import cn.zvo.log.Log;
import cn.zvo.log.datasource.elasticsearch.ElasticSearchDataSource;

/**
 * 日志的Demo示例
 * @author 管雷鸣
 */
public class Demo{
	public static void main(String[] args) {
		Log log = new Log();
		//log.setLogInterface(new ElasticSearchDataSource("127.0.0.1", 9200, "http", null, null));  //设置使用elasticsearch
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("author", "管雷鸣");
		params.put("url", "https://github.com/xnx3/log");
		
		log.add(params);
	}
}
