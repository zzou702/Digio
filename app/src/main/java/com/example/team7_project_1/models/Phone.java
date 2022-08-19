package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

import com.example.team7_project_1.DataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Phone {
    // Fields
    private int id;
    private String name;
    private String subtitle;
    private String operating_system;
    private String brand;
    private String manufacturer_part_no;
    private ArrayList<Specification> specifications = new ArrayList<>();;

    /**
     * Constructor
     */
    public Phone(int id, String name, String subtitle, String operating_system, String brand, String manufacturer_part_no) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.operating_system = operating_system;
        this.brand = brand;
        this.manufacturer_part_no = manufacturer_part_no;

    }

    public void parseSpecifications(Object spec_obj) {
        // TODO: see if theres an alternative to remove unchecked cast; we know that spec_obj is a hashmap in database
        HashMap<String,Object> spec_map = (HashMap<String, Object>) spec_obj;

        for (HashMap.Entry<String, Object> entry : spec_map.entrySet()) {
            String value = entry.getValue().toString();

            // Empty string due to N/A value in database
            if (value.equals("")) {
                value = DataProvider.NOT_APPLICABLE;
            }
            Specification spec = new StringSpecification(entry.getKey(), entry.getKey(), value);

            // temporarily store as string; wait for specification types to be fetched
            this.specifications.add(spec);
        }

    }

    @NonNull
    @Override
    public String toString() {
        return "Phone{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", subtitle='" + this.subtitle + '\'' +
                ", operatingSystem='" + this.operating_system + '\'' +
                ", brand='" + this.brand + '\'' +
                ", manufacturerPartNo='" + this.manufacturer_part_no + '\'' +
                ", specifications=" + this.specifications +
                '}';
    }

    /** Getters and setters for fields */
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getManufacturerPartNo() {
        return this.manufacturer_part_no;
    }

    public String getOperatingSystem() {return this.operating_system; }

    public void setSpecifications(ArrayList<Specification> specifications) {
        this.specifications = specifications;
    }

    public ArrayList<Specification> getSpecifications() {
        return specifications;
    }

    public Specification getSpecification(String spec_field_name) {
        for (Specification specification : specifications) {
            if (specification.getFieldName().equals(spec_field_name)) {
                return specification;
            }
        }
        return null;
    }
}
