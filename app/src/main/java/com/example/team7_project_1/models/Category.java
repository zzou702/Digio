package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

public abstract class Category implements ICategory {

    // Field
    public final String brand;

    /**
     * Constructor
     */
    Category(String brand) {
        this.brand = brand;
    }

    public abstract int getOSImageId(Context context);
    public abstract int getBrandImageId(Context context);

    @NonNull
    public abstract String toString();

}
