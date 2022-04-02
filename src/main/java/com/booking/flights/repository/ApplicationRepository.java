package com.booking.flights.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.booking.flights.model.Application;
import com.booking.flights.model.User;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long>{

	
	@Query(value = "SELECT * FROM flights.application f where f.user_id=?", nativeQuery = true 	)
	List<Application> findByUserId(Long userId);
	
	@Query(value = "SELECT * FROM flights.application f where f.user_id=?1 and f.status<>-1", nativeQuery = true 	)
	List<Application> findByUserIdAndStatusActiveApplications(Long id);
	
	@Query(value = "SELECT * FROM flights.application f where f.user_id=?1 and f.flight_id=?2 and f.status<>-1", nativeQuery = true 	)
	List<Application> findAlreadyExcistingApplication(Long userId, Long flightId);
	
	@Query(value = "SELECT * FROM flights.application f where f.user_id=?1 and f.status=1", nativeQuery = true 	)
	List<Application> findByUserIdAndStatus(Long id);
	
	Page<Application> findAllByUser(User user, Pageable pageable);
	
	@Query(value = "SELECT * FROM flights.application f where f.user_id=?1 and f.flight_id=?2 and f.status<>-1", nativeQuery = true 	)
	List<Application> findExcistingApplication(Long userId, Long flightId);
	
	@Query(value = "SELECT * FROM flights.application f where f.user_id=?1 and f.flight_id=?2 and f.status=1", nativeQuery = true 	)
	List<Application> findBookedApplication(Long userId, Long flightId);

}
