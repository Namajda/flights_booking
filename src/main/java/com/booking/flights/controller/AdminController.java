package com.booking.flights.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	

//i have taken all the cases that a supervisor is also a user, so 
// a user can add, update, delete a supervisor
// if not i have to check the role of the user and see if it is a "USER",
//than modify, if not i continue without making any modifications
	
	@PostMapping("/supervisor-add-user")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }
	
	@PutMapping("/supervisor-update-user")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
	
	@DeleteMapping("/supervisor-delete-user/{id}")
    public User deleteUser(@PathVariable(value="id") Long id) throws NotFoundException {
        return userService.deleteUser(id);
    }
	
}
