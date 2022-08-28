package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

import com.example.team7_project_1.utilities.DataProvider;

public interface IProduct {

    @NonNull
    String toString();

    long getId();

    String getName();

    double getPrice();

    String getDescription();

    double getRating();

    Phone getPhone();

    /**
     * Gets the Product object given its id
     * @param product_id
     * @return Product object with the given id
     */
    static Product getProductById(long product_id) {
        for (Product product : DataProvider.getProducts()) {
            if (product.getId() == product_id) {
                return product;
            }
        }
        return null;
    }

}
