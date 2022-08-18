package com.example.team7_project_1.models;

public class UnitValueSpecification extends Specification {
    private double value;
    private String unit;

    public UnitValueSpecification(String fieldName, String displayName, double value, String unit) {
        super(fieldName, displayName);
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String getFormattedValue() {
        return this.value + this.unit;
    }

    @Override
    public boolean isBetterThan(Specification otherSpec) {
        // TODO: implement
        return false;
    }
}
