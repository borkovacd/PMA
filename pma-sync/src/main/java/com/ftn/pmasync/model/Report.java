package com.ftn.pmasync.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String location;
    @Column
    private String date;
    @Column
    private String city;
    @Column
    private String comment;
    @Column
    private String userId;
    @Column
    private int exConvictId;
    @Column
    private double lat;
    @Column
    private double lang;

}
