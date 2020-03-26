package konantech.ai.aikwc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import konantech.ai.aikwc.common.config.AsyncConfig;
import konantech.ai.aikwc.common.config.CheckStatusHandler;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CommonService;
import konantech.ai.aikwc.service.CrawlService;

@Controller
@RequestMapping("simulator")
public class SimulatorController {
	@Resource
	CommonService commonService;
	
	@Resource
	CollectorService collectorService;
	@Autowired
	CrawlService crawlService;
	@Autowired
	AsyncConfig asyncConfig;
	@Autowired
	CheckStatusHandler statusHandler;
	
	@RequestMapping("/list")
	public String list(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo
			,Model model) {
		Map map = commonService.commInfo(agencyNo);
		Agency selAgency = (Agency) map.get("selAgency");
		model.addAttribute("selAgency", selAgency);
		model.addAttribute("agencyList", map.get("agencyList"));
		model.addAttribute("groupList", map.get("groupList"));
		model.addAttribute("agencyNo", selAgency.getPk());
		model.addAttribute("menuNo", "1");
		
		return "sml/runJob";
	}
	
	@RequestMapping("/schedule")
	public String runSchedule(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo
			,@RequestParam(name = "menuNo", required = false, defaultValue = "1") String menuNo
			,Model model) {
		return "sml/runSchedule";
	}
	
	
	
	@RequestMapping(value ="/collectorList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> collectorList(@RequestBody Map<String,String> params) {
		
		String site = params.get("site");
		List<Collector> result = new ArrayList<Collector>();
		if(site != null && !site.equals("")) {
			result = collectorService.getCollectorListInSiteInUse(site);
		}else
			result = collectorService.getCollectorList();
			
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("taskCnt", asyncConfig.getTaskCount());
		
		return map;
	}
	
	@RequestMapping("/crawl")
	@ResponseBody
	public void getCrawl(@RequestBody Collector collector) {
		
		Collector selectedCollector = collectorService.getCollectorInfo(collector.getPk());
		selectedCollector.setStartPage(collector.getStartPage());
		selectedCollector.setEndPage(collector.getEndPage());
		
		String agencyName = collectorService.getAgencyNameForCollector(selectedCollector.getToSite().getGroup().getAgency());
		selectedCollector.getToSite().getGroup().setAgencyName(agencyName);
		
		//1. update Running status
		collectorService.updateStatus(collector.getPk(), "R");
		try {
			crawlService.webCrawl(selectedCollector);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				statusHandler.sendTaskCnt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

