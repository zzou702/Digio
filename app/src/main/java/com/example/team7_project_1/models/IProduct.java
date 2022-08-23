package com.example.team7_project_1.models;

import com.example.team7_project_1.DataProvider;

public interface IProduct {
    String toString();

    int getSoldPhoneId();

    String getName();

    double getPrice();

    String getDescription();

    double getRating();

    Phone getSoldPhone();
}
