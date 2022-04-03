package com.booking.flights.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.booking.flights.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	User findByUsername(String username);

	@Query(value = "select * from flights.user u where (u.username=?1 or u.email=?2)", nativeQuery = true )
	List<User> findByUsernameOrEmail(String username , String email);
	
	}

