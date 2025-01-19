package com.app.login.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.app.login.Services.CustomOAuth2UserService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/assets/**", "/oauth2/**").permitAll() // Allow
																											// static
																											// assets
																											// and
																											// OAuth2
																											// endpoints
						.anyRequest().authenticated() // Protect all other endpoints
				).formLogin(form -> form.loginPage("/login") // Your custom login page
						.defaultSuccessUrl("/", true) // Redirect after successful form login
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout") // URL to trigger logout
						.logoutSuccessUrl("/login?logout") // Redirect to login page after logout
						.invalidateHttpSession(true) // Invalidate session
						.deleteCookies("JSESSIONID") // Clear cookies
						.permitAll())
				.oauth2Login(oauth2 -> oauth2.loginPage("/login") // Reuse the custom login page for OAuth2
						.defaultSuccessUrl("/", true) // Redirect after successful OAuth2 login
						.failureUrl("/login?error=true") // Redirect to login page on failure
						.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)))
				.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

}
