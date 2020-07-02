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
import com.ftn.pmasync.model.User;
import com.ftn.pmasync.repository.ReportRepository;
import com.ftn.pmasync.service.ExConvictService;
import com.ftn.pmasync.service.ReportService;
import com.ftn.pmasync.service.UserService;

@RestController
@RequestMapping(path = "/api/reports")
public class ReportController {
	
	@Autowired
	private ReportService reportService ;
	@Autowired
	private UserService userService ;
	@Autowired
	private ExConvictService exConvictService ;
	
	@RequestMapping(value = "/syncReports", method = RequestMethod.POST)
	public ResponseEntity<List<Report>> syncActivity(@RequestBody List<ReportDTO> dtos) {
		boolean tmp = false;
		for(ReportDTO dto : dtos) {
			
			User u = userService.findOneById(dto.getUserId());
			ExConvict exc = exConvictService.findOneById(dto.getExConvictId());
			
			Report report = new Report();
			report.setCity(dto.getCity());
			report.setComment(dto.getComment());
			report.setDate(dto.getDate());
			report.setExConvictId(exc);
			report.setId(dto.getId());
			report.setLang(dto.getLang());
			report.setLat(dto.getLat());
			report.setLocation(dto.getLocation());
			report.setSync(true);
			report.setUserId(u);
			
			System.out.println("UserEmail: " + u.getEmail() + ", ExConvictName: " + exc.getFirstName());
			Report result = reportService.save(report);

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
