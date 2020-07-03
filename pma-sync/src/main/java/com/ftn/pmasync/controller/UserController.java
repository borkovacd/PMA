package com.ftn.pmasync.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.pmasync.dto.UserDTO;
import com.ftn.pmasync.model.User;
import com.ftn.pmasync.repository.UserRepository;
import com.ftn.pmasync.service.UserService;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository ;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<UserDTO>> getUsers() {
		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		List<User> users = userService.getUsers();
		for(User user : users) {
			ModelMapper modelMapper = new ModelMapper();
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);
			usersDTO.add(userDTO);
		}
		return new ResponseEntity<List<UserDTO>> (usersDTO, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/syncUsers", method = RequestMethod.POST)
	public ResponseEntity<List<User>> syncActivity(@RequestBody List<User> users) {
		boolean tmp = false;
		for(User user : users) {
			user.setSync(true);
			User result = userRepository.save(user);

			if(result == null)
				tmp = true;
		}
		if(tmp == false) // ako je uspeo da ga sacuva
			return new ResponseEntity<>(HttpStatus.OK);
		else // ako nije uspeo da ga sacuva
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
