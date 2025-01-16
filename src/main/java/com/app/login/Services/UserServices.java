package com.app.login.Services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.login.Model.User;
import com.app.login.Repository.UserRepository;

@Service
public class UserServices {

	private final UserRepository userRepository;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	public UserServices(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User adduser(User user, String CreatedBy) {
		User user1 = new User();
		user1.setName(user.getName());
		user1.setEmail(user.getEmail());
		user1.setCreated_by(CreatedBy);
		user1.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user1);
	}

	public void delteUserById(Long id) {
		userRepository.deleteById(id);

	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

	}

	public void updateUser(User user) {

		User existsUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("User Not Found"));
		existsUser.setName(user.getName());
		existsUser.setEmail(user.getEmail());
		userRepository.save(existsUser);
	}

}
