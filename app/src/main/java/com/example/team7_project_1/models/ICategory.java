package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

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

    enum Names {
        /**
         * Represents the type of category the user selected
         */
        ANDROID ("Android"),
        IOS ("iOS"),
        OTHER ("Other");

        private final String name;

        private Names(String name) {
            this.name = name;
        }

        public boolean equals(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        @NonNull
        public String toString() {
            return this.name;
        }
    }
}
