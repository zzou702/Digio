package com.example.team7_project_1.models;

import com.example.team7_project_1.DataProvider;

public class Product implements IProduct {
    // Fields
    private int sold_phone_id;
    private String name;
    private double price;
    private String description;
    private double rating;

    /**
     * Constructor
     */
    public Product(int sold_phone_id, String name, double price, String description, double rating) {
        this.sold_phone_id = sold_phone_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Product{" +
                "soldPhoneId=" + sold_phone_id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }

    /**
     * Getters and Setters for fields
     */
    public int getSoldPhoneId() {
        return sold_phone_id;
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

    public Phone getSoldPhone() {
        return DataProvider.getPhoneById(sold_phone_id);
    }

}
