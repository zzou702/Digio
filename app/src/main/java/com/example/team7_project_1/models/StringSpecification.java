package com.example.team7_project_1.models;

public class StringSpecification extends Specification {

    // Field
    private String value;

    /**
     * Constructor
     */
    public StringSpecification(String field_name, String display_name, String value) {
        super(field_name, display_name);
        this.value = value;
    }

    /**
     * Getter and setter for value field
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public String getFormattedValue() {
        return this.value;
    }

}
