package com.example.team7_project_1.models;

public class BoolSpecification extends Specification {
    private boolean value;

    public BoolSpecification(String field_name, String display_name, boolean value) {
        super(field_name, display_name);
        this.value = value;
    }

    @Override
    public String getValue() {
        return Boolean.toString(this.value);
    }

    @Override
    public String getFormattedValue() {
        return Boolean.toString(this.value);
    }

    @Override
    public boolean isBetterThan(Specification other_spec) {
        // TODO: implement
        return false;
    }
}
