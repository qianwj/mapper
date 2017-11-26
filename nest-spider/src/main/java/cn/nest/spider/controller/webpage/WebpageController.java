package cn.nest.spider.controller.webpage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.nest.spider.entity.commons.Webpage;
import cn.nest.spider.service.WebpageService;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.TablePage;

@Controller
@RequestMapping("webpage")
public class WebpageController {

	@Autowired
	private WebpageService service;
	
	@RequestMapping("/search_list.html")
	public String searchList(@RequestParam(required = false, defaultValue = "1")int page, HttpServletRequest request) {
		page = page<1?1:page;
		Pair<List<Webpage>, Long> list = service.listAll(page, 10).getResult();
		request.setAttribute("resultList", list.getLeft());
		TablePage tp = new TablePage(list.getRight(), page, 10);
		tp.checkAgain();
		request.setAttribute("tp", tp);
		return "pages/webpage/search_list";
	}
	
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false)String query, @RequestParam(required = false)String domain, @RequestParam(defaultValue = "1", required = false)int page) {
		ModelAndView mav = new ModelAndView("pages/webpage/search_list");
		StringBuilder str = new StringBuilder();
		if(StringUtils.isNotBlank(query)) 
			str.append("&query=" + query.trim());
		else if(StringUtils.isNotBlank(domain)) 
			str.append("&domain=" + domain.trim());
		else
			str.append("&query=&domain=");
		page = page<1?1:page;
		TablePage tp = null;
		ResultBundle<Pair<List<Webpage>, Long>> bundle = service.findByKeyWordAndDomain(query, domain, page, 10);
		if(bundle.getResult() == null)
			return new ModelAndView("error").addObject("errorMsg", "查找不到您要找的数据，可能是ES里没有存储过哦");
		Long totalRow = bundle.getResult().getRight();
		if(totalRow>0) {
			tp = new TablePage(totalRow, page, 10);
			tp.checkAgain();
			tp.setOtherParam(str.toString());
		}
		mav.addObject("tablepage", tp).addObject("resultList", bundle.getResult().getKey());
		return mav;
	}
	
	@RequestMapping(value = "/showWebpage.do", method = RequestMethod.GET)
	public String findWebpage(String id, HttpServletRequest request) {
		request.setAttribute("webpage", service.findWebpage(id));
		request.setAttribute("relatedList", service.moreLikeThis(id, 1, 15).getResult());
		return "webpage/showWebpage";
	}
	
	@RequestMapping(value = "/findWebpage.do", method = RequestMethod.GET)
	@ResponseBody
	public ResultBundle<Webpage> findWebpage(String id) {
		return service.findWebpage(id);
	}
	
	@RequestMapping(value = "/domain_list.html", method = RequestMethod.GET)
    public ModelAndView domainList(@RequestParam(defaultValue = "50", required = false, value = "size") int size) {
        ModelAndView mav = new ModelAndView("pages/webpage/domain_list");
        ResultBundle<Map<String, Long>> result = service.countDomain(size);
        if(result == null)
        	return mav;
        mav.addObject("domainList", result.getResult());
        return mav;
    }
	
	@RequestMapping(value = "/exportWebpageJSONByDomain.do", method = RequestMethod.GET, produces = "application/octet-stream")
	public void exportWebpageJSONByDomain(String domain, 
			@RequestParam(value = "includeRaw", required = false, defaultValue = "false") Boolean includeRaw,
            HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader("Content-Disposition", "attachment;fileName=" + new String(domain.getBytes("UTF-8"), "iso-8859-1") + ".segtxt");
		OutputStream out = response.getOutputStream();
		service.exportWebpageJSONByDomain(domain, includeRaw, out);
		out.close();
	}
	
	@RequestMapping(value = "/exportTitleContentPairBySpiderUUID.do", method = RequestMethod.GET, produces = "application/octet-stream")
	public void exportTitleContentPairBySpiderUUID(String uuid, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;fileName=" + new String((uuid).getBytes("UTF-8"), "iso-8859-1") + ".segtxt");
        OutputStream out = response.getOutputStream();
        service.exportTitleContentPairBySpiderUUID(uuid, out);
        out.close();
	}
}
