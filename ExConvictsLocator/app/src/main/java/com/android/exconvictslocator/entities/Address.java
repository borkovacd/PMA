package com.android.exconvictslocator.entities;

public class Address {

    private int id;
    private String name;
    private double lat;
    private double lang;

    public Address() {
    }

    public Address(int id, String name, double lat, double lang) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lang = lang;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
