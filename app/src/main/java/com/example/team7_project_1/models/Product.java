package com.example.team7_project_1.models;

import java.math.BigDecimal;

public class Product {
    private Phone phoneSold;
    private int id;
    private String name;
    private BigDecimal price;
    private String description;
    private int rating;

    public Product(Phone phoneSold, int id, String name, BigDecimal price, String description, int rating) {
        this.phoneSold = phoneSold;
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }

    public String getPhoneDetails() {
        return "";
    }

}
