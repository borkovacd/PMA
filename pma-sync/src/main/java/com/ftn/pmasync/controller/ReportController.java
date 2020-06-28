package com.ftn.pmasync.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.pmasync.model.Report;
import com.ftn.pmasync.repository.ReportRepository;

@RestController
@RequestMapping(path = "/api/reports")
public class ReportController {
	
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

}
