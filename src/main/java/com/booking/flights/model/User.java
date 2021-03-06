package com.booking.flights.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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
    //@NotEmpty(message = "Username must not be empty")
    @Column(unique=true)
    private String username;
    
    
    private String firstName;
    
    
    private String lastName;

    
    @Email(message = "Not a well-formed email address")
    private String email;
    
    @NotEmpty(message = "Password must not be empty")
    private String password;

   
    @Enumerated(EnumType.STRING)
    private Roles role;
    
    @JsonIgnore
    @OneToMany(
	        mappedBy = "user",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private Set<Application> application;
      
}
