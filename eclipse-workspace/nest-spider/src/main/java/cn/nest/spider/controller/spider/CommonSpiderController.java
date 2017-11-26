package cn.nest.spider.controller.spider;

import java.util.Map;

import org.apache.commons.lang3.tuple.Triple;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import cn.nest.spider.entity.commons.SpiderInfo;
import cn.nest.spider.entity.commons.Webpage;
import cn.nest.spider.service.CommonSpiderService;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.ResultList;

@Controller
public class CommonSpiderController {

	private static final Gson GSON = new Gson();
	
	@Autowired
	private CommonSpiderService service;
	
	@RequestMapping(value = "spider/start.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultBundle<String> start(String spiderInfoJson) {
		return service.start(GSON.fromJson(spiderInfoJson, SpiderInfo.class));
	}
	
	@RequestMapping(value = "spider/startAll.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultList<String> startAll(String spiderInfoIdList) {
		return service.startAll(Lists.newArrayList(spiderInfoIdList.split(",")));
	}
	
	@RequestMapping(value = "spider/stop.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultBundle<String> stop(String uuid) {
		return service.stop(uuid);
	}
	
	@RequestMapping(value = "spiderinfo/testSpiderInfo.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultList<Webpage> testSpiderInfo(String spiderInfoJson) {
		ResultList<Webpage> list = null;
		try {
			list = service.testSpiderInfo(spiderInfoJson);
		} catch(Exception e) {
			return new ResultList<Webpage>(null, 0, false, e.getLocalizedMessage());
		}
		return list;
	}
	
	@RequestMapping("spider/progress.do")
	@ResponseBody
	public Integer progress() {
		return service.progress();
	}
	
	@RequestMapping("task/delAll.do")
	public void delAll() {
		service.delAll();
	}
	
	@RequestMapping(value = "quartz/createQuartz.do", method = RequestMethod.POST)
	public String createQuartz(String infoId, int interval) {
		service.createQuartzJob(infoId, interval);
	return "redirect:/nest-spider/spider/quartz_list.html";
	}
	
	@RequestMapping(value = "quartz/createQuartz.do", method = RequestMethod.GET)
	public String createQuartz(String infoId, Model model) {
		model.addAttribute("infoId", infoId);
		model.addAttribute("interval", 1);
		return "pages/spider/quartz_list";
	}
	
	@RequestMapping(value = "quartz/listAll.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultBundle<Map<String, Triple<SpiderInfo, JobKey, Trigger>>> listAll() {
		return service.listAllJobs();
	}
	
	@RequestMapping(value = "quartz/removeQuartz.do", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBundle<String> removeQuartzJob(String infoId) {
        return service.removeQuartzJob(infoId);
    }
	
}
