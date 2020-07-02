package com.ftn.pmasync.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.pmasync.model.User;
import com.ftn.pmasync.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	public User save(User u) {
		return userRepository.save(u);
	}
	
	public User findOneById(int id) {
		return userRepository.getOne(id);
	}

}
