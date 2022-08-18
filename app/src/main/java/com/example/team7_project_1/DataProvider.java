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

    private static ArrayList<Phone> phoneList;
    private static ArrayList<Product> productList;
    private static ArrayList<SpecificationDatabaseType> specificationTypesList;

    public static void setPhoneList(ArrayList<Phone> phoneList) {
        DataProvider.phoneList = phoneList;
    }

    public static void setProductList(ArrayList<Product> productList) {
        DataProvider.productList = productList;
    }

    public static ArrayList<Phone> getPhones() {
        return phoneList;
    }

    public static ArrayList<Product> getProducts() {
        return productList;
    }

    public static Phone getPhoneById(int id) {
        for (Phone phone : phoneList) {
            if (phone.getId() == id) {
                return phone;
            }
        }
        // could not find phone of id
        return null;
    }

    public static Product getProductByPhoneId(int phoneId) {
        for (Product product : productList) {
            if (product.getSoldPhoneId() == phoneId) {
                return product;
            }
        }
        // could not find product of phoneId
        return null;
    }


    public static void setSpecificationTypesList(ArrayList<SpecificationDatabaseType> specificationTypesList) {
        DataProvider.specificationTypesList = specificationTypesList;
    }

    public static void parsePhoneSpecifications() {
        // For each phone, convert stored string specifications into one of the Specification classes
        for (Phone phone : phoneList) {
            ArrayList<Specification> typedSpecifications = new ArrayList<>();
            ArrayList<Specification> storedStringSpecs = phone.getSpecifications();

            for (Specification stringSpec : storedStringSpecs) {
                SpecificationDatabaseType specType = null;

                // Lookup specification type of stored string specification
                for (SpecificationDatabaseType specTypeItem : specificationTypesList) {
                    if (specTypeItem.getFieldName().equals(stringSpec.getFieldName())) {
                        specType = specTypeItem;
                        break;
                    }
                }

                // Use String type as default (existing stored stringSpec)
                Specification specification = stringSpec;

                // null if no matching spec type found
                if (specType != null && !specType.getType().equals("String")) {
                    // Create typed specifications
                    switch (specType.getType()) {
                        case "Boolean": {
                            boolean value;

                            if (stringSpec.getFormattedValue().equals(NOT_APPLICABLE)) {
                                value = false; // default
                            } else {
                                value = Boolean.parseBoolean(stringSpec.getValue());
                            }

                            specification = new BoolSpecification(
                                    specType.getFieldName(),
                                    specType.getDisplayName(),
                                    value);
                            break;
                        }

                        case "UnitValue": {
                            double value;

                            if (stringSpec.getFormattedValue().equals(NOT_APPLICABLE)) {
                                value = -1; // default
                            } else {
                                value = Double.parseDouble(stringSpec.getValue());
                            }

                            specification = new UnitValueSpecification(
                                    specType.getFieldName(),
                                    specType.getDisplayName(),
                                    value,
                                    specType.getUnit());

                            break;
                        }
                    }
                }

                typedSpecifications.add(specification);
            }

            phone.setSpecifications(typedSpecifications);
            Log.i("test", typedSpecifications.toString());
        }
    }
}