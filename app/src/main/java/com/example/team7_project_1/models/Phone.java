package com.example.team7_project_1.models;

import androidx.annotation.NonNull;

import com.example.team7_project_1.utilities.DataProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class Phone extends Product implements IPhone {
    // Fields
    private String subtitle;
    private String operating_system;
    private String brand;
    private String manufacturer_part_no;
    private ArrayList<Specification> specifications = new ArrayList<>();

    private Category category;

    /**
     * Constructor
     */
    public Phone(long id, String name, double price, String description, double rating, String subtitle, String operating_system, String brand, String manufacturer_part_no) {
        super(id, name, price, description, rating, 0);
        this.subtitle = subtitle;
        this.operating_system = operating_system;
        this.brand = brand;
        this.manufacturer_part_no = manufacturer_part_no;

        assignToCategory();
    }

    /**
     * Determines the category based on OS
     */
    private void assignToCategory() {
        if (Category.Names.ANDROID.equals(operating_system)) {
           this.category = new AndroidCategory(this.brand);
        } else if (Category.Names.IOS.equals(operating_system)) {
            this.category = new IOSCategory(this.brand);
        } else {
            this.category = new OtherCategory(this.brand);
        }
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
        return "Phone{" + '\'' +
                ", subtitle='" + this.subtitle + '\'' +
                ", operatingSystem='" + this.operating_system + '\'' +
                ", brand='" + this.brand + '\'' +
                ", manufacturerPartNo='" + this.manufacturer_part_no + '\'' +
                ", specifications=" + this.specifications +
                '}';
    }

    /** Getters and setters for fields */

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

    public Category getCategory() {
        return category;
    }
}
