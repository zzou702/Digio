package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

public class OtherCategory extends Category {
    @NonNull
    @Override
    public String toString() {
        return Names.OTHER.toString();
    }

    @Override
    public int getOSImageId() {
//        context.getResources().getIdentifier(String.format(Locale.getDefault(),
//                "p%d_%d_medium", phone_id, image_index + 1), "drawable", context.getPackageName());
        return 0;
    }

    @Override
    public int getBrandImageId() {
        return 0;
    }
}