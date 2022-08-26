package com.example.team7_project_1.utilities;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.team7_project_1.R;
import com.example.team7_project_1.models.BoolSpecification;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.models.Specification;
import com.example.team7_project_1.models.SpecificationDatabaseType;
import com.example.team7_project_1.models.UnitValueSpecification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class DataProvider {

    // Fields
    private static final int NUM_PHONE_IMAGES = 4;
    public static final String NOT_APPLICABLE = "N/A";

    private static int NUM_IMAGES = 117;
    public static StorageReference[]  image_holder;

    private static StorageReference storage_ref;

    private static ArrayList<Phone> all_phones;
    private static ArrayList<Product> all_products;
    private static ArrayList<SpecificationDatabaseType> all_specification_types;

    // Fields used in the CartActivity in order to keep track of what phones the user has put
    // in their shopping cart
    private static ArrayList<Product> shopping_cart_products = new ArrayList<>();

    // Field used in the ComparisonFilterActivity to keep track of the ID of the first product we
    // decided to compare
    private static long first_product_id = -1;


    /**
     * Getters and setters for the all_phones and all_products ArrayLists as well as shopping_cart_products
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

    public static Phone getPhoneById(long id) {
        for (Phone phone : all_phones) {
            if (phone.getId() == id) {
                return phone;
            }
        }
        // could not find phone of id
        return null;
    }

    public static Product getProductByPhoneId(long phone_id) {
        for (Product product : all_products) {
            if (product.getId() == phone_id) {
                return product;
            }
        }
        // could not find product of phone_id
        return null;
    }

    public static ArrayList<Product> getShoppingCartProducts() {
        return shopping_cart_products;
    }

    public static long getFirstProductId() {
        return first_product_id;
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
        }
    }

    public static StorageReference[] getPhoneImageResourcesById(long phone_id, Context context) {
        StorageReference[] images = new StorageReference[NUM_PHONE_IMAGES];

        //storage_ref = FirebaseStorage.getInstance().getReference("phone_images/" + Long.toString(phone_id) + "_" + Integer.toString(image_index+1) + "_medium.jpeg");

        String ph_id = Long.toString(phone_id);


        for (int image_index = 0; image_index < NUM_PHONE_IMAGES; image_index++) {
            storage_ref = FirebaseStorage.getInstance().getReference("phone_images/p"
                    + ph_id + "_" + (image_index + 1) + "_medium.jpeg");

            images[image_index] = storage_ref;
            File local_file = null;

            try {
                local_file = File.createTempFile("tempfile", "jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            int finalImage_index = image_index;
            storage_ref.getFile(local_file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            images[finalImage_index] = storage_ref;

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to retrieve image from firebase storage", Toast.LENGTH_SHORT).show();

                        }
                    });

            images[image_index] = storage_ref;
        }

/*
        for (int image_index = 0; image_index < NUM_PHONE_IMAGES; image_index++) {
            images[image_index] = context.getResources().getIdentifier(String.format(Locale.getDefault(),"p%d_%d_medium", phone_id, image_index + 1), "drawable", context.getPackageName());
        }*/

        // TODO: handle when images not found, return NotFound image?
        return images;
    }


    public static boolean addToShoppingCart(long phone_id) {
        Product equivalent_product = DataProvider.getProductByPhoneId(phone_id);
        if (!shopping_cart_products.contains(equivalent_product)) {
            shopping_cart_products.add(equivalent_product);
            return false;
        } else {
            return true;
        }
    }

    public static void removeFromShoppingCart(long phone_id) {
        Product equivalent_product = DataProvider.getProductByPhoneId(phone_id);
        shopping_cart_products.remove(equivalent_product);
    }

    public static boolean isCartEmpty() {
        return shopping_cart_products.isEmpty();
    }

    public static void emptyCart() {
        shopping_cart_products.clear();
    }

    public static void setProductId(long first_product_id) {
        DataProvider.first_product_id = first_product_id;
    }

    public static long getProductId() {
        return DataProvider.first_product_id;
    }
}