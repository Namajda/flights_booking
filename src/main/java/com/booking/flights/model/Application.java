package com.booking.flights.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "application")
public class Application {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long applicationId;
	
	@ManyToOne
	@NotNull(message = "User must not be empty")
    @JoinColumn(name = "userId")
    private User user;
    
	@ManyToOne
	@NotNull(message = "Flight must not be empty")
	@JoinColumn(name = "flightId")
    private Flight flight ;
    
    private Integer status;
    
    private String note;

}
