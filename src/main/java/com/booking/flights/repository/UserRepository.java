package com.booking.flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.booking.flights.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	User findByUsername(String username);
	}

