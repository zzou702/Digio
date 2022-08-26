package com.example.team7_project_1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    /**
     * Represents the type of category the user selected
     */
    public enum Category {
        ANDROID,
        IOS,
        OTHER,
    }

    /**
     * View holder class
     */
    private class ViewHolder {
        BottomNavigationView bottom_navigation_view;
        RecyclerView recycler_view_phones;
        TextView action_bar_title;
        ImageButton action_bar_back_button;

        /**
         * Constructor
         */
        public ViewHolder() {
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            recycler_view_phones = (RecyclerView) findViewById(R.id.search_recycler_view);
        }
    }

    // Fields
    private Category chosen_category; //the chosen category
    private String user_search; //the user search
    ArrayList<Product> products = new ArrayList<Product>();
    PhoneAdapter adapter;
    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialising the ViewHolder
        vh = new ViewHolder();

        // Generating Phone List
        generatePhoneList();

        // Action bar
        initialiseActionBar();

        //Setting the title of the page
        setLabel();

        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();
    }


    /**
     * This method initialises the action bar using a custom layout
     * */
    public void initialiseActionBar(){
        // Use the customer layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Setting the back button to be invisible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.INVISIBLE);
    }



    /**
     * Changes the title on the header based on user action
     */
    public void setLabel() {
        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);

        //Check whether the user clicked into the page through the category buttons or not
        if(this.chosen_category == null) {
            vh.action_bar_title.setText("Search");
        }else if (this.chosen_category != null && user_search == null) {

            /* Change the string that will be outputted based on the category selected */
            // If the Category chosen is iOS
            if(this.chosen_category == Category.IOS){
                vh.action_bar_title.setText("iOS");
            }else if(this.chosen_category == Category.ANDROID){ // If the Category chose is Android
                vh.action_bar_title.setText("Android");
            }else if(this.chosen_category == Category.OTHER){ // If the Category Chosen is Other
                vh.action_bar_title.setText("Other OS");
            }
        }
    }



    /**
     * Generates the phone list used to populated the RecyclerView, and setting the adapter needed
     * in order to achieve this
     */
    public void generatePhoneList() {
        initializeArray();
        setPhoneAdapter();
    }



    /**
     * Sets the adapter for the RecyclerView
     */
    public void setPhoneAdapter() {
        adapter = new PhoneAdapter(products,false,this);

        // Creating layout with 2 vertical columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        vh.recycler_view_phones.setLayoutManager(gridLayoutManager);

        // Attaching the adapter to the recyclerView in order to populate it
        vh.recycler_view_phones.setAdapter(adapter);
    }



    /**
     * Initializes the phones and products ArrayLists
     */
    public void initializeArray() {
        // Getting the chosen category and user search that have been passed using the putExtra()
        // method
        this.chosen_category = (Category) getIntent().getSerializableExtra("CATEGORY_CHOSEN");
        this.user_search = (String) getIntent().getStringExtra("user_search");

        // If the chosen_category is not null - filter all phones by category and get only the phones
        // that fit into our category
        // If the user_search is not null - filter all phones by the user_search and get only the
        // phones whose name is somewhat associated with the user_search
        // if both chosen_category and user_search are null - get all phones
        if (this.chosen_category != null) {
            filterCategories();
        } else if (this.user_search != null) {
            filterUserSearches();
        } else {
            this.products = new ArrayList<>(DataProvider.getProducts());
        }
    }



    /**
     * Filters phones by the chosen category and puts these phones and associated products into
     * the phones and products ArrayLists
     */
    public void filterCategories() {
        String category = this.chosen_category.name();
        for (Product product: DataProvider.getProducts()) {
            String current_phone_operating_system = product.getPhone().getOperatingSystem();
            if (category.equals("OTHER") && !current_phone_operating_system.equals("iOS") && !current_phone_operating_system.equals("Android")) {
                this.products.add(product);
            }
            else if (current_phone_operating_system.equalsIgnoreCase(category)) {
                this.products.add(product);
            }
        }
    }



    /**
     * Filters phones by the given user search and puts these phones and associated products into
     * the phones and products ArrayLists
     */
    public void filterUserSearches() {
        // "Cleaning" the user search
        this.user_search = this.user_search.trim();
        for (Product product: DataProvider.getProducts()) {
            String current_product_name = product.getPhone().getName();
            if ((current_product_name.equalsIgnoreCase(this.user_search)) ||
            (current_product_name.toLowerCase().contains(this.user_search.toLowerCase()))) {
                this.products.add(product);
            }
        }
    }



    /**
     * Initialises the navigation item selected for the search page
     */
    public void initializeNavItem() {
        //set home selected
        vh.bottom_navigation_view.setSelectedItemId(R.id.nav_search);

        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
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
        });
    }



    /**
     * Creates the top bar menu used for the user to search for phones
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creating menu inflater
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

        // Setting the listener for search bar
        menu.findItem(R.id.search_bar).setOnActionExpandListener(onActionExpandListener);
        SearchView search_view = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        search_view.setQueryHint("Search...");

        // Listening for when the user commits to searching something
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Passing the user search to the SearchActivity
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                intent.putExtra("user_search", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String new_text) {
                adapter.getFilter().filter(new_text);
                return false;
            }
        });

        return true;
    }



    /**
     * Takes the user back to the previous page
     */
    public void backButtonClicked(View v){
        finish();
    }



    /**
     * Sets the bottom navigation bar visible or invisible depending on whether the keyboard is
     * activated
     */
    public void setNavVisibility(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener (
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean is_open) {
                        if (is_open) {
                            vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                        }else {
                            vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}


