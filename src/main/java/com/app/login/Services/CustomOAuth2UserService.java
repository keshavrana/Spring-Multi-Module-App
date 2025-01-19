package com.app.login.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
		
		User userAlreadyExists = userRepo.findByName(name);
		if (userAlreadyExists == null) {
			User user = new User();
			user.setEmail(email);
			user.setName(name);
			user.setPassword(name);
			user.setRole("USER");
			user.setCreated_by(name);
			userServices.adduser(user, "GOOGLE");
		}
		
		System.out.println("Email =" + email);
		System.out.println("Name =" + name);
		// Optionally save user data to your database
		// ...

		return oAuth2User;
	}
}
