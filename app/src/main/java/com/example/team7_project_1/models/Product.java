package com.example.team7_project_1.models;

import com.example.team7_project_1.DataProvider;

public class Product implements IProduct {
    // Fields
    private long id;
    private String name;
    private double price;
    private String description;
    private double rating;

    /**
     * Constructor
     */
    public Product(long id, String name, double price, String description, double rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Product{" +
                "soldPhoneId=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }

    /**
     * Getters and Setters for fields
     */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public Phone getPhone() {
        return (Phone) this;
    }

}
