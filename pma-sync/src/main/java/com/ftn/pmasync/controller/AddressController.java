package com.ftn.pmasync.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.pmasync.dto.AddressDTO;
import com.ftn.pmasync.dto.ExConvictDTO;
import com.ftn.pmasync.model.Address;
import com.ftn.pmasync.model.ExConvict;
import com.ftn.pmasync.service.AddressService;
import com.ftn.pmasync.service.ExConvictService;

@RestController
@RequestMapping(path = "/api/addresses")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<AddressDTO>> getAddresses() {
		List<AddressDTO> addressesDTO = new ArrayList<AddressDTO>();
		List<Address> addresses = addressService.getAddresses();
		for(Address address : addresses) {
			ModelMapper modelMapper = new ModelMapper();
			AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
			addressesDTO.add(addressDTO);
		}
		return new ResponseEntity<List<AddressDTO>> (addressesDTO, HttpStatus.OK);		
	}
	

}
