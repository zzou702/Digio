package com.example.team7_project_1.models;

public class StringSpecification extends Specification {
    private String value;

    public StringSpecification(String fieldName, String displayName, String value) {
        super(fieldName, displayName);
        this.value = value;
    }

    @Override
    public String getFormattedValue() {
        return this.value;
    }

    @Override
    public boolean isBetterThan(Specification otherSpec) {
        // TODO: implement
        return false;
    }
}
