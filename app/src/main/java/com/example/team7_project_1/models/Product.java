package com.example.team7_project_1.models;

import com.example.team7_project_1.DataProvider;

import java.math.BigDecimal;

public class Product {
    private int soldPhoneId;
    private String name;
    private double price;
    private String description;
    private double rating;

    public Product(int soldPhoneId, String name, double price, String description, double rating) {
        this.soldPhoneId = soldPhoneId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Product{" +
                "soldPhoneId=" + soldPhoneId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }

    public int getSoldPhoneId() {
        return soldPhoneId;
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
        return DataProvider.getPhoneById(soldPhoneId);
    }

}
