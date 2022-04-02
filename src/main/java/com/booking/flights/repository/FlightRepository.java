package com.booking.flights.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.flights.dto.FlightsDto;
import com.booking.flights.model.ClassType;
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
	
//page
	@Query(value = "SELECT f.flight_id as flightId, f.departure as departure, f.destination as destination, \r\n"
			+ " f.departure_time as departureTime, f.class_type as classType \r\n"
			+ " from flights.flight f\r\n"
			+ " where \r\n"
			+ " (f.flight_id LIKE CONCAT('%',:search,'%') \r\n"
			+ " OR f.departure LIKE CONCAT('%',ifnull(:search,''),'%')\r\n"
			+ " OR f.destination LIKE CONCAT('%',ifnull(:search,''),'%') \r\n"
			+ " OR f.departure_time LIKE CONCAT('%',ifnull(:search,''),'%') \r\n"
			+ " OR f.class_type LIKE CONCAT('%',ifnull(:search,''),'%'));", nativeQuery = true 	)
	List<FlightsDto> filterAllFlights(@Param("search") String search);
	
//	@Query(value = "select * from flights.flight f where f.departure=?1 and f.destination=?2 and f.", nativeQuery = true 	)
//	List<Flight> findAlreadyExcistingFlight(String departure, String destination, Date departureTime,
//			ClassType classType);
	
}
