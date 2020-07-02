package com.ftn.pmasync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.pmasync.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	
	//User findOneById(int id);

}
