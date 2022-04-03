package com.booking.flights.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.booking.flights.dto.FlightsDto;
import com.booking.flights.model.Flight;
import com.booking.flights.service.FlightService;

@RestController
@RequestMapping("/flight")
public class FlightController {

	@Autowired
	private FlightService flightService;

	@PostMapping("/add-flight")
	public Flight addFlight(@RequestBody Flight flight) {
		return flightService.addFlight(flight);
	}

	@GetMapping("/all")
	public Page<Flight> getAllFlights(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "departureTime") String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return flightService.getAllFlights(pageable);
	}

	@GetMapping("/filter")
	public List<FlightsDto> filterFlights(@RequestParam(name = "search", required = false) String search) {

		return flightService.filterFlights(search);
	}

	@GetMapping("/filter-page")
	public Page<FlightsDto> filterFlightsPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "departureTime") String sortBy,
			@RequestParam(name = "search", required = false) String search) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return flightService.filterFlightsPage(pageable, search);
	}

}
