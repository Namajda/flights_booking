package com.booking.flights.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.flights.dto.RemainingFlightsDto;
import com.booking.flights.model.Application;
import com.booking.flights.model.Flight;
import com.booking.flights.model.User;
import com.booking.flights.service.ApplicationService;
import com.booking.flights.service.FlightService;

@RestController
@RequestMapping("/application")
public class ApplicationController {
	
	@Autowired
    private ApplicationService applicationService;
	
	
	@PostMapping("/apply-flight")
    public List<Application> applyFlights(@RequestParam(name="flightIds", required = true) List<Long> flightIds) {
        return applicationService.applyFlights(flightIds);
    }
	
	
	@GetMapping("/all-user-application")
    public Page<Application> getAllFlights(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size
                                      ){
        Pageable pageable = PageRequest.of(page, size);
        return applicationService.findAllApplication(pageable);
    }
	
	@GetMapping("/remaining-flights")
	public RemainingFlightsDto findRemainingFlights(@RequestParam(name="year", required = true) Integer year) {
	return applicationService.findRemainingFlights(year);
	}
	
	@PostMapping("/supervisor-book-flight")
    public Application bookFlight(@RequestBody Application application) {
        return applicationService.bookFlight(application);
    }
	
	@PostMapping("/create-flight-request")
    public Application createFlightRequest(@RequestBody Application application) {
        return applicationService.createFlightRequest(application);
    }
	
	
	@DeleteMapping("/cancel-flight-request/{applicationId}")
    public void createFlightRequest(@PathVariable(value="applicationId") Long applicationId) {
       applicationService.cancelFlightRequest(applicationId);
    }
	
	

}
