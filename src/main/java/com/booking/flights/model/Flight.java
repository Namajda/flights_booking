 package com.booking.flights.model;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flight")
public class Flight {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long flightId;
	
	@NotEmpty(message = "Departure field must not be empty")
    private String departure;
    
	@NotEmpty(message = "Destination field must not be empty")
    private String destination;
    
	@Future
	@NotNull(message = "Departure time field must not be empty")
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Rome")
    private Date departureTime;

	
	
	
	@JsonIgnore
    @OneToMany(
	        mappedBy = "flight",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private Set<Application> application;

	
}
