package com.booking.flights.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.booking.flights.dto.RemainingFlightsDto;
import com.booking.flights.model.Application;
import com.booking.flights.model.Flight;
import com.booking.flights.model.Roles;
import com.booking.flights.model.User;
import com.booking.flights.repository.ApplicationRepository;
import com.booking.flights.repository.FlightRepository;
import com.booking.flights.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class FlightService {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FlightRepository flightRepository;

	public RemainingFlightsDto findRemainingFlights(Integer year) {
		String username = userService.getLoggedInUser();
		User u = userRepository.findByUsername(username);
		Long userId = u.getUserId();
		// here is the list of application of user
		List<Application> lista = applicationRepository.findByUserId(userId);
		List<Integer> i = new ArrayList<>();

		for (Application a : lista) {
			i.add(a.getFlight().getFlightId().intValue());
		}

		List<Flight> flights = flightRepository.findFlightByYearAndId(year, i);

		RemainingFlightsDto remaining = new RemainingFlightsDto();
		remaining.setRemainingFlights(20 - flights.size());
		remaining.setFlights(flights);

		return remaining;
	}

	public List<Flight> findBookedFlights() {
		Long id= userRepository.findByUsername(userService.getLoggedInUser()).getUserId();
		
		List<Application> lista = applicationRepository.findByUserIdAndStatus(id);
		List<Integer> i = new ArrayList<>();

		for (Application a : lista) {
			i.add(a.getFlight().getFlightId().intValue());
		}
		
		List<Flight> flights = flightRepository.findFlightById(i);
		return flights;
	}

		
	public Page<Flight> getAllFlights(Pageable pageable){
        User loggedUser = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (loggedUser!=null){
            if (loggedUser.getRole().equals(Roles.ROLE_SUPERVISOR)){
                return flightRepository.findAll(pageable);
            } else return null;//flightRepository.findAllByUsersContains(loggedUser, pageable);
        } else return null;
    }

    public Flight createFlight(Flight flight){
       
        return flightRepository.save(flight);
    }

}
