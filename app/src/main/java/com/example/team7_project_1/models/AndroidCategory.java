package com.example.team7_project_1.models;

import android.content.Context;
import androidx.annotation.NonNull;


public class AndroidCategory extends Category {
    @NonNull
    @Override
    public String toString() {
        return Names.ANDROID.toString();
    }

    @Override
    public int getOSImageId(Context context) {
        return context.getResources().getIdentifier("android_logo", "drawable", context.getPackageName());
    }

    @Override
    public int getBrandImageId(Context context) {
        return context.getResources().getIdentifier("android_logo", "drawable", context.getPackageName());
    }
}
