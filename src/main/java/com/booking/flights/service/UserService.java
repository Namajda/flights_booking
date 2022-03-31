package com.booking.flights.service;

import com.booking.flights.exceptions.BusinessException;
import com.booking.flights.model.Application;
import com.booking.flights.model.Flight;
import com.booking.flights.model.User;
import com.booking.flights.repository.ApplicationRepository;
import com.booking.flights.repository.FlightRepository;
import com.booking.flights.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.booking.flights.exceptions.RestErrorCodes;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// @Qualifier("chronosEntityManagerFactory")
	@Autowired
	private EntityManager em;

	public User createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		log.info("User Created");
		return userRepository.save(user);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public String getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return name;
	}

	public String updatePassword(User user) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.getName());

		if (user.getUsername().equals(auth.getName())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return "You updated the password";

		} else {
			// throw new BusinessException(RestErrorCodes.ERRORE_VALIDAZIONE.getCode(),
			// "You cannot update the password");
			log.error("You cannot update the password");
		}
		return "You cannot update the password";
	}

//	public List<Flight> findUserFlights(User user) {
//		List<Flight> lista = flightRepository.findAllByUsers(user);
//		return lista;
//	}

	public List<Application> applyFlights(List<Long> flightIds) {
		List<Application> list = new ArrayList<>();

		for (Long i : flightIds) {
			Application application = new Application();
			application.setUser(userRepository.findByUsername(this.getLoggedInUser()));
			application.setFlight(flightRepository.findById(i).get());
			application.setStatus(0);
			application.setNote("Request");

			list.add(application);
		}

		return applicationRepository.saveAll(list);
	}

	public User updateUser(User user) {
		// by id get the saved user in db//check if this id is Present
		Optional<User> savedUser = userRepository.findById(user.getUserId());
		if (!(savedUser.get().getUsername().equals(user.getUsername()))) {
			// check if the username that we want to set is in any other row in db
			User userCheck = userRepository.findByUsername(user.getUsername());
			if (userCheck != null) {
				log.error("This username is used before.");

			} else {
				user.setPassword(passwordEncoder.encode(user.getPassword()));//do not change pass
				userRepository.save(user);
			}
		} else {

			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
		}

		return user;

	}

	public User deleteUser(Long id) throws NotFoundException {
		User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException());
		
		List<Application> applicationList= applicationRepository.findByUserId(user.getUserId());
		if(!applicationList.isEmpty()) {
			for(Application a:applicationList) {
				applicationRepository.delete(a);
		}
		}
		userRepository.delete(user);
		
		
		return null;
	}

}
