package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

public class IOSCategory extends Category {
    public IOSCategory(String brand) {
        super(brand);
    }

    @NonNull
    @Override
    public String toString() {
        return Names.IOS.toString();
    }

    @Override
    public int getOSImageId(Context context) {
        return context.getResources().getIdentifier("ios_logo", "drawable", context.getPackageName());
    }

    /**
     * It returns 0 as the IOS phones do not have a logo for their brand
     */
    @Override
    public int getBrandImageId(Context context) {
        return 0;
    }
}
