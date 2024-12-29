package com.app.login.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.login.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
