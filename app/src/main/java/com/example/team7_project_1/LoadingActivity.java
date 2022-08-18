package com.example.team7_project_1;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class LoadingActivity extends AppCompatActivity {

    //categories to be chosen
    public SearchActivity.Category cat;

    /** View holder class*/
    private class ViewHolder{
        ProgressBar phone_load_progressbar;

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

        // Fetch and store data from Firestore
        fetchPhoneData();
    }

    public void fetchPhoneData() {
        ArrayList<Phone> phoneList = new ArrayList<>();
        ArrayList<Product> productList = new ArrayList<>();

        // Getting phone collection from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("phones").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("LoadPhones", "onSuccess: LIST EMPTY");
                        return;
                    }

                    List<DocumentSnapshot> phoneDocuments = documentSnapshots.getDocuments();

                    for (DocumentSnapshot document : phoneDocuments) {
                        Map<String,Object> data = document.getData();

                        if (data == null) {
                            Log.e("LoadPhones", "Phone document is NULL");
                            return;
                        }

                        Log.i("LoadPhones", "Parsing phone: " + data.toString());

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
                        Log.i("LoadPhones", "Created phone: " + phone.toString());

                        Product product = new Product(
                                phone.getId(),
                                phone.getName(),
                                Double.parseDouble(Objects.requireNonNull(data.get("price")).toString()),
                                Objects.requireNonNull(data.get("description")).toString(),
                                Double.parseDouble(Objects.requireNonNull(data.get("rating")).toString()));

                        productList.add(product);
                        Log.i("LoadPhones", "Created product: " + product.toString());

                        // fails: Could not deserialize object. Expected a List, but got a class java.util.HashMap
                        // https://stackoverflow.com/questions/55694354/expected-a-list-while-deserializing-but-got-a-class-java-util-hashmap
//                            phone = document.toObject(Phone.class);

                        Log.d("LoadPhones", "onSuccess: " + phoneList);
                        vh.phone_load_progressbar.setVisibility(View.INVISIBLE);

                        // Start Main Activity once all data has been fetched
                        asycStartMainActivity();
                    }
                });

        // Store data in DataProvider
        DataProvider.setPhoneList(phoneList);
        DataProvider.setProductList(productList);
    }

    private void asycStartMainActivity() {
        finish();
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
    }
}
