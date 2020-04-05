package konantech.ai.aikwc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CommonService;
import konantech.ai.aikwc.service.impl.BasicCollectorServiceImpl;

@Controller
@RequestMapping("/manage")
public class ManageController {
	@Resource
	CommonService commonService;
	
	@Resource(name = "BasicCollectorService")
	BasicCollectorServiceImpl collectorService;
	
	
	@RequestMapping("")
	public String manageGroup(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo
			,@RequestParam(name = "menuNo", required = false, defaultValue = "1") String menuNo
			,@RequestParam(name = "menuNm", required = false, defaultValue = "Group") String menuNm
			,@RequestParam(name = "cpk", required = false) String cpk
			, Model model) {
		
		Map map = commonService.commInfo(agencyNo);
		Agency selAgency = (Agency) map.get("selAgency");
		model.addAttribute("selAgency", selAgency);
		model.addAttribute("agencyList", map.get("agencyList"));
		model.addAttribute("groupList", map.get("groupList"));
		model.addAttribute("agencyNo", selAgency.getPk());
		model.addAttribute("menuNo", menuNo);
		model.addAttribute("menuNm", menuNm);
		
		if(menuNo.equals("2")) {
			List<Site> siteList = collectorService.getSiteListInAgency(agencyNo);
			model.addAttribute("siteList", siteList);
		}else if(menuNo.equals("3")) {
			List<CollectorMapping> collectors = collectorService.getCollectorListInAgency(agencyNo);
			model.addAttribute("collectorList",collectors);
		}else if(menuNo.equals("4")) {
			// collector viewname으로 세팅해야함
			if(cpk == null) {
				model.addAttribute("detailPage", "clt/collectors/dummyDetail");
			}else {
				model.addAttribute("detailPage", "clt/collectors/basicDetail");
			}
			
		}
		
		return "clt/manage"+menuNm;
	}
	@RequestMapping(value ="/groupInAgency", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> groupInAgency(@RequestBody Group group) {
		
		List<Group> result = commonService.getGroupInAgency(group.getAgency());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		
		return map;
	}
	@RequestMapping(value ="/siteInGroup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> siteInGroup(@RequestBody Site site) {
		
		List<Site> result = collectorService.getSiteInGroup(site.getGrp());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		
		return map;
	}
	@RequestMapping(value ="/collectorInSite", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> collectorInSite(@RequestBody Collector collector) {
		
		List<Collector> result = collectorService.getCollectorListInSite(collector.getSite());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		
		return map;
	}
	
	@PostMapping("/collector/save")
	public String saveCollector(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo,
			@ModelAttribute Collector collector,
			Model model) {
		collectorService.saveCollector(collector);
		
		return "redirect:/manage";
	}
	
	@PostMapping("/site/save")
	public String saveSite(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo,
			@ModelAttribute Site site,
			Model model) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>SITE INSERT");
		collectorService.saveSite(site);
		
		return "redirect:/manage";
	}
	
	@PostMapping("/group/save")
	public String saveGroup(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo,
			@ModelAttribute Group group,
			Model model) {
		collectorService.saveGroup(group);
		return "redirect:/manage";
	}
	@PostMapping("/group/edit")
	public String editGroup(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo,
			@ModelAttribute Group group,
			Model model) {
		collectorService.updateGroup(group);
		
		return "redirect:/manage";
	}
	@PostMapping("/group/delete")
	public String deleteGroup(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo,
			@ModelAttribute Group group,
			Model model) {
		collectorService.deleteGroup(group);;
		return "redirect:/manage";
	}
	@RequestMapping(value ="/detail/json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> detailJsonTemplate(@RequestBody Map<String,String> params) throws Exception {
		
		Collector selectedCollector = collectorService.getCollector(Integer.parseInt(params.get("pk")));
		Class collectorClass = Class.forName(selectedCollector.getPackageClassName());
		String jsonstr = CommonUtil.jsonToString(collectorClass.newInstance());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", jsonstr);
		
		return map;
	}
	@RequestMapping(value ="/detail/info", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> siteInfo(@RequestBody Collector collector) {
		
		BasicCollector result = collectorService.getCollectorDetailInfo(collector.getPk());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		
		return map;
	}
	@PostMapping("/detail/save")
	public String saveCollectorDetail(@RequestParam(name = "agencyNo", required = false, defaultValue = "0") Integer agencyNo,
			@ModelAttribute BasicCollector collector,
			Model model) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>detail INSERT");
		collectorService.saveCollectorDetail(Integer.parseInt(collector.getPk()), collector);
		
		return "redirect:/manage";
	}
}
