package com.booking.flights.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.booking.flights.dto.FlightsDto;
import com.booking.flights.model.Application;
import com.booking.flights.model.Flight;
import com.booking.flights.model.Roles;
import com.booking.flights.model.User;
import com.booking.flights.repository.FlightRepository;
import com.booking.flights.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class FlightService {
	private static final Logger log = LoggerFactory.getLogger(FlightService.class);
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FlightRepository flightRepository;

	public Page<Flight> getAllFlights(Pageable pageable) {
		User loggedUser = userRepository
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (loggedUser != null) {
			if (loggedUser.getRole().equals(Roles.ROLE_SUPERVISOR)) {
				return flightRepository.findAll(pageable);
			} else
				return null;// flightRepository.findAllByUsersContains(loggedUser, pageable);
		} else
			return null;
	}

	public List<FlightsDto> filterFlights(String search) {
		List<FlightsDto> flights = flightRepository.filterAllFlights(search);
		return flights;
	}

	public Page<FlightsDto> filterFlightsPage(Pageable pageable, String search) {
		Page<FlightsDto> flights = flightRepository.filterAllFlightsPage(search, pageable);
		return flights;
	}

	public Flight addFlight(Flight flight) {
		Set<Application> applicationSet = new HashSet<>();
		List<Flight> excistingFlight = flightRepository.findExcistingFlight(flight.getDeparture(),
				flight.getDestination(), flight.getDepartureTime());
		if (flight.getDepartureTime().before(new Date()))
			log.error("You can not add a flight in the past");
		else if (!excistingFlight.isEmpty())
			log.error("This flight already excist in db");
		else if (flight.getDeparture().equals(flight.getDestination()))
			log.error("Flight departure and destination can not be the same");
		else {
			flight.setApplication(applicationSet);
			flightRepository.save(flight);
		}
		return null;
	}

}
