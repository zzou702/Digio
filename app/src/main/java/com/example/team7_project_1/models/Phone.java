package com.example.team7_project_1.models;

import java.util.Arrays;

public class Phone {
    private String brand;
    private String manufacturerPartNo;
    private Specification[] specifications;

    public Phone(String brand, String manufacturerPartNo, Specification[] specifications) {
        this.brand = brand;
        this.manufacturerPartNo = manufacturerPartNo;
        this.specifications = specifications;
    }

    public String getBrand() {
        return brand;
    }

    public String getManufacturerPartNo() {
        return manufacturerPartNo;
    }

    public Specification[] getSpecifications() {
        // Return copy rather than reference of array; prevents external modification
        return Arrays.copyOf(specifications, specifications.length);
    }
}
