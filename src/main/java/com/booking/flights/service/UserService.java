package com.booking.flights.service;

import com.booking.flights.model.Application;
import com.booking.flights.model.User;
import com.booking.flights.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User createUser(User user) {
		
		List<User> alreadyExcistingUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
		if(!alreadyExcistingUser.isEmpty()) {
			log.error("This username or email already excist. Please add another username or email for a new user.");
		return null;
		}
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
		// this is because of a hibernate exception
		Set<Application> applicationSet = new HashSet<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (List.copyOf(auth.getAuthorities()).get(0).toString().equals("ROLE_SUPERVISOR") || user.getUsername().equals(auth.getName())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setApplication(applicationSet);
			userRepository.save(user);
			return "You updated the password";

		} else {
			// throw new BusinessException(RestErrorCodes.ERRORE_VALIDAZIONE.getCode(),
			// "You cannot update the password");
			log.error("You cannot update the password");
		}
		return "You cannot update the password";
	}

	public User updateUser(User user) {
		
		Optional<User> savedUser = userRepository.findById(user.getUserId());
		Set<Application> applicationSet = new HashSet<>();
		if (!(savedUser.get().getUsername().equals(user.getUsername())) || !(savedUser.get().getEmail().equals(user.getEmail())) ) {
			// check if the username that we want to set is in any other row in db
			List<User> userCheck = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
			if (!userCheck.isEmpty()) {
				log.error("This username or email is used before.");
			} else {
				user.setPassword(user.getPassword());
				userRepository.save(user);
			}
		} else {
			user.setPassword(user.getPassword());
			user.setApplication(applicationSet);
			userRepository.save(user);
		}
		return user;
	}

	public User deleteUser(Long id) throws NotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException());
		userRepository.delete(user);
		return null;
	}

}
