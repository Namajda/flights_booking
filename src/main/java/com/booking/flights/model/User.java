package com.booking.flights.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Length(min = 3, message = "Username must contain at least 3 characters")
    @Column(unique=true)
    private String username;
    
    private String firstName;
    
    private String lastName;

    @Email(message = "Not a well-formed email address")
    private String email;
    
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
    
    //this create an unfinitive cycle
    
    @JsonIgnore
    @OneToMany(
	        mappedBy = "user",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private Set<Application> application;



	
    
  
}
