package konantech.ai.aikwc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.service.CommonService;

@Controller
public class AdminController {
	
	@Autowired
	CommonService commonService;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/main")
	public String main() {
		return "admin/main";
	}
	@RequestMapping("/login")
	public String login() {
		return "admin/login";
	}
	
	@RequestMapping(value ="/comm/logReadAll", method = RequestMethod.POST)
	@ResponseBody
	public void logReadAll() {
		commonService.readAllLog();
	}
	
}
