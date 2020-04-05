package konantech.ai.aikwc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/collector")
public class BasicCollectorController {
	@RequestMapping("/basic")
	public String detail() {
		return "clt/collectors/basicDetail";
	}
}
