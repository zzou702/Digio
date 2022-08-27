package com.example.team7_project_1.models;

public class BoolSpecification extends Specification {

    // Field
    private boolean value;

    /**
     * Constructor
     */
    public BoolSpecification(String field_name, String display_name, boolean value) {
        super(field_name, display_name);
        this.value = value;
    }


    /** Getter and setter for value field */
    @Override
    public String getValue() {
        return Boolean.toString(this.value);
    }

    @Override
    public String getFormattedValue() {
        return Boolean.toString(this.value);
    }

}
