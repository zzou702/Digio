package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

public abstract class Specification implements ISpecification, Comparable<Specification> {

    // Fields
    private final String field_name;
    private final String display_name;

    /**
     * Constructor
     */
    protected Specification(String field_name, String display_name) {
        this.field_name = field_name;
        this.display_name = display_name;
    }

    public String getFieldName() {
        return field_name;
    }

    public String getDisplayName() {
        return display_name;
    }

    public abstract String getValue();
    public abstract String getFormattedValue();

    @NonNull
    @Override
    public String toString() {
        return "Specification{" +
                "fieldName='" + field_name + '\'' +
                ", displayName='" + display_name + '\'' +
                ", value='" + getFormattedValue() + '\'' +
                '}';
    }

    @Override
    public int compareTo(Specification spec) {
        return this.getDisplayName().compareTo(spec.getDisplayName());
    }
}
