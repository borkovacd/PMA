package com.ftn.pmasync.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
	

    private int id;
    private String location;
    private String date;
    private String city;
    private String comment;
    private int userId;
    private int exConvictId;
    private double lat;
    private double lang;
    private boolean isSync ;

}
