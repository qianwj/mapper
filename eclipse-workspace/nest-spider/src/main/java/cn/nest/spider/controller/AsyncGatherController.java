package cn.nest.spider.controller;


import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import cn.nest.spider.entity.async.Task;
import cn.nest.spider.entity.util.State;
import cn.nest.spider.service.AsyncGatherService;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.ResultList;

@Controller
@RequestMapping("task")
public class AsyncGatherController {

	@Autowired
	private AsyncGatherService service;
	
	private Gson gson = new Gson();
	
	@RequestMapping(value = "/findTask.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultBundle<Task> findTask(String taskId, @RequestParam(value = "extra", required = false, defaultValue = "true")boolean extra) {
		return service.findTask(taskId, extra);
	}
	
	@RequestMapping(value = "/task_list.html", method = RequestMethod.GET)
	public String taskList(HttpServletRequest request, @RequestParam(required = false, defaultValue = "false")boolean showRunning) {
		ResultList<Task> list;
		if(!showRunning)
			list = service.findAll(true);
		else
			list = service.findTasksByState(State.RUNNING, true);
		try {
			request.setAttribute("result", list);
			request.setAttribute("count", service.countByState(State.RUNNING).getResult());
			request.setAttribute("infoList", list.getRes().stream().map(task -> 
			        StringEscapeUtils.escapeHtml4(gson.toJson(task.getExtraInfoByKey("spiderInfo"))
			)).collect(Collectors.toList()));
		} catch(Exception e) {
			request.setAttribute("result", null);
		}
		return "pages/task/task_list";
	}
	
	@RequestMapping(value = "/delTask.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultBundle<String> delTask(String uuid) {
		return service.delTask(uuid);
	}
	
}
