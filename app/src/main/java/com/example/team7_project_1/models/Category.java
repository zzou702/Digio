package com.example.team7_project_1.models;

import android.content.Context;

import androidx.annotation.NonNull;

public abstract class Category {

    // Field
    public final String brand;

    /**
     * Constructor
     */
    Category(String brand) {
        this.brand = brand;
    }

    public abstract int getOSImageId(Context context);
    public abstract int getBrandImageId(Context context);

    @NonNull
    public abstract String toString();

    public enum Names {
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
