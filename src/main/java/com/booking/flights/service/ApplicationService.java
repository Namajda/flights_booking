package com.booking.flights.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.booking.flights.model.Application;
import com.booking.flights.model.Flight;
import com.booking.flights.model.Roles;
import com.booking.flights.model.User;
import com.booking.flights.repository.ApplicationRepository;
import com.booking.flights.repository.FlightRepository;
import com.booking.flights.repository.UserRepository;

@Service
public class ApplicationService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private FlightRepository flightRepository;

	public Application approveRejectFlight(Long applicationId, Integer stato, String note) throws NotFoundException {
		Application application = applicationRepository.findById(applicationId).orElseThrow(()-> new NotFoundException());
		// Optional<Flight> flight=
		// flightRepository.findById(application.get().getFlight().getFlightId());
		if (stato.equals(1) && application.getStatus().equals(1))
			log.info("The application is already validated");
		else if (stato.equals(-1) && application.getStatus().equals(-1))
			log.info("The application is already rejected");
		else if (stato.equals(0) && application.getStatus().equals(0))
			log.info("The application is already waiting");

		else if (stato.equals(-1) && !note.isEmpty()) {
			application.setStatus(stato);
				log.error("Insert a note for rejecting the application");
				application.setNote(note);
				applicationRepository.save(application);
			
		} else if (stato.equals(1)) {
			application.setStatus(stato);
			application.setNote(note);

			applicationRepository.save(application);

			// flightRepository.save(flight.get());

		}

		return application;
	}

	public Page<Application> findAllApplication(Pageable pageable) {

		User loggedUser = userRepository.findByUsername(userService.getLoggedInUser());
		if (loggedUser != null) {
			if (loggedUser.getRole().equals(Roles.ROLE_SUPERVISOR)) {
				return applicationRepository.findAll(pageable);
			} else
				return applicationRepository.findAllByUser(loggedUser, pageable);
		} else
			return null;

	}

	//supervisor is also a user
	
	public Application bookFlight(Application application)  {
		
		Optional<User> user = userRepository.findById(application.getUser().getUserId());
		
		Optional<Flight> flight =  Optional.ofNullable(flightRepository.findById(application.getFlight().getFlightId()).get());
		if(user.isPresent())	
		applicationRepository.save(application);
		return application;
	}

}
