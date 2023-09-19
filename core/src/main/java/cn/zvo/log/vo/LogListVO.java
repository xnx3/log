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
	/**
	 * 【旧的，已废弃， 使用list】json数组，数组内有json对象，每个json对象都是一列数据。数据都存在这里。
	 * @deprecated
	 */
	private JSONArray jsonArray;	
	private Page page;
	private JSONArray list; 	//列表数据，也就是之前的 jsonArray
	
	public JSONArray getJsonArray() {
		return list;
	}
	public void setJsonArray(JSONArray list) {
		this.list = list;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	public JSONArray getList() {
		return list;
	}
	public void setList(JSONArray list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "LogListVO [list=" + list + ", page=" + page + "]";
	}
	
}
