package com.app.login.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        return http
	        		.csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(auth -> auth
	                        .requestMatchers("/assets/**").permitAll()
	                        .anyRequest().authenticated()
	                )
	                .formLogin(form -> form
	                        .loginPage("/login")
	                        .defaultSuccessUrl("/", true)
	                        .permitAll()
	                )
	                .logout(logout -> logout
	                        .logoutUrl("/logout") // URL to trigger logout
	                        .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
	                        .invalidateHttpSession(true) // Invalidate session
	                        .deleteCookies("JSESSIONID") // Clear cookies
	                        .permitAll() // Allow everyone to access logout
	                )
	                .build();
	    }
	 
	 

}
