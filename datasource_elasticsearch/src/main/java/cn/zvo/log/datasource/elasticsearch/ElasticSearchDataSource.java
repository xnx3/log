package cn.zvo.log.datasource.elasticsearch;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import com.xnx3.Lang;
import com.xnx3.StringUtil;
import com.xnx3.elasticsearch.ElasticSearchUtil;
import com.xnx3.elasticsearch.jsonFormat.JsonFormatInterface;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.vo.ActionLogListVO;
import cn.zvo.log.DatasourceInterface;
import cn.zvo.log.vo.LogListVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * elasticsearch 模块。这里调试使用的是 7.10.1 版本
 * @author 管雷鸣
 */
public class ElasticSearchDataSource implements DatasourceInterface{
	public static final int PORT_DEFAULT = 9200;	//默认端口号
//	public String table;	//setTable 的值，查询的索引名字，类似于mysql的table名字
	public static ElasticSearchUtil es;
	
	/**
	 * 创建 ElasticSearch 实现
	 * @param hostname 主机，传入如 127.0.0.1
	 * @param port 端口，传入如 9200
	 * @param scheme 协议，传入如 http
	 * @param username 登录用户名，如果es未设置账号密码，则此项无需传入，传入null即可
	 * @param password 登录密码，如果es未设置账号密码，则此项无需传入，传入null即可
	 * @param indexName 索引名字，理解上类似于数据表的名字，要存储的数据是存到哪个表
	 */
	public ElasticSearchDataSource(String hostname, int port, String scheme, String username, String password, String indexName) {
		init(hostname, port, scheme, username, password);
	}
	
	public ElasticSearchDataSource(Map<String, String> config) {
		String hostname = config.get("hostname");
		int port = Lang.stringToInt(config.get("port"), PORT_DEFAULT);
		String scheme = config.get("scheme");
		String username = config.get("username");
		String password = config.get("password");
		
		init(hostname, port, scheme, username, password);
	}
	
	public void init(String hostname, int port, String scheme, String username, String password) {
		//判断是否使用es进行日志记录，条件便是 hostname 是否为空。若为空，则不使用
		if(hostname != null && hostname.length() > 0){
			//使用
			es = new ElasticSearchUtil(hostname, port, scheme);
			if((username != null && username.length() > 0) && (password != null && password.length() > 0)) {
				//使用账号密码模式
				es.setUsernameAndPassword(username, password);
			}
//			Log.info("log ElasticSearch进行操作记录");
			
			//自动检测用户动作的索引是否存在
//			if(!es.existIndex(indexName)){
//				//如果不存在，那么创建
//				try {
//					CreateIndexResponse res = es.createIndex(indexName);
//					Log.info("检测到用户动作的存储索引不存在，已自动创建 " +indexName+ " 索引。");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			
			//重写序列化接口
			es.setJsonFormatInterface(new JsonFormatInterface() {
				@Override
				public String mapToJsonString(Map<String, Object> params) {
					if(params == null){
						params = new HashMap<String, Object>();
					}
					return JSONObject.fromObject(params).toString();
				}
			});
			
			//动作记录使用本es的
//			ActionLogUtil.actionLogInterface = new ElasticSearchMode();
		}
		
	}
	
	public static void main(String[] args) {
		String indexName = "useraction";
		
		String queryString = "time > 0";
		
		Global.system.put("a", "");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		CountRequest countRequest = new CountRequest();
		//构造查询条件
		if(queryString != null && queryString.length() > 0){
			//有查询条件，才会进行查询，否则会查出所有
			QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(queryString);
			searchSourceBuilder.query(queryBuilder);
		}
		
		countRequest.indices(indexName).source(searchSourceBuilder);
		
//	    countRequest.indices(indexName).query(null);
		CountResponse countResponse = null;
		try {
			countResponse = es.getRestHighLevelClient().count(countRequest, RequestOptions.DEFAULT);
			System.out.println("countResponse.getCount(): "+countResponse.getCount());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	public void add(Map<String, Object> map) {
//		if(es == null){
//			//不使用，终止即可
//			return;
//		}
//		
//		es.cache(map, this.table);
//	}
	
	@Override
	public boolean commit(String table, List<Map<String, Object>> list) {
		es.cacheMap.put(table, list); //加入缓存
		return es.cacheSubmit(table);	//提交数据
	}

	/**
	 * 同上面的 list,只不过这里可以自定义操作 indexName。
	 * <p>获取到的数据排序规则：这里是按照数据加入的顺序，倒序排列，插入越往后的，显示越靠前</p>
	 * @param indexName 索引名字
	 * @param query 查询条件，传入如： name:guanleiming AND age:123
	 * @param everyPageNumber 每页显示几条，最大200
	 * @param request
	 * @return 如果失败， vo.getResult() == ActionLogListVO.FAILURE
	 */
	public LogListVO list(String table, String query,int everyPageNumber, int currentPage) {
		LogListVO vo = new LogListVO();
		
		//统计符合条件的总数量
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		CountRequest countRequest = new CountRequest();
		//构造查询条件
		if(query != null && query.length() > 0){
			//有查询条件，才会进行查询，否则会查出所有
			QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query);
			searchSourceBuilder.query(queryBuilder);
		}
		countRequest.indices(table).source(searchSourceBuilder);
		long count = 0;
		CountResponse countResponse = null;
		try {
			countResponse = es.getRestHighLevelClient().count(countRequest, RequestOptions.DEFAULT);
			count = countResponse.getCount();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Page page = new Page((int) count, everyPageNumber, request);

		//limit查询条数
		int limitNumber = everyPageNumber;
		//判断当前是否是最后一页
//		if(page.isCurrentLastPage()){
//			//最后一页，那limit条数肯定不满足一页多少条的标准了，肯定是要少的，那就计算出当前一共几条，就显示几条
//			limitNumber = (int) (count - page.getLimitStart() );
//		}

		long max_result_window = 10000;	//默认就是10000
		try {
			Response res = es.getRestClient().performRequest(new Request("GET", "/"+table+"/_settings"));
			String text = StringUtil.inputStreamToString(res.getEntity().getContent(), "UTF-8");
			JSONObject setJson = JSONObject.fromObject(text);
			JSONObject indexJson = setJson.getJSONObject(table).getJSONObject("settings").getJSONObject("index");
			if(indexJson.get("max_result_window") != null){
				//自己单独设置过 max_result_window
				max_result_window = indexJson.getLong("max_result_window");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//判断最大显示条数是否超过可显示的最大条数
		int limitStart = (currentPage-1)*everyPageNumber;	//开始的limit
		if((limitStart + limitNumber) > max_result_window){
			vo.setBaseVO(ActionLogListVO.FAILURE, "显示最大条数超过系统预设优化的最大条数"+max_result_window+"条。");
			ConsoleUtil.log("显示最大条数超过系统预设优化的最大条数"+max_result_window+"条。你可以设置ElasticSearch中，"+table+"索引的max_result_window属性来设置更大条数。");
			return vo;
		}
		
		//获取查询结果的数据 
		List<Map<String, Object>> list = es.search(table, query, limitStart, limitNumber, SortBuilders.fieldSort("time").order(SortOrder.DESC));
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			
			//兼容以前的阿里云日志服务的版本
			Object timeObj = map.get("time");
			if(timeObj == null){
				map.put("logtime", 0l);
			}else{
				map.put("logtime", timeObj);
			}
		}
		vo.setJsonArray(JSONArray.fromObject(list));
		
		return vo;
	}

//	@Override
//	public void setTable(String name) {
//		this.table = name;
//	}
}