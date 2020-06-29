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

import com.ftn.pmasync.dto.ExConvictDTO;
import com.ftn.pmasync.model.ExConvict;
import com.ftn.pmasync.service.ExConvictService;


@RestController
@RequestMapping(path = "/api/exConvicts")
public class ExConvictController {
	
	@Autowired
	private ExConvictService exConvictService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ExConvictDTO>> getExConvicts() {
		List<ExConvictDTO> exConvictsDTO = new ArrayList<ExConvictDTO>();
		List<ExConvict> exConvicts = exConvictService.getExConvicts();
		for(ExConvict exConvict : exConvicts) {
			ModelMapper modelMapper = new ModelMapper();
			ExConvictDTO exConvictDTO = modelMapper.map(exConvict, ExConvictDTO.class);
			exConvictsDTO.add(exConvictDTO);
		}
		return new ResponseEntity<List<ExConvictDTO>> (exConvictsDTO, HttpStatus.OK);		
	}
	
	

}
