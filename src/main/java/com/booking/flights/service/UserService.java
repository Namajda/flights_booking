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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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

    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("User Created");
        return userRepository.save(user);
    }

    public List<User> findAllUsers(){
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
		 
		if(user.getUsername().equals(auth.getName())) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		 userRepository.save(user);
		 return "You updated the password";
		
		 }else {
			//throw new BusinessException(RestErrorCodes.ERRORE_VALIDAZIONE.getCode(),
			//		"You cannot update the password");
				log.error("You cannot update the password");
			}
		 return "You cannot update the password";
	}

//	public List<Flight> findUserFlights(User user) {
//		List<Flight> lista = flightRepository.findAllByUsers(user);
//		return lista;
//	}

	public List<Application> applyFlights(List<Long> flightIds) {
		List<Application> list=new ArrayList<>();
	
		for(Long i:flightIds) {
			Application application=new Application();
			application.setUser(userRepository.findByUsername(this.getLoggedInUser()));
			application.setFlight(flightRepository.findById(i).get());
			application.setStatus(0);
			application.setNote("Request");
			
			list.add(application);
		}
		
		return applicationRepository.saveAll(list);
	}
    
}
