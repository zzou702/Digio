package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

public class IOSCategory extends Category {

    /**
     * Constructor
     */
    public IOSCategory(String brand) {
        super(brand);
    }

    @NonNull
    @Override
    public String toString() {
        return Names.IOS.toString();
    }

    /**
     * Gets the OS image for the given iOS phone
     * @param context
     * @return int value of the OS image
     */
    @Override
    public int getOSImageId(Context context) {
        return context.getResources().getIdentifier("ios_logo", "drawable", context.getPackageName());
    }

    /**
     * Gets the brand image for the phone. It returns 0 as the iOS phones do not have a logo
     * for their brand.
     * @param context
     * @return int value of the brand image
     */
    @Override
    public int getBrandImageId(Context context) {
        return 0;
    }
}
