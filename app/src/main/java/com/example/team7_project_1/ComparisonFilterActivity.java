package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.adapters.ProductAdapter;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ComparisonFilterActivity extends AppCompatActivity {

    /**
     * ViewHolder Class
     */
    private class ViewHolder {
        RecyclerView recycler_view_phones;
        BottomNavigationView bottom_navigation_view;
        TextView action_bar_title;
        ImageButton action_bar_back_button;
        TextView no_results_text;

        /**
         * Constructor
         */
        public ViewHolder() {
            recycler_view_phones = findViewById(R.id.comparison_recycler_view);
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            no_results_text = findViewById(R.id.no_results_text);
        }
    }

    // Fields
    private String user_search; //the user search
    ArrayList<Product> products = new ArrayList<>();
    ProductAdapter adapter;
    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_search);

        // Initialising the ViewHolder
        vh = new ViewHolder();


        // Generating the product list
        generateProductList();

        // Initialising the action bar
        initialiseActionBar();

        // Setting empty results text
        setEmptyResults();

        // Initialising the bottom navigation bar ie setting onClickListener for each item
        initializeNavItem();
    }



    /**
     * This method initialises the action bar using a custom layout
     * */
    public void initialiseActionBar() {
        // Use the custom layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Setting the back button to be invisible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.VISIBLE);

        // On click listener for the back button
        vh.action_bar_back_button.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        );

        //Setting the title of the page
        setLabel();
    }



    /**
     * Changes the title on the header based on user action
     */
    public void setLabel() {
        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        if (user_search == null) {
            vh.action_bar_title.setText("Choose one to compare");
            vh.action_bar_title.setTextSize(20);
        } else {
            if (!products.isEmpty()) {
                vh.action_bar_title.setText((products.size()) + " Results Found");
                vh.action_bar_title.setTextSize(21);
            }
        }
    }



    /**
     * Changing the visibility of the no_results_text TextView depending on whether or not there are any results
     */
    public void setEmptyResults() {
        if (products.isEmpty()) {
            vh.no_results_text.setVisibility(View.VISIBLE);
        } else {
            vh.no_results_text.setVisibility(View.GONE);
        }
    }



    /**
     * Generates the product list used to populated the RecyclerView, and sets the adapter needed
     * in order to achieve this
     */
    public void generateProductList() {
        initializeArray();
        setProductAdapter();
    }



    /**
     * Sets the adapter for the RecyclerView
     */
    public void setProductAdapter() {
        adapter = new ProductAdapter(products,this);

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
        // Setting the ProductId that was passed in the putExtra() method
        long product1_id = getIntent().getLongExtra("product1_id", DataProvider.getFirstProductId());
        DataProvider.setFirstProductId(product1_id);

        // Getting the user search that have been passed using the putExtra() method
        this.user_search = getIntent().getStringExtra("user_search");

        // If the user_search is not null - filter all phones by the user_search and get only the
        // phones whose name is somewhat associated with the user_search
        // if both chosen_category and user_search are null - get all phones
        if (this.user_search != null) {
            filterUserSearch();
        } else {
            filterOutFirstPhoneId();
        }
    }



    /**
     * Filters phones by the given user search and puts these phones and associated products into
     * the phones and products ArrayLists
     */
    public void filterUserSearch() {
        // "Cleaning" the user search
        this.user_search = this.user_search.trim();
        for (Product product: DataProvider.getProducts()) {
            String current_phone_name = product.getName();
            if ((current_phone_name.equalsIgnoreCase(this.user_search)) ||
                    (current_phone_name.toLowerCase().contains(this.user_search.toLowerCase()))) {
                if (product.getId() != DataProvider.getFirstProductId()) {
                    this.products.add(product);
                }
            }
        }
    }



    /**
     * Filters out the first_phone_id out of our phones and products ArrayLists as it doesn't make
     * sense to be able to compare a given phone to itself
     */
    public void filterOutFirstPhoneId() {
        for (Product product: DataProvider.getProducts()) {
            if (product.getId() != DataProvider.getFirstProductId()) {
                this.products.add(product);
            }
        }
    }



    /**
     * Creates the top bar menu used for the user to search for phones
     * @param menu
     * @return boolean (true/false)
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
                // Passing the user search to the ComparisonFilterActivity
                Intent intent = new Intent(ComparisonFilterActivity.this, ComparisonFilterActivity.class);
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
     * Initialises the navigation item selected for the comparison filter page
     */
    public void initializeNavItem() {
        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_home) {
                startActivity(new Intent(ComparisonFilterActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            } else if (menuItem.getItemId() == R.id.nav_search){
                startActivity(new Intent(ComparisonFilterActivity.this, SearchActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            }else if (menuItem.getItemId() == R.id.nav_cart){
                startActivity(new Intent(ComparisonFilterActivity.this, CartActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            }
            return false;
        });
    }
}