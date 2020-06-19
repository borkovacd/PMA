package com.ftn.pmasync.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.pmasync.model.ExConvict;
import com.ftn.pmasync.repository.ExConvictRepository;

@Service
public class ExConvictService {
	
	@Autowired
	private ExConvictRepository exConvictRepository;

	public List<ExConvict> getExConvicts() {
		return exConvictRepository.findAll();
	}

}
