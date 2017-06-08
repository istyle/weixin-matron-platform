package com.geekunite.wx.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

	@RequestMapping("/index")
	public String home() {
		System.out.println("1111");
		return "index";
	}
	
	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		System.out.println("1111");
		return "index";
	}
}
