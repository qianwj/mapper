package cn.nest.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasicController {
	
	@RequestMapping("/index.html") 
	public String welcome() {
		return "index";
	}
	
	@RequestMapping("spider/edit_spiderinfo.html")
	public String editSpiderInfo() {
		return "pages/spider/edit_spiderinfo";
	}
	
	@RequestMapping("spider/quartz_list.html")
	public String quartzList() {
		return "pages/spider/quartz_list";
	}
	
	
	@RequestMapping("spider/task_list.html")
	public String taskList() {
		return "pages/spider/task_list";
	}
	
	@RequestMapping("spiderinfo/update_data.html")
	public String updateData() {
		return "pages/spiderinfo/update_data";
	}
}
