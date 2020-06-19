package com.ftn.pmasync.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExConvictDTO {
	
    private Long id;
    private String firstName;
    private String lastName;
    private String pseudonym;
    private int photo;
    private String address;
    private String gender;
    private String dateOfBirth;
    private String crime;
    private String description;
	

}
