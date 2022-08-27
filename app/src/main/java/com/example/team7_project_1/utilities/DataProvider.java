package com.example.team7_project_1.utilities;

import android.content.Context;

import com.example.team7_project_1.models.BoolSpecification;
import com.example.team7_project_1.models.IPhone;
import com.example.team7_project_1.models.IProduct;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.models.Specification;
import com.example.team7_project_1.models.SpecificationDatabaseType;
import com.example.team7_project_1.models.StringSpecification;
import com.example.team7_project_1.models.UnitValueSpecification;

import java.util.ArrayList;
import java.util.Locale;

public class DataProvider {

    // Fields
    private static final int NUM_PHONE_IMAGES = 4;
    public static final String NOT_APPLICABLE = "N/A";

    private static ArrayList<Phone> all_phones;
    private static ArrayList<Product> all_products;
    private static ArrayList<SpecificationDatabaseType> all_specification_types;

    // Field used in the CartActivity in order to keep track of what phones the user has put
    // in their shopping cart
    private static ArrayList<Product> shopping_cart_products = new ArrayList<>();

    // Field used in the ComparisonFilterActivity to keep track of the ID of the first product we
    // decided to compare
    private static long first_product_id = -1;


    /**
     * Getters and setters for the all_phones and all_products ArrayLists
     */
    public static void setPhoneList(ArrayList<Phone> phones) {
        // Create copy of list for storage
        DataProvider.all_phones = new ArrayList<>(phones);
    }

    public static void setProductList(ArrayList<Product> products) {
        // Create copy of list for storage
        DataProvider.all_products = new ArrayList<>(products);
    }

    public static ArrayList<Phone> getPhones() {
        return all_phones;
    }

    public static ArrayList<Product> getProducts() {
        return all_products;
    }


    public static void setSpecificationTypesList(ArrayList<SpecificationDatabaseType> specificationTypesList) {
        DataProvider.all_specification_types = specificationTypesList;
    }

    public static void parsePhoneSpecifications() {
        // For each phone, convert stored string specifications into one of the Specification classes
        for (Phone phone : all_phones) {
            ArrayList<Specification> typed_specifications = new ArrayList<>();
            ArrayList<Specification> stored_string_specs = phone.getSpecifications();

            for (Specification phone_string_spec : stored_string_specs) {
                SpecificationDatabaseType spec_type = null;

                // Lookup specification type of stored string specification
                for (SpecificationDatabaseType spec_type_item : all_specification_types) {
                    if (spec_type_item.getFieldName().equals(phone_string_spec.getFieldName())) {
                        spec_type = spec_type_item;
                        break;
                    }
                }

                // Use existing stored stringSpec to reference values
                Specification specification = phone_string_spec;

                // null if no matching spec type found
                if (spec_type != null) {
                    // Create typed specifications
                    switch (spec_type.getType()) {
                        case "String": {
                            specification = new StringSpecification(
                                    spec_type.getFieldName(),
                                    spec_type.getDisplayName(),
                                    phone_string_spec.getValue());
                            break;
                        }
                        case "Boolean": {
                            boolean value;

                            if (phone_string_spec.getFormattedValue().equals(NOT_APPLICABLE)) {
                                value = false; // default
                            } else {
                                value = Boolean.parseBoolean(phone_string_spec.getValue());
                            }

                            specification = new BoolSpecification(
                                    spec_type.getFieldName(),
                                    spec_type.getDisplayName(),
                                    value);
                            break;
                        }

                        case "UnitValue": {
                            double value;

                            if (phone_string_spec.getFormattedValue().equals(NOT_APPLICABLE)) {
                                value = -1; // default
                            } else {
                                value = Double.parseDouble(phone_string_spec.getValue());
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
        }
    }

    /**
     * Gets the images of a product given its id
     * @param product_id
     * @param context
     * @return int[] array containing the values for the images
     */
    public static int[] getPhoneImageResourcesById(long product_id, Context context) {
        int[] images = new int[NUM_PHONE_IMAGES];

        for (int image_index = 0; image_index < NUM_PHONE_IMAGES; image_index++) {
            images[image_index] = context.getResources().getIdentifier(String.format(Locale.getDefault(),"p%d_%d_medium", product_id, image_index + 1), "drawable", context.getPackageName());
        }

        // TODO: handle when images not found, return NotFound image?
        return images;
    }



    /** SHOPPING CART*/

    /**
     * Returns the shopping cart ArrayList
     * @return ArrayList with products in shopping cart
     */
    public static ArrayList<Product> getShoppingCartProducts() {
        return shopping_cart_products;
    }

    /**
     * Adds a given product from the shopping cart given its id
     * @param product_id
     */
    public static boolean addToShoppingCart(long product_id) {
        Product product = IProduct.getProductById(product_id);
        if (!shopping_cart_products.contains(product)) {
            shopping_cart_products.add(product);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Removes a given product from the shopping cart given its id
     * @param product_id
     */
    public static void removeFromShoppingCart(long product_id) {
        Product product = IProduct.getProductById(product_id);
        shopping_cart_products.remove(product);
    }

    /**
     * Checks if the shopping cart is empty or not
     * @return true if its empty and false otherwise
     */
    public static boolean isCartEmpty() {
        return shopping_cart_products.isEmpty();
    }

    /**
     * Empties the shopping cart. Done when the user checks out.
     */
    public static void emptyCart() {
        shopping_cart_products.clear();
    }


    /** Getter and setter for first_product_id which is used when comparing two given products */
    public static void setFirstProductId(long first_product_id) {
        DataProvider.first_product_id = first_product_id;
    }

    public static long getFirstProductId() {
        return first_product_id;
    }


}