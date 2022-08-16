package com.example.team7_project_1.models;

import android.util.Log;

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
    private List<Specification> specifications;

    // TODO: temporary variable, must move to Product model class
    private int price;

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

    public Phone(String brand, int price) {
        this.brand = brand;
        this.price = price;
    }

    public void parseSpecifications(Object specObj) {
        // TODO: see if theres an alternative to remove unchecked cast; we know that specObj is a hashmap in database
        HashMap<String,Object> specMap = (HashMap<String, Object>) specObj;

        for (HashMap.Entry<String, Object> entry : specMap.entrySet()) {
            Log.i("test", entry.getKey() + ":" + entry.getValue().toString());
        }
        Log.i("END", "end----------");

    }


    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBrand() {
        return brand;
    }

    public String getManufacturerPartNo() {
        return manufacturerPartNo;
    }

    public List<Specification> getSpecifications() {
        // Return copy rather than reference of array; prevents external modification
        return null;
    }
}
