package com.example.team7_project_1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;


import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    /** Represents the type of category the user selected */
    public enum Category {
        ANDROID,
        IOS,
        OTHER,
    }

    /** View holder class*/
    private class ViewHolder {
        BottomNavigationView bottom_navigation_view;
        TextView test;
        RecyclerView recycler_view_phones;

        public ViewHolder(){
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            test = findViewById(R.id.test);
            recycler_view_phones = (RecyclerView) findViewById(R.id.search_recycler_view);
        }
    }

    private Category chosen_category; //the chosen category
    private String user_search; //the user search
    ArrayList<Phone> phones = new ArrayList<Phone>();
    ArrayList<Product> products = new ArrayList<Product>();
    PhoneAdapter adapter;
    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        vh = new ViewHolder();

        //calling the method to populate the SearchActivity
        generatePhoneList();
        initializeNavItem();
        setNavVisibility();
        setLabel();
    }

    /** This method changes the title on the header based on user action*/
    public void setLabel(){
        if(this.chosen_category == null) {
            setTitle("Search");
        }else if (this.chosen_category != null && user_search == null){
            setTitle(this.chosen_category.toString());
        }
    }

    public void generatePhoneList() {
        initializeArrays();
        setRecyclerAdapter();
    }

    public void setRecyclerAdapter() {
        adapter = new PhoneAdapter( phones, products,this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        vh.recycler_view_phones.setLayoutManager(gridLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        vh.recycler_view_phones.setAdapter(adapter);
    }


    public void initializeArrays() {
        //getting the chosen category that has been passed through the putExtra() method
        this.chosen_category = (Category) getIntent().getSerializableExtra("CATEGORY_CHOSEN");
        this.user_search = (String) getIntent().getStringExtra("user_search");
        if (this.chosen_category != null) {
            filterCategories();
        } else if (this.user_search != null) {
            filterUserSearches();
        } else {
            this.phones = DataProvider.getPhones();
            this.products = DataProvider.getProducts();
        }
    }

    public void filterCategories() {
        String category = this.chosen_category.name();
        for (Phone phone: DataProvider.getPhones()) {
            String current_phone_operating_system = phone.getOperatingSystem();
            if (category.equals("OTHER") && !current_phone_operating_system.equals("iOS") && !current_phone_operating_system.equals("Android")) {
                this.phones.add(phone);
            }
            else if (current_phone_operating_system.equalsIgnoreCase(category)) {
                this.phones.add(phone);
            }
        }

        for (Phone phone: this.phones) {
            for (Product product: DataProvider.getProducts()) {
                if (phone.getId() == product.getSoldPhoneId()) {
                    this.products.add(product);
                }
            }
        }
    }

    public void filterUserSearches() {
        // "Cleaning" the user search
        this.user_search = this.user_search.trim();
        for (Phone phone: DataProvider.getPhones()) {
            String current_phone_name = phone.getName();
            if ((current_phone_name.equalsIgnoreCase(this.user_search)) ||
            (current_phone_name.toLowerCase().contains(this.user_search.toLowerCase()))) {
                this.phones.add(phone);
            }
        }

        for (Phone phone: this.phones) {
            for (Product product: DataProvider.getProducts()) {
                if (phone.getId() == product.getSoldPhoneId()) {
                    this.products.add(product);
                }
            }
        }
    }



    /** This method initialises the navigation item selected for the search page*/
    public void initializeNavItem() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        //setting ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(SearchActivity.this, SearchActivity.class));
                        return true;
                    case R.id.nav_cart:
                        startActivity(new Intent(SearchActivity.this, CartActivity.class));
                        return true;
                }
                return false;
            }
        });
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
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
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
    
    /** This method takes the user back to the previous page*/
    public void backButtonClicked(View v){
        finish();
    }

    /** This method sets the bottom navigation bar visible or invisible depending on whether the
     *  keyboard is activated */
    public void setNavVisibility(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean is_open) {
                        if(is_open) {
                            vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                        }else {
                            vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}


