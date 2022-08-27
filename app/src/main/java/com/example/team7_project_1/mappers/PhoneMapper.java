package com.example.team7_project_1.mappers;

import android.util.Log;

import com.example.team7_project_1.utilities.DataProvider;
import com.example.team7_project_1.handlers.MapHandler;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class PhoneMapper extends DataMapper {


    public PhoneMapper(String collection_path) {
        super(collection_path);
    }

    @Override
    public void fetchFromDatabase() {
        ArrayList<Phone> phones = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();

        // Getting phone collection from Firestore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(getCollectionPath()).get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("Load", "onSuccess: LIST EMPTY");
                        return;
                    }

                    for (DocumentSnapshot phone_document : documentSnapshots.getDocuments()) {
                        Phone new_phone = parsePhone(phone_document.getData());
                        phones.add(new_phone);
                        products.add(new_phone);
                    }

                    // Store data in DataProvider
                    DataProvider.setPhoneList(phones);
                    DataProvider.setProductList(products);

                    setIsFetchComplete(true);
                    MapHandler.callbackFromFetch();
                });
    }

    private Phone parsePhone(Map<String,Object> data) {
        if (data == null) {
            Log.e("Load", "Document is NULL");
            return null;
        }

        // Not using toObject as: Could not deserialize object. Expected a List, but got a class java.util.HashMap
        // https://stackoverflow.com/questions/55694354/expected-a-list-while-deserializing-but-got-a-class-java-util-hashmap
        // phone = document.toObject(Phone.class);

        // requiresNonNull to prevent passing null values to constructor
        // NOTE: if NullPointerException, might be due to field not added to database
        Phone current_phone = new Phone (
                Integer.parseInt(Objects.requireNonNull(data.get("id")).toString()),
                Objects.requireNonNull(data.get("name")).toString(),
                Double.parseDouble(Objects.requireNonNull(data.get("price")).toString()),
                Objects.requireNonNull(data.get("description")).toString(),
                Double.parseDouble(Objects.requireNonNull(data.get("rating")).toString()),
                Objects.requireNonNull(data.get("subtitle")).toString(),
                Objects.requireNonNull(data.get("operatingSystem")).toString(),
                Objects.requireNonNull(data.get("brand")).toString(),
                Objects.requireNonNull(data.get("manufacturerPartNo")).toString());

        current_phone.parseSpecifications(data.get("specifications"));

        return current_phone;
    }
}
