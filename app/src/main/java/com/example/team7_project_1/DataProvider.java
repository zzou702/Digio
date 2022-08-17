package com.example.team7_project_1;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DataProvider {

    private static ArrayList<Phone> phoneList;
    private static ArrayList<Product> productList;

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
}
