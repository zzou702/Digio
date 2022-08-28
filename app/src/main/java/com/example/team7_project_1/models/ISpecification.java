package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

public interface ISpecification {

    String getFieldName();

    String getValue();

    String getFormattedValue();

    @NonNull
    String toString();
}
