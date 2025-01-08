package com.app.login.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.login.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByName(String username);


}
