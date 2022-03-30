package com.booking.flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.flights.model.Application;
import com.booking.flights.model.Flight;
import com.booking.flights.service.ApplicationService;
import com.booking.flights.service.FlightService;

@RestController
@RequestMapping("/application")
public class ApplicationController {
	
	@Autowired
    private ApplicationService applicationService;
	
	@GetMapping("/all-user-application")
    public Page<Application> getAllFlights(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size
                                      ){
        Pageable pageable = PageRequest.of(page, size);
        return applicationService.findAllApplication(pageable);
    }
	

}
