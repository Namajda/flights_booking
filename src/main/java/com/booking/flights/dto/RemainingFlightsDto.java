package com.booking.flights.dto;

import java.io.Serializable;
import java.util.List;
import com.booking.flights.model.Flight;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class RemainingFlightsDto implements Serializable {
	
	protected Integer remainingFlights;
	
	protected List<Flight> flights;

}
