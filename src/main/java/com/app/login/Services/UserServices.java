package com.app.login.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.login.Model.User;
import com.app.login.Repository.UserRepository;

@Service
public class UserServices {

	private final UserRepository userRepository;

	public UserServices(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public User adduser(User user) {
		return userRepository.save(user);
	}
	
	public void delteUserById(Long id) {
		userRepository.deleteById(id);
	
	}
	

}
