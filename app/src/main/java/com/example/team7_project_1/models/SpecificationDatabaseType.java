package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

public class SpecificationDatabaseType {

    // Fields
    private String field_name;
    private String type;
    private String display_name;
    private String unit;

    /**
     * Constructor
     */
    public SpecificationDatabaseType(String field_name, String type, String display_name, String unit) {
        this.field_name = field_name;
        this.type = type;
        this.display_name = display_name;
        this.unit = unit;
    }

    /** Getters and Setters for fields */
    public String getFieldName() {
        return field_name;
    }

    public String getType() {
        return type;
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getUnit() {
        return unit;
    }

    @NonNull
    @Override
    public String toString() {
        return "SpecificationDatabaseType{" +
                "fieldName='" + field_name + '\'' +
                ", type='" + type + '\'' +
                ", displayName='" + display_name + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
