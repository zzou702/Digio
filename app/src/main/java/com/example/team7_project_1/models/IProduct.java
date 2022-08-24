package com.example.team7_project_1.models;

import android.provider.ContactsContract;

import com.example.team7_project_1.DataProvider;

public interface IProduct {
    String toString();

    long getId();

    String getName();

    double getPrice();

    String getDescription();

    double getRating();

    Phone getPhone();

    static Product getProductById(long product_id) {
        for (Product product : DataProvider.getProducts()) {
            if (product.getId() == product_id) {
                return product;
            }
        }
        return null;
    }

}
