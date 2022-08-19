package com.example.team7_project_1.models;

import com.example.team7_project_1.DataProvider;

public class UnitValueSpecification extends Specification {
    // Fields
    private double value;
    private String unit;

    /**
     * Constructor
     */
    public UnitValueSpecification(String field_name, String displayName, double value, String unit) {
        super(field_name, displayName);
        this.value = value;
        this.unit = unit;
    }

    /**
     * Getter and setter for value field
     */
    @Override
    public String getValue() {
        // -1 set by DataProvider due to empty field value
        if (this.value == -1) {
            return DataProvider.NOT_APPLICABLE;
        }

        // tidy up so there is no trailing .0
        if (this.value % 1 == 0) {
            return Integer.toString((int) this.value);
        } else {
            return Double.toString(this.value);
        }
    }

    @Override
    public String getFormattedValue() {
        return this.value + this.unit;
    }

    @Override
    public boolean isBetterThan(Specification other_spec) {
        // TODO: implement
        return false;
    }
}
