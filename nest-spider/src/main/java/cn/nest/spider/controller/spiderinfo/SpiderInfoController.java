package cn.nest.spider.controller.spiderinfo;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import cn.nest.spider.entity.commons.SpiderInfo;
import cn.nest.spider.service.SpiderInfoService;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.ResultList;

@Controller
@RequestMapping("spiderinfo")
public class SpiderInfoController {

	private Gson gson = new Gson();
	
	@Autowired
	private SpiderInfoService service;
	
	@RequestMapping("/spiderinfo_list.html")
	public ModelAndView spiderinfoList(@RequestParam(defaultValue = "1", required = false) int page) {
		ResultList<SpiderInfo> spiderInfoList = service.listAll(page, 10);
		ModelAndView mav = new ModelAndView("pages/spiderinfo/spiderinfo_list");
		if(spiderInfoList.getRes().size() <= 0 || spiderInfoList == null) 
			return new ModelAndView("error").addObject("errorMsg", "ES中尚未存在模板");
		return mav.addObject("spiderInfoList", spiderInfoList.getRes());
	}
	
	@RequestMapping(value = "/save.do", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
	@ResponseBody
	public ResultBundle<String> save(String spiderInfoJson, HttpServletRequest request) {
		return service.index(gson.fromJson(spiderInfoJson, SpiderInfo.class));
	}
	
	@RequestMapping(value = "/importSpiderInfo.do", method = RequestMethod.POST)
	public String importSpiderInfo(String spiderInfoJson, HttpServletRequest request) {
		if(StringUtils.isNotBlank(spiderInfoJson)) {
			SpiderInfo info = gson.fromJson(spiderInfoJson, SpiderInfo.class);
			info = convertInfo(info);
		    request.setAttribute("spiderInfo", info);
		} else
			request.setAttribute("spiderInfo", null);
		return "pages/spider/edit_spiderinfo";
	}
	
	@RequestMapping(value = "/editSpiderInfo.do", method = RequestMethod.GET)
	public String editSpiderinfo(String spiderInfoId, HttpServletRequest request) {
		SpiderInfo info = service.getById(spiderInfoId).getResult();
		info = convertInfo(info);
		request.setAttribute("spiderInfo", info);
		request.setAttribute("spiderInfoson", gson.toJson(info));
		return "pages/spider/edit_spiderinfo";
	}
	
	@RequestMapping("/findByDomain.do")
	@ResponseBody
	public ResultList<SpiderInfo> findByDomain(String domain, @RequestParam(value = "size", required = false, defaultValue = "10") int size, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		return service.findByDomain(domain, page, size);
    }
	
	@RequestMapping(value = "/listSpiderinfo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String listSpiderInfo(String domain, @RequestParam(defaultValue = "1", required = false) int page, HttpServletRequest request) {
        if (StringUtils.isBlank(domain)) {
            request.setAttribute("spiderInfoList", service.listAll(10, page).getRes());
        } else {
        	request.setAttribute("spiderInfoList", service.findByDomain(domain, page, 10).getRes());
        }
        request.setAttribute("domain", domain);
        request.setAttribute("page", page);
        return "pages/spiderinfo/spiderinfo_list";
    }
	
	@RequestMapping(value = "/delById.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultBundle<Boolean> delById(String id) {
		return service.delById(id);
	}
	
	@RequestMapping(value = "/delByDomain.do", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResultBundle<Boolean> delByDomain(String domain) {
		return service.delByDomain(domain);
	}
	
	@RequestMapping(value = "/update.do", method = RequestMethod.POST)
	public ModelAndView update(String spiderInfoId, String spiderInfoJson, String callbackUrl) {
		List<String> callbackUrls = Lists.newArrayList(callbackUrl);
		ModelAndView mav = new ModelAndView("pages/spiderinfo/update_data");
		try {
			int traceId = service.update(spiderInfoId, spiderInfoJson, callbackUrls).getTraceId();
			mav.addObject("traceId", traceId);
		} catch(Exception e) {
			mav.addObject("errorMsg", "更新失败");
		}
		return mav;
	}
	
	private SpiderInfo convertInfo(SpiderInfo info) {
		info.setPublishTimeReg(StringEscapeUtils.escapeHtml4(info.getPublishTimeReg()))
    		.setCategoryReg(StringEscapeUtils.escapeHtml4(info.getCategoryReg()))
    		.setContentReg(StringEscapeUtils.escapeHtml4(info.getContentReg()))
    		.setTitleReg(StringEscapeUtils.escapeHtml4(info.getTitleReg()))
    		.setPublishTimeXPath(StringEscapeUtils.escapeHtml4(info.getPublishTimeXPath()))
    		.setCategoryXPath(StringEscapeUtils.escapeHtml4(info.getCategoryXPath()))
    		.setContentXPath(StringEscapeUtils.escapeHtml4(info.getContentXPath()))
    		.setTitleXPath(StringEscapeUtils.escapeHtml4(info.getTitleXPath()));
		for (SpiderInfo.FieldConfig config : info.getDynamicFields()) {
			  config.setRegex(StringEscapeUtils.escapeHtml4(config.getRegex()))
        			.setXpath(StringEscapeUtils.escapeHtml4(config.getXpath()));
		}
		return info;
	}
}
