package com.booking.flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.flights.security.UserDetailsServiceImpl;

@RestController
public class MainController {
	
	@Autowired
	UserDetailsServiceImpl userService;

	@PostMapping("/login")
	public String login(@RequestParam() String user, @RequestParam() String pass) {
	   UsernamePasswordAuthenticationToken authReq
	     = new UsernamePasswordAuthenticationToken(user, pass);
	   return authReq.getName();
	}


}
