package com.booking.flights.controller;

import java.util.List;

import com.booking.flights.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.booking.flights.model.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/add-user")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }

	@GetMapping("/find-users")
	public List<User> findUser() {
	return userService.findAllUsers();
	}
	
	@PutMapping("/update-password")
	public String updatePassword(@RequestBody User user) {
		
	return userService.updatePassword(user);
	}

}
