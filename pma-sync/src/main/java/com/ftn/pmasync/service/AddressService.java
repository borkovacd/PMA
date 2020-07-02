package com.ftn.pmasync.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.pmasync.model.Address;
import com.ftn.pmasync.repository.AddressRepository;

@Service
public class AddressService {
	@Autowired
	private AddressRepository addressRepository;

	public List<Address> getAddresses() {
		return addressRepository.findAll();
	}

}
