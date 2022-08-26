package com.example.team7_project_1.models;

public class IOSCategory extends Category {
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
