package com.booking.flights.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.flights.model.Application;
import com.booking.flights.model.User;
import com.booking.flights.service.ApplicationService;
import com.booking.flights.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {


	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationService applicationService;
	
	
	@GetMapping("/find-users")
	public List<User> findUser() {
	return userService.findAllUsers();
	}
	
	
	@PutMapping("/approve-reject-flight")
	public Application approveRejectFlight(@RequestParam(name="applicationId", required = true) Long applicationId,
			@RequestParam(name="stato", required = true) Integer stato,
			@RequestParam(name="note", required = false) String note)  throws NotFoundException {
	return applicationService.approveRejectFlight(applicationId, stato, note);
	}

	//test3333
//testtttt
}
