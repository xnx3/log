package cn.zvo.log.demo;

import java.util.HashMap;
import java.util.Map;
import cn.zvo.log.Log;

/**
 * 日志的Demo示例
 * @author 管雷鸣
 */
public class Demo{
	public static Log log;
	
	static {
		log = new Log();
	}
	
	public static void main(String[] args) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("author", "管雷鸣");
		params.put("url", "https://github.com/xnx3/log");
		log.add(params);
	}
}
