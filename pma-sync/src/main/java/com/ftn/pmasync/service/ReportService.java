package com.ftn.pmasync.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.pmasync.model.Report;
import com.ftn.pmasync.repository.ReportRepository;

@Service
public class ReportService {
	
	@Autowired
	private ReportRepository reportRepository;

	public List<Report> getReports() {
		return reportRepository.findAll();
	}
	
	public Report save(Report r) {
		return reportRepository.save(r);
	}
	
	public Report findOneById(int id) {
		return reportRepository.getOne(id);
	}

}
