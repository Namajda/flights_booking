package com.booking.flights.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.booking.flights.dto.RemainingFlightsDto;
import com.booking.flights.model.Application;
import com.booking.flights.model.ClassType;
import com.booking.flights.model.Flight;
import com.booking.flights.service.ApplicationService;

@RestController
@RequestMapping("/application")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	@PostMapping("/apply-one-flight")
	public Application applyFlight(@RequestParam(name = "flightId", required = true) Long flightId,
			@RequestParam(name = "classType", required = true) ClassType classType) {
		return applicationService.applyFlight(flightId, classType);
	}

	@GetMapping("/all-user-application")
	public Page<Application> getAllFlights(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return applicationService.findAllApplication(pageable);
	}

	@GetMapping("/remaining-flights")
	public RemainingFlightsDto findRemainingFlights(@RequestParam(name = "year", required = true) Integer year) {
		return applicationService.findRemainingFlights(year);
	}

	@GetMapping("/booked-flights")
	public List<Flight> findBookedFlights() {

		return applicationService.findBookedFlights();
	}

	@PutMapping("/approve-reject-flight")
	public String approveRejectFlight(@RequestParam(name = "applicationId", required = true) Long applicationId,
			@RequestParam(name = "status", required = true) Integer status,
			@RequestParam(name = "note", required = false) String note) throws NotFoundException {
		return applicationService.approveRejectFlight(applicationId, status, note);
	}

	@PostMapping("/supervisor-book-flight")
	public Application bookFlight(@RequestBody Application application) {
		return applicationService.bookFlight(application);
	}

	@PostMapping("/add-flight-request")
	public Application addFlightRequest(@RequestBody Application application) {
		return applicationService.addFlightRequest(application);
	}

	@DeleteMapping("/cancel-flight-request/{applicationId}")
	public void createFlightRequest(@PathVariable(value = "applicationId") Long applicationId) {
		applicationService.cancelFlightRequest(applicationId);
	}

}
