package com.mgmtp.vn.techevent.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class AppController {

	@RequestMapping(value = "/*")
	public String all() {
		return "index";
	}

	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}

}
