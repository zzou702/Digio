package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

public abstract class Specification {
    private String fieldName;
    private String displayName;

    protected Specification(String fieldName, String displayName) {
        this.fieldName = fieldName;
        this.displayName = displayName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract String getValue();
    public abstract String getFormattedValue();
    public abstract boolean isBetterThan(Specification otherSpec);

    @NonNull
    @Override
    public String toString() {
        return "Specification{" +
                "fieldName='" + fieldName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", value='" + getFormattedValue() + '\'' +
                '}';
    }
}
