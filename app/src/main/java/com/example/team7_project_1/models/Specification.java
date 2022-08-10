package com.example.team7_project_1.models;

public abstract class Specification {
    private String name;

    protected Specification(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getFormattedValue();
    public abstract boolean isBetterThan(Specification otherSpec);
}
