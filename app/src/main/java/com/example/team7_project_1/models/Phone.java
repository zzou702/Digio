package com.example.team7_project_1.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.team7_project_1.DataProvider;

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
            String value = entry.getValue().toString();

            // Empty string due to N/A value in database
            if (value.equals("")) {
                value = DataProvider.NOT_APPLICABLE;
            }
            Specification spec = new StringSpecification(entry.getKey(), entry.getKey(), value);

            // temporarily store as string; wait for specification types to be fetched
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

    public void setSpecifications(ArrayList<Specification> specifications) {
        this.specifications = specifications;
    }

    public ArrayList<Specification> getSpecifications() {
        return specifications;
    }

    public Specification getSpecification(String specFieldName) {
        for (Specification specification : specifications) {
            if (specification.getFieldName().equals(specFieldName)) {
                return specification;
            }
        }
        return null;
    }
}
