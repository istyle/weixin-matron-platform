package com.geekunite.wx.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class WebStartApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebStartApplication.class, args);
	}
}
