package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

public class OtherCategory extends Category {

    /**
     * Constructor
     */
    public OtherCategory(String brand) {
        super(brand);
    }

    @NonNull
    @Override
    public String toString() {
        return Names.OTHER.toString();
    }

    /**
     * Gets the OS image for the given Other phone
     * @param context
     * @return int value of the OS image
     */
    @Override
    public int getOSImageId(Context context) {
        return context.getResources().getIdentifier("other_category_icon", "drawable", context.getPackageName());
    }

    /**
     * Gets the brand image for the given Other phone. If the given phone's brand is Microsoft
     * than it will return the value for the Windows logo. Otherwise, it will return 0 meaning
     * it will not have a brand image.
     * @param context
     * @return int value of the brand image
     */
    @Override
    public int getBrandImageId(Context context) {
        switch (this.brand) {
            case "Microsoft":
                return context.getResources().getIdentifier("windows_logo", "drawable", context.getPackageName());
            default:
                return 0;
        }
    }
}
