package com.example.team7_project_1;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.models.SpecificationDatabaseType;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class LoadingActivity extends AppCompatActivity {

    //categories to be chosen
    public SearchActivity.Category cat;
    private boolean isFetchPhonesComplete, isFetchSpecificationsComplete = false;

    /** View holder class*/
    private class ViewHolder{
        ProgressBar phoneLoadProgressbar;

        public ViewHolder(){
            phoneLoadProgressbar = findViewById(R.id.phone_load_progressBar);
        }
    }

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        vh = new ViewHolder();

        // Fetch and store data from Firestore
        fetchPhoneData();
        fetchSpecificationTypesData();
    }


    public void fetchPhoneData() {
        ArrayList<Phone> phoneList = new ArrayList<>();
        ArrayList<Product> productList = new ArrayList<>();

        // Getting phone collection from Firestore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("phones").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("Load", "onSuccess: LIST EMPTY");
                        return;
                    }

                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();

                    for (DocumentSnapshot phoneDocument : documents) {
                        Map<String,Object> data = phoneDocument.getData();

                        if (data == null) {
                            Log.e("Load", "Document is NULL");
                            return;
                        }

                        // requiresNonNull to prevent passing null values to constructor
                        // NOTE: if NullPointerException, might be due to field not added to database

                        Phone phone = new Phone(
                                Integer.parseInt(Objects.requireNonNull(data.get("id")).toString()),
                                Objects.requireNonNull(data.get("name")).toString(),
                                Objects.requireNonNull(data.get("subtitle")).toString(),
                                Objects.requireNonNull(data.get("operatingSystem")).toString(),
                                Objects.requireNonNull(data.get("brand")).toString(),
                                Objects.requireNonNull(data.get("manufacturerPartNo")).toString());

                        phone.parseSpecifications(data.get("specifications"));

                        phoneList.add(phone);

                        Product product = new Product(
                                phone.getId(),
                                phone.getName(),
                                Double.parseDouble(Objects.requireNonNull(data.get("price")).toString()),
                                Objects.requireNonNull(data.get("description")).toString(),
                                Double.parseDouble(Objects.requireNonNull(data.get("rating")).toString()));

                        productList.add(product);

                        // fails: Could not deserialize object. Expected a List, but got a class java.util.HashMap
                        // https://stackoverflow.com/questions/55694354/expected-a-list-while-deserializing-but-got-a-class-java-util-hashmap
//                            phone = document.toObject(Phone.class);
                    }

                    // Store data in DataProvider
                    DataProvider.setPhoneList(phoneList);
                    DataProvider.setProductList(productList);

                    // Start Main Activity once all data has been fetched
                    isFetchPhonesComplete = true;
                    asycStartMainActivity();
                });
    }

    public void fetchSpecificationTypesData() {
        ArrayList<SpecificationDatabaseType> specificationTypes = new ArrayList<>();

        // Getting specifications collection from Firestore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("specifications").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("Load", "onSuccess: LIST EMPTY");
                        return;
                    }

                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();

                    for (DocumentSnapshot specsDocument : documents) {
                        Map<String,Object> data = specsDocument.getData();

                        if (data == null) {
                            Log.e("Load", "Document is NULL");
                            return;
                        }

                        // requiresNonNull to prevent passing null values to constructor
                        // NOTE: if NullPointerException, might be due to field not added to database

                        SpecificationDatabaseType specType = new SpecificationDatabaseType(
                                Objects.requireNonNull(data.get("fieldName")).toString(),
                                Objects.requireNonNull(data.get("type")).toString(),
                                Objects.requireNonNull(data.get("displayName")).toString(),
                                Objects.requireNonNull(data.get("unit")).toString());

                        specificationTypes.add(specType);
                    }

                    // Store data in DataProvider
                    DataProvider.setSpecificationTypesList(specificationTypes);

                    // Start Main Activity once all data has been fetched
                    isFetchSpecificationsComplete = true;
                    asycStartMainActivity();
                });
    }

    private void asycStartMainActivity() {
        // Check if both fetch tasks are complete
        Log.i("Test", isFetchPhonesComplete + " or " + isFetchSpecificationsComplete);
        if (!isFetchPhonesComplete || !isFetchSpecificationsComplete) {
            return;
        }

        vh.phoneLoadProgressbar.setVisibility(View.INVISIBLE);

        finish();
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
    }
}
