package konantech.ai.aikwc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import konantech.ai.aikwc.common.config.AsyncConfig;
import konantech.ai.aikwc.common.config.StatusWebSocketHandler;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.KLog;
import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CommonService;
import konantech.ai.aikwc.service.CrawlService;
import konantech.ai.aikwc.service.ScheduleService;
import konantech.ai.aikwc.service.TaskService;

@Controller
@RequestMapping("simulator")
public class SimulatorController {
	@Autowired
	CommonService commonService;
	@Autowired
	CrawlService crawlService;
	@Autowired
	AsyncConfig asyncConfig;
	@Autowired
	StatusWebSocketHandler statusHandler;
	
	@Resource(name = "CollectorService")
	CollectorService collectorService;
	
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	TaskService taskService;
	
	@RequestMapping("/list")
	public String list(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo
			,Model model) {
		model.addAttribute("menuNo", "1");
		
		return "sml/runJob";
	}
	
	@RequestMapping("/schedule")
	public String runSchedule(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo
			,@RequestParam(name = "menuNo", required = false, defaultValue = "1") String menuNo
			,Model model) {
		
		List<Agency> agencyList = commonService.getAgencyAll();
		model.addAttribute("agencyList", agencyList);
		model.addAttribute("menuNo", "2");
		
		List<Map<String, String>> list = taskService.getAllTaskWithCollectorName();
		model.addAttribute("taskList", list);
		
		model.addAttribute("taskCnt", asyncConfig.getTaskCount() + scheduleService.getTaskCount());
		model.addAttribute("taskRsvCnt", scheduleService.getSchedulingTaskCount());
		model.addAttribute("taskRunCnt", scheduleService.getTaskCount());
		
		return "sml/runSchedule";
	}
	@RequestMapping("/log")
	public String viewLog(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo
			,@RequestParam(name = "menuNo", required = false, defaultValue = "1") String menuNo
			,Model model) {
		
		model.addAttribute("menuNo", "3");
		
		List<KLog> logList = new ArrayList<KLog>();
		logList = commonService.getAllLog();
		model.addAttribute("logList", logList);
		return "sml/viewLog";
	}
	
	
	
	@RequestMapping(value ="/collectorList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> collectorList(@RequestBody Map<String,String> params) {
		
		String agency = params.get("agencyNo");
		List<CollectorMapping> result = new ArrayList<CollectorMapping>();
//		if(agency != null && !agency.equals("")) {
//			result = collectorService.getCollectorListInAgency(Integer.parseInt(agency));
//		}else
		result = collectorService.getCollectorList();
			
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("taskCnt", asyncConfig.getTaskCount() + scheduleService.getTaskCount());
		
		return map;
	}
	
	@RequestMapping("/crawl")
	@ResponseBody
	public void getCrawl(@RequestBody Map<String,String> params) throws Exception {
		
		int pk = Integer.parseInt(params.get("pk"));
		
		//1. update Running status / send websocket message
		collectorService.updateStatus(pk, "R");
		
		CompletableFuture cf = crawlService.webCrawlThread(pk, params.get("startPage"), params.get("endPage"));
		statusHandler.sendTaskCnt(asyncConfig.getTaskCount()+scheduleService.getTaskCount());
		
		CompletableFuture<Void> after = cf.handle((res,ex) -> {
			statusHandler.sendTaskCnt(asyncConfig.getAfterTaskCount());
			return null;
		});
		
	}
	
	@RequestMapping(value="/schedule/save", method = RequestMethod.POST)
	public String saveSchdule(@ModelAttribute KTask task) throws Exception {
//		KTask task = new KTask();
//		List<KTask> list = taskService.getTaskByCollector(task.getCollector());
		Long count = taskService.getTaskByCollectorCount(task.getCollector());
		String taskNo = "C"+task.getCollector()+"-"+(count+1);
		task.setTaskNo(taskNo); // C8-1
		scheduleService.registerSchedule(task);
		taskService.saveTask(task);
		return "redirect:/simulator/schedule";
	}
	
	@RequestMapping(value="/schedule/delete", method = RequestMethod.POST)
	public String deleteSchdule(@ModelAttribute(name = "pk") String pk) throws Exception {
		KTask task = taskService.getTaskByPk(pk);
		scheduleService.stopSchedule(task);
		taskService.deleteTask(task);
		return "redirect:/simulator/schedule";
	}
}

