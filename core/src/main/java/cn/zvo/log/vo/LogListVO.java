package cn.zvo.log.vo;

import com.xnx3.BaseVO;

import cn.zvo.page.Page;
import net.sf.json.JSONArray;

/**
 * com.xnx3.j2ee.util.actionLog.list 方法返回所用
 * @author 管雷鸣
 *
 */
public class LogListVO extends BaseVO {
	private JSONArray jsonArray;	//json数组，数组内有json对象，每个json对象都是一列数据。数据都存在这里。
	private Page page;
	
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	@Override
	public String toString() {
		return "LogListVO [jsonArray=" + jsonArray + ", page=" + page + "]";
	}
	
}
