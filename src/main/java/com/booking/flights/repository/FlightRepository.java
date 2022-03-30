package com.booking.flights.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.booking.flights.model.Flight;
import com.booking.flights.model.User;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long>{
	
	
	@Query(value = "select * from flights.flight f where year(f.departure_time)=?1 and flight_id in(?2)", nativeQuery = true 	)
	List<Flight> findFlightByYearAndId(Integer year, List<Integer> id);

	@Query(value = "select * from flights.flight f where f.status=1", nativeQuery = true 	)
	List<Flight> findFlightByStatus();
	
	@Query(value = "select * from flights.flight f where  flight_id in(?1)", nativeQuery = true 	)
	List<Flight> findFlightById(List<Integer> id);
	
//	Page<Flight> findAllByUsersContains(User user, Pageable pageable);
	
}
