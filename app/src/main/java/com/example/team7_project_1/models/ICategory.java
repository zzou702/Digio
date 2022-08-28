package com.example.team7_project_1.models;

import android.content.Context;

public interface ICategory {

    /**
     * Gets the OS image ID of this category
     * @param context Activity context the image is fetched in
     * @return OS image id
     */
    int getOSImageId(Context context);

    /**
     * Gets the brand image ID of this category
     * @param context Activity context the image is fetched in
     * @return Brand image id
     */
    int getBrandImageId(Context context);
}
