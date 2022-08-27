package com.example.team7_project_1.models;

import android.content.Context;
import androidx.annotation.NonNull;

import java.util.Locale;


public class AndroidCategory extends Category {
    public AndroidCategory(String brand) {
        super(brand);
    }

    @NonNull
    @Override
    public String toString() {
        return Names.ANDROID.toString();
    }

    /**
     * Gets the OS image for the given Android phone
     * @param context
     * @return int value of the OS image
     */
    @Override
    public int getOSImageId(Context context) {
        return context.getResources().getIdentifier("android_logo", "drawable", context.getPackageName());
    }

    /**
     * Gets the brand image for the given Android phone
     * @param context
     * @return int value of the brand image
     */
    @Override
    public int getBrandImageId(Context context) {
        return context.getResources().getIdentifier(
                String.format(Locale.getDefault(), "%s_logo", brand.toLowerCase()), "drawable", context.getPackageName());
    }
}
