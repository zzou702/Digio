package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

public class SpecificationDatabaseType {
    private String fieldName;
    private String type;
    private String displayName;
    private String unit;

    public SpecificationDatabaseType(String fieldName, String type, String displayName, String unit) {
        this.fieldName = fieldName;
        this.type = type;
        this.displayName = displayName;
        this.unit = unit;
    }

    @NonNull
    @Override
    public String toString() {
        return "SpecificationDatabaseType{" +
                "fieldName='" + fieldName + '\'' +
                ", type='" + type + '\'' +
                ", displayName='" + displayName + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
