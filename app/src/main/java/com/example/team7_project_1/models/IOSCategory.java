package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

public class IOSCategory extends Category {
    @NonNull
    @Override
    public String toString() {
        return Names.IOS.toString();
    }

    @Override
    public int getOSImageId(Context context) {
        return context.getResources().getIdentifier("ios_logo.png", "drawable", context.getPackageName());
    }
}
