package com.example.team7_project_1.models;

public class BoolSpecification extends Specification {
    private boolean value;

    public BoolSpecification(String fieldName, String displayName, boolean value) {
        super(fieldName, displayName);
        this.value = value;
    }

    @Override
    public String getFormattedValue() {
        return Boolean.toString(this.value);
    }

    @Override
    public boolean isBetterThan(Specification otherSpec) {
        // TODO: implement
        return false;
    }
}
