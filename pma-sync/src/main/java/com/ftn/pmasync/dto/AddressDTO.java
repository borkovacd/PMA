package com.ftn.pmasync.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class AddressDTO {
	
	 private int id;
	 private String name;
	 private double lat;
	 private double lang;
	
	
	public AddressDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLang() {
		return lang;
	}
	public void setLang(double lang) {
		this.lang = lang;
	}
	
	

}
