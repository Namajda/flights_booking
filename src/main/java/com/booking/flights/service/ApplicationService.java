package com.booking.flights.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.booking.flights.dto.RemainingFlightsDto;
import com.booking.flights.model.Application;
import com.booking.flights.model.ClassType;
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

	public Application applyFlight(Long flightId, ClassType classType) {
		Application application = new Application();
		application.setUser(userRepository.findByUsername(userService.getLoggedInUser()));

		if (flightRepository.findById(flightId).isPresent()) {
			application.setFlight(flightRepository.findById(flightId).get());
		} else if (flightRepository.findById(flightId).isPresent()
				&& flightRepository.findById(flightId).get().getDepartureTime().before(new Date())) {
			log.error("You can not add an application for a flight in the past");
			return null;
		} else {
			log.error("There does not excist a flight with this id:" + flightId);
			return null;
		}

		application.setClassType(classType);
		application.setStatus(0);
		application.setNote("Request");

		List<Application> alreadyExcistingApplication = applicationRepository
				.findAlreadyExcistingApplication(application.getUser().getUserId(), flightId);
		if (!alreadyExcistingApplication.isEmpty()) {
			log.error("There does already excist an application for this flight: "
					+ application.getFlight().getDeparture() + " - " + application.getFlight().getDestination());
			return null;
		}
		return applicationRepository.save(application);
	}

	public RemainingFlightsDto findRemainingFlights(Integer year) {
		String username = userService.getLoggedInUser();
		User u = userRepository.findByUsername(username);
		Long userId = u.getUserId();
		// here is the list of application of user, all accepted flight requests
		List<Application> lista = applicationRepository.findByUserIdAndStatus(userId);

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
		Long id = userRepository.findByUsername(userService.getLoggedInUser()).getUserId();

		List<Application> lista = applicationRepository.findByUserIdAndStatus(id);
		List<Integer> i = new ArrayList<>();

		for (Application a : lista) {
			i.add(a.getFlight().getFlightId().intValue());
		}
		List<Flight> flights = flightRepository.findFlightById(i);
		return flights;
	}

	public String approveRejectFlight(Long applicationId, Integer status, String note) throws NotFoundException {
		Application application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException());

		Optional<Flight> flight = flightRepository.findById(application.getFlight().getFlightId());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(flight.get().getDepartureTime());
		Integer year = calendar.get(Calendar.YEAR);

		if (status.equals(1) && application.getStatus().equals(1)) {
			log.info("The application is already validated");
			return "The application is already validated";
		} else if (status.equals(-1) && application.getStatus().equals(-1)) {
			log.info("The application is already rejected");
			return "The application is already rejected";
		} else if (status.equals(0) && application.getStatus().equals(0)) {
			log.info("The application is already waiting");
			return "The application is already waiting";
		} else if (status.equals(-1)) {
			application.setStatus(status);
			if (note.isEmpty()) {
				log.error("Insert a note for rejecting the application");
				return "Insert a note for rejecting the application";
			}
			application.setNote(note);
			applicationRepository.save(application);
			return "Application rejected";

		} else if (status.equals(1)) {
			application.setStatus(status);
			application.setNote(note);
			if (this.findRemainingFlights(year).getRemainingFlights() < 21) {
				applicationRepository.save(application);
				return "Application approved successfully";
			} else {
				return "Application is not approved because the number of exceeded fligts for year";
			}
		}

		return null;
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

	public Application bookFlight(Application application) {

		Optional<User> user = userRepository.findById(application.getUser().getUserId());

		Optional<Flight> flight = flightRepository.findById(application.getFlight().getFlightId());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(flight.get().getDepartureTime());
		Integer year = calendar.get(Calendar.YEAR);

		List<Application> alreadyExcistingApplication = applicationRepository
				.findBookedApplication(application.getUser().getUserId(), application.getFlight().getFlightId());
		if (!alreadyExcistingApplication.isEmpty()) {
			log.error("There does already excist an application for this flight: "
					+ application.getFlight().getDeparture() + " - " + application.getFlight().getDestination());

			return null;
		} else if (user.isPresent() && flight.isPresent()
				&& this.findRemainingFlights(year).getRemainingFlights() < 21) {
			applicationRepository.save(application);
			log.info("Flight booked successfully");
		} else
			log.info("Flight can not be booked");

		return application;
	}

	public Application addFlightRequest(Application application) {
		User loggedUser = null;
		Optional<User> user = userRepository.findById(application.getUser().getUserId());
		Optional<Flight> flight = flightRepository.findById(application.getFlight().getFlightId());
		List<Application> findExcistingApplication;

		if (user.isPresent() && flight.isPresent()) {
			loggedUser = userRepository.findByUsername(userService.getLoggedInUser());
			if (loggedUser.getUserId().equals(application.getUser().getUserId())) {
				findExcistingApplication = applicationRepository.findExcistingApplication(
						application.getUser().getUserId(), application.getFlight().getFlightId());
				if (!findExcistingApplication.isEmpty()) {
					log.error("Flight request already excist");

				} else {
					applicationRepository.save(application);
					log.info("Flight request created successfully");
				}
			} else
				log.error("Flight request is not valid");
		} else
			log.error("Flight request is not valid");

		return application;
	}

	public void cancelFlightRequest(Long applicationId) {
		Optional<Application> application = applicationRepository.findById(applicationId);
		User loggedUser = null;
		if (application.isPresent()) {
			loggedUser = userRepository.findByUsername(userService.getLoggedInUser());
			if (loggedUser.getUserId().equals(application.get().getUser().getUserId())) {
				applicationRepository.deleteById(applicationId);
				log.info("Application deleted successfully");
			} else
				log.error("You can not delete this application");
		} else
			log.error("This application does not excist");

	}

}
