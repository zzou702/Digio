package com.example.team7_project_1.models;

import java.util.ArrayList;

public interface IPhone {

    void parseSpecifications(Object spec_obj);

    String getSubtitle();

    String getOperatingSystem();

    void setSpecifications(ArrayList<Specification> specifications);

     ArrayList<Specification> getSpecifications();

     Specification getSpecification(String spec_field_name);


}
