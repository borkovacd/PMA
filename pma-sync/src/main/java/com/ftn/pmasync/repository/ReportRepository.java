package com.ftn.pmasync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.pmasync.model.Report;

public interface ReportRepository extends JpaRepository<Report, Integer>{
	
	//Report fineOneById(int id);

}
