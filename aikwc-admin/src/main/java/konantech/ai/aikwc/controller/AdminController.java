package konantech.ai.aikwc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	
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
	
}
