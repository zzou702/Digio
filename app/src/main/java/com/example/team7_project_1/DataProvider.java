package com.example.team7_project_1;

import android.util.Log;

import com.example.team7_project_1.models.BoolSpecification;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.models.Specification;
import com.example.team7_project_1.models.SpecificationDatabaseType;
import com.example.team7_project_1.models.UnitValueSpecification;

import java.util.ArrayList;

public class DataProvider {

    public static final String NOT_APPLICABLE = "N/A";

    private static ArrayList<Phone> all_phones;
    private static ArrayList<Product> all_products;
    private static ArrayList<SpecificationDatabaseType> all_specification_types;

    /** Getters and setters for the all_phones and all_products ArrayLists*/
    public static void setPhoneList(ArrayList<Phone> phones) {
        DataProvider.all_phones = phones;
    }

    public static void setProductList(ArrayList<Product> products) {
        DataProvider.all_products = products;
    }

    public static ArrayList<Phone> getPhones() {
        return all_phones;
    }

    public static ArrayList<Product> getProducts() {
        return all_products;
    }

    public static Phone getPhoneById(int id) {
        for (Phone phone : all_phones) {
            if (phone.getId() == id) {
                return phone;
            }
        }
        // could not find phone of id
        return null;
    }

    public static Product getProductByPhoneId(int phoneId) {
        for (Product product : all_products) {
            if (product.getSoldPhoneId() == phoneId) {
                return product;
            }
        }
        // could not find product of phoneId
        return null;
    }


    public static void setSpecificationTypesList(ArrayList<SpecificationDatabaseType> specificationTypesList) {
        DataProvider.all_specification_types = specificationTypesList;
    }

    public static void parsePhoneSpecifications() {
        // For each phone, convert stored string specifications into one of the Specification classes
        for (Phone phone : all_phones) {
            ArrayList<Specification> typed_specifications = new ArrayList<>();
            ArrayList<Specification> stored_string_specs = phone.getSpecifications();

            for (Specification string_spec : stored_string_specs) {
                SpecificationDatabaseType spec_type = null;

                // Lookup specification type of stored string specification
                for (SpecificationDatabaseType spec_type_item : all_specification_types) {
                    if (spec_type_item.getFieldName().equals(string_spec.getFieldName())) {
                        spec_type = spec_type_item;
                        break;
                    }
                }

                // Use String type as default (existing stored stringSpec)
                Specification specification = string_spec;

                // null if no matching spec type found
                if (spec_type != null && !spec_type.getType().equals("String")) {
                    // Create typed specifications
                    switch (spec_type.getType()) {
                        case "Boolean": {
                            boolean value;

                            if (string_spec.getFormattedValue().equals(NOT_APPLICABLE)) {
                                value = false; // default
                            } else {
                                value = Boolean.parseBoolean(string_spec.getValue());
                            }

                            specification = new BoolSpecification(
                                    spec_type.getFieldName(),
                                    spec_type.getDisplayName(),
                                    value);
                            break;
                        }

                        case "UnitValue": {
                            double value;

                            if (string_spec.getFormattedValue().equals(NOT_APPLICABLE)) {
                                value = -1; // default
                            } else {
                                value = Double.parseDouble(string_spec.getValue());
                            }

                            specification = new UnitValueSpecification(
                                    spec_type.getFieldName(),
                                    spec_type.getDisplayName(),
                                    value,
                                    spec_type.getUnit());

                            break;
                        }
                    }
                }

                typed_specifications.add(specification);
            }

            phone.setSpecifications(typed_specifications);
            Log.i("test", typed_specifications.toString());
        }
    }
}