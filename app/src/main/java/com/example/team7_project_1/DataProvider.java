package com.example.team7_project_1;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.team7_project_1.models.Phone;
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

    static String[] names = {"Iphone", "Iphone", "Iphone", "Iphone", "Iphone", "Iphone", "Iphone"};

    public static ArrayList<Phone> generateData() {
        ArrayList<Phone> phones = new ArrayList<>();

        Random random = new Random();
        for (String name : names) {
            int rnd = random.nextInt(1200);
            Phone phone = new Phone(name, rnd);
            phones.add(phone);
        }
        return phones;
    }

//    public static ArrayList<Product> createProductFromPhones() {
//
//    }



    public static ArrayList<Phone> getPhoneDataAsList() {
        return phoneList;
    }
}
