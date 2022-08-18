package com.example.team7_project_1.models;

public class StringSpecification extends Specification {
    private String value;

    public StringSpecification(String field_name, String display_name, String value) {
        super(field_name, display_name);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getFormattedValue() {
        return this.value;
    }

    @Override
    public boolean isBetterThan(Specification other_spec) {
        // TODO: implement
        return false;
    }
}
