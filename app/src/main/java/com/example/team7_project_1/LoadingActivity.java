package com.example.team7_project_1;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
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
    public SearchActivity.Category category;
    private boolean is_fetch_phones_complete, is_fetch_specifications_complete = false;

    /** View holder class*/
    private class ViewHolder{
        ProgressBar phone_load_progressbar;
        TextView action_bar_title;

        public ViewHolder(){
            phone_load_progressbar = findViewById(R.id.phone_load_progressBar);
        }
    }

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        vh = new ViewHolder();

        // Action bar
        initialiseActionBar();

        // Fetch and store data from Firestore
        fetchPhoneData();
        fetchSpecificationTypesData();
    }

    /**
     * This method initialises the action bar using a customer layout
     * */
    public void initialiseActionBar(){
        // Use the customer layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        vh.action_bar_title.setText("DIGIO");
    }


    public void fetchPhoneData() {
        ArrayList<Phone> phones = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();

        // Getting phone collection from Firestore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("phones").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("Load", "onSuccess: LIST EMPTY");
                        return;
                    }

                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();

                    for (DocumentSnapshot phone_document : documents) {
                        Map<String,Object> data = phone_document.getData();

                        if (data == null) {
                            Log.e("Load", "Document is NULL");
                            return;
                        }

                        // requiresNonNull to prevent passing null values to constructor
                        // NOTE: if NullPointerException, might be due to field not added to database

                        Phone current_phone = new Phone (
                                Integer.parseInt(Objects.requireNonNull(data.get("id")).toString()),
                                Objects.requireNonNull(data.get("name")).toString(),
                                Objects.requireNonNull(data.get("subtitle")).toString(),
                                Objects.requireNonNull(data.get("operatingSystem")).toString(),
                                Objects.requireNonNull(data.get("brand")).toString(),
                                Objects.requireNonNull(data.get("manufacturerPartNo")).toString());

                        current_phone.parseSpecifications(data.get("specifications"));

                        phones.add(current_phone);

                        Product current_product = new Product(
                                current_phone.getId(),
                                current_phone.getName(),
                                Double.parseDouble(Objects.requireNonNull(data.get("price")).toString()),
                                Objects.requireNonNull(data.get("description")).toString(),
                                Double.parseDouble(Objects.requireNonNull(data.get("rating")).toString()));

                        products.add(current_product);

                        // fails: Could not deserialize object. Expected a List, but got a class java.util.HashMap
                        // https://stackoverflow.com/questions/55694354/expected-a-list-while-deserializing-but-got-a-class-java-util-hashmap
//                            phone = document.toObject(Phone.class);
                    }

                    // Store data in DataProvider
                    DataProvider.setPhoneList(phones);
                    DataProvider.setProductList(products);

                    // Start Main Activity once all data has been fetched
                    is_fetch_phones_complete = true;
                    asycStartMainActivity();
                });
    }


    public void fetchSpecificationTypesData() {
        ArrayList<SpecificationDatabaseType> specification_types = new ArrayList<>();

        // Getting specifications collection from Firestore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("specifications").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("Load", "onSuccess: LIST EMPTY");
                        return;
                    }

                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();

                    for (DocumentSnapshot specs_document : documents) {
                        Map<String,Object> data = specs_document.getData();

                        if (data == null) {
                            Log.e("Load", "Document is NULL");
                            return;
                        }

                        // requiresNonNull to prevent passing null values to constructor
                        // NOTE: if NullPointerException, might be due to field not added to database

                        SpecificationDatabaseType spec_type = new SpecificationDatabaseType(
                                Objects.requireNonNull(data.get("fieldName")).toString(),
                                Objects.requireNonNull(data.get("type")).toString(),
                                Objects.requireNonNull(data.get("displayName")).toString(),
                                Objects.requireNonNull(data.get("unit")).toString());

                        specification_types.add(spec_type);
                    }

                    // Store data in DataProvider
                    DataProvider.setSpecificationTypesList(specification_types);

                    // Start Main Activity once all data has been fetched
                    is_fetch_specifications_complete = true;
                    asycStartMainActivity();
                });
    }


    private void asycStartMainActivity() {
        // Check if both fetch tasks are complete
        if (!is_fetch_phones_complete || !is_fetch_specifications_complete) {
            return;
        }

        DataProvider.parsePhoneSpecifications();
        vh.phone_load_progressbar.setVisibility(View.INVISIBLE);

        finish();
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
    }
}
