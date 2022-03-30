package com.booking.flights.config;

import com.booking.flights.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userService;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/user/add-user").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER","SUPERVISOR")
                .antMatchers("/admin/**").hasAnyRole("SUPERVISOR")
                .antMatchers("/flight/create").hasRole("SUPERVISOR")
                .antMatchers("/flight/**").hasAnyRole("SUPERVISOR", "USER")         
                .and().logout()
                .logoutSuccessUrl("/login")
                .and().httpBasic()
                .and().csrf().disable();
    }

//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//
//                .authorizeRequests().antMatchers("/admin/**").hasRole("SUPERVISOR")
//                .and().authorizeRequests().antMatchers("/user/**").hasRole("USER")
//                .and().authorizeRequests().antMatchers("/registration").permitAll()
//                .antMatchers("/").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login?logout")
//                .permitAll().and().headers().frameOptions().disable()
//                .and().csrf().disable();
//        ;
//    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
