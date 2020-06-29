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

import com.ftn.pmasync.dto.ExConvictDTO;
import com.ftn.pmasync.dto.ReportDTO;
import com.ftn.pmasync.model.ExConvict;
import com.ftn.pmasync.model.Report;
import com.ftn.pmasync.repository.ReportRepository;
import com.ftn.pmasync.service.ReportService;

@RestController
@RequestMapping(path = "/api/reports")
public class ReportController {
	
	@Autowired
	private ReportService reportService ;
	//Brisi ovaj repository cole mu :D
	@Autowired
	private ReportRepository reportRepository ;
	
	@RequestMapping(value = "/syncReports", method = RequestMethod.POST)
	public ResponseEntity<List<Report>> syncActivity(@RequestBody List<Report> reports) {
		boolean tmp = false;
		for(Report report : reports) {
			Report result = reportRepository.save(report);
			if(result == null)
				tmp = true;
		}
		if(tmp == false) // ako je uspeo da ga sacuva
			return new ResponseEntity<>(HttpStatus.OK);
		else // ako nije uspeo da ga sacuva
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ReportDTO>> getReports() {
		List<ReportDTO> reportsDTO = new ArrayList<ReportDTO>();
		List<Report> reports = reportService.getReports();
		for(Report report : reports) {
			ModelMapper modelMapper = new ModelMapper();
			ReportDTO reportDTO = modelMapper.map(report, ReportDTO.class);
			reportsDTO.add(reportDTO);
		}
		return new ResponseEntity<List<ReportDTO>> (reportsDTO, HttpStatus.OK);		
	}
	
	

}
