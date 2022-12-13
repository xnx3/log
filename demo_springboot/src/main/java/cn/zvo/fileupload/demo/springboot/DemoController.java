package cn.zvo.fileupload.demo.springboot;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xnx3.DateUtil;
import cn.zvo.log.framework.springboot.LogUtil;
import cn.zvo.log.vo.LogListVO;
import net.sf.json.JSONObject;

/**
 * 日志的demo演示
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/")
public class DemoController{
	
	/**
	 * 向日志中添加一条数据
	 */
	@RequestMapping(value="add.json")
	@ResponseBody
	public String add(){
		//添加一条数据到日志
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", "管雷鸣");
		params.put("action", "访问测试接口的动作");
		params.put("time", DateUtil.timeForUnix13());
		LogUtil.add(params);
		
		return "测试增加一条数据："+JSONObject.fromObject(params).toString();
	}
	

	/**
	 * 将日志中的数据取出进行列表分页展示
	 */
	@RequestMapping(value="list.json")
	@ResponseBody
	public LogListVO list(@RequestParam(defaultValue="1") int currentPage){
		LogListVO vo = LogUtil.list("", 5, currentPage); //每页显示5条，取出数据
		return vo;
	}

	
}
