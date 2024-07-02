package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionsController {

	@GetMapping("/error")
	public String error() {
		return "error";
	}
	
}
