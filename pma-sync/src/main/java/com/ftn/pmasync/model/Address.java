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
public class Address {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
	 private int id;
    
     @Column
	 private String name;
    
     @Column
	 private double lat;
    
     @Column
	 private double lang;

	@Override
	public String toString() {
		return "Address [id=" + id + ", name=" + name + ", lat=" + lat + ", lang=" + lang + "]";
	}

	public Address() {
		super();
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
