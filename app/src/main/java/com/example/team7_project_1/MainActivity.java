package com.example.team7_project_1;


import androidx.annotation.NonNull;
import static com.google.android.material.textfield.TextInputLayout.*;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.ImageButton;

import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    //categories to be chosen
    public SearchActivity.Category cat;

    /** View holder class*/
    private class ViewHolder{
        BottomNavigationView bottom_navigation_view;
        RecyclerView top_picks_recycler_view;
        ProgressBar phone_load_progressbar;

        public ViewHolder(){
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            top_picks_recycler_view = findViewById(R.id.top_picks_recycler_view);
            phone_load_progressbar = findViewById(R.id.phone_load_progressBar);
        }
    }

    ArrayList<Phone> phones = new ArrayList<Phone>();
    ArrayList<Product> products = new ArrayList<Product>();
    PhoneAdapter adapter;
    ViewHolder vh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();

        // Fetch and store data from Firestore
        fetchPhoneData();

        // Generating the Top Picks
        generatedTopPicks();

        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();

    }

    public void fetchPhoneData() {
        // Check if data has already been fetched before
        if (DataProvider.isHasFetchedData()) {
            vh.phone_load_progressbar.setVisibility(View.GONE);
            return;
        }
        ArrayList<Phone> phoneList = new ArrayList<>();
        ArrayList<Product> productList = new ArrayList<>();

        // Getting phone collection from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("phones").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("LoadPhones", "onSuccess: LIST EMPTY");
                    } else {
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
//
//                            // and we will pass this object class
//                            // inside our arraylist which we have
//                            // created for recycler view.
//                            phoneList.add(phone);
                        }
                        Log.d("LoadPhones", "onSuccess: " + phoneList);
                        vh.phone_load_progressbar.setVisibility(View.GONE);
                    }
                });

        // Store data in DataProvider
        DataProvider.setPhoneList(phoneList);
        DataProvider.setProductList(productList);

        DataProvider.setHasFetchedData(true);
    }

    public void generatedTopPicks() {
        initializeArrays();
        adapter = new PhoneAdapter(phones, products,this);

        LinearLayoutManager layout_manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.top_picks_recycler_view.setLayoutManager(layout_manager);

        // Attach the adapter to the recyclerview to populate items
        vh.top_picks_recycler_view.setAdapter(adapter);
    }

    public void initializeArrays() {
        for (Product product : DataProvider.getProducts()) {
            if (product.getRating() >= 4.3) {
                this.products.add(product);
            }
        }

        for (Product product: this.products) {
            for (Phone phone: DataProvider.getPhones()) {
                if (product.getSoldPhoneId() == phone.getId()) {
                    this.phones.add(phone);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        };

        menu.findItem(R.id.search_bar).setOnActionExpandListener(onActionExpandListener);
        SearchView search_view = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        search_view.setQueryHint("Search...");

        // Listening for when the user commits to searching something
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("user_search", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    /** this method takes the user to the category page when each of the category buttons are pressed */
    public void categoryButtonPressed(View v){
        switch(v.getId())
        {
            case R.id.category_btn_android: //if android button is pressed
                cat = SearchActivity.Category.ANDROID;
                break;
            case R.id.category_btn_ios: //if ios button is pressed
                cat = SearchActivity.Category.IOS;
                break;
            case R.id.category_btn_other: // if OTHER button is pressed
                cat = SearchActivity.Category.OTHER;
                break;
        }

        // directing to the new activity(ListActivity) and passing the category selected to
        // the new activity via putExtra()
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("CATEGORY_CHOSEN", cat);
        startActivity(intent);
    }

    /** This method initialises the navigation item selected for the home page*/
    public void initializeNavItem(){
        //set home selected
        vh.bottom_navigation_view.setSelectedItemId(R.id.nav_home);

        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        return true;
                    case R.id.nav_cart:
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                        return true;
                }

                return false;
            }
        });
    }

    /** This method sets the bottom navigation bar visible or invisible depending on whether the
     *  keyboard is activated */
    public void setNavVisibility(){
        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                        }else{
                            vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}
