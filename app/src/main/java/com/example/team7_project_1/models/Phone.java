package com.example.team7_project_1.models;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Phone {
    private int id;
    private String name;
    private String subtitle;
    private String operatingSystem;
    private String brand;
    private String manufacturerPartNo;
    private ArrayList<Specification> specifications = new ArrayList<>();;

    // TODO: temporary variable, must move to Product model class
    private double price;

    public Phone() {
        // empty constructor
        // required for Firebase converting to object.
    }

    public Phone(int id, String name, String subtitle, String operatingSystem, String brand, String manufacturerPartNo) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.operatingSystem = operatingSystem;
        this.brand = brand;
        this.manufacturerPartNo = manufacturerPartNo;

    }

    public void parseSpecifications(Object specObj) {
        // TODO: see if theres an alternative to remove unchecked cast; we know that specObj is a hashmap in database
        HashMap<String,Object> specMap = (HashMap<String, Object>) specObj;

        for (HashMap.Entry<String, Object> entry : specMap.entrySet()) {
            Specification spec = new StringSpecification(entry.getKey(), entry.getValue().toString());
            this.specifications.add(spec);
        }

    }

    @NonNull
    @Override
    public String toString() {
        return "Phone{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", subtitle='" + this.subtitle + '\'' +
                ", operatingSystem='" + this.operatingSystem + '\'' +
                ", brand='" + this.brand + '\'' +
                ", manufacturerPartNo='" + this.manufacturerPartNo + '\'' +
                ", specifications=" + this.specifications +
                '}';
    }

    public double getPrice() {
        return this.price;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getManufacturerPartNo() {
        return this.manufacturerPartNo;
    }

    public String getOperatingSystem() {return this.operatingSystem; }

    public List<Specification> getSpecifications() {
        // Return copy rather than reference of array; prevents external modification
        return null;
    }
}
