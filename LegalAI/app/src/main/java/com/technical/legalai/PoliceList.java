package com.technical.legalai;

public class PoliceList {
    String name;
    String address;
    String coordinates;
    public PoliceList(String name, String address, String coordinates) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCoordinates() {
        return coordinates;
    }
}
