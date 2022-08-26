package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

public class OtherCategory extends Category {
    @NonNull
    @Override
    public String toString() {
        return Names.OTHER.toString();
    }

    @Override
    public int getOSImageId(Context context) {
        return context.getResources().getIdentifier("windows_logo.png", "drawable", context.getPackageName());
    }
}
