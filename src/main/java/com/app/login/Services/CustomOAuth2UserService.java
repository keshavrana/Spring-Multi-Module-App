package com.app.login.Services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.app.login.Model.CustomOAuth2User;
import com.app.login.Model.User;
import com.app.login.Repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	UserServices userServices;
	@Autowired
	UserRepository userRepo;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// Extract user information (e.g., email, name)
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");

		User user = null;
		if (email != null) {
			user = userRepo.findByEmail(email);
		} else {
			user = userRepo.findByName(name);
		}
		if (user == null) {
			user = new User();
			if (email != null) {
				user.setEmail(email);
			} else {
				user.setEmail("github@gmail.com");
			}
			user.setName(name);
			user.setPassword(name);
			user.setRole("USER");
			user.setCreated_by(name);
			userServices.adduser(user, "GOOGLE");
		}
		// Assign roles as authorities
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
		return new CustomOAuth2User(oAuth2User, Collections.singletonList(authority));
		// return oAuth2User;
	}
}
