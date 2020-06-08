package com.ftn.pmasync.model;

import java.util.List;

import javax.persistence.Embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExConvictReport {
	
	@Embedded
    public ExConvict exConvict;

    public List<Report> reports;

}
