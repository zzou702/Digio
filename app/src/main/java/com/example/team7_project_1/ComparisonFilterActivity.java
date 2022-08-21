package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;

import java.util.ArrayList;

public class ComparisonFilterActivity extends AppCompatActivity {

    /**
     * View holder class
     */
    private class ViewHolder {
        RecyclerView recycler_view_phones;

        /**
         * Constructor
         */
        public ViewHolder() {
            recycler_view_phones = (RecyclerView) findViewById(R.id.comparison_recycler_view);
        }
    }

    // Fields
    private String user_search; //the user search
    ArrayList<Product> products = new ArrayList<Product>();
    PhoneAdapter adapter;
    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparison_search);

        // Initialising the ViewHolder
        vh = new ViewHolder();

        // Setting the PhoneID that was passed in the putExtra() method
        setPhoneID();

        // Generating Phone List
        generatePhoneList();

    }



    public void setPhoneID() {
        DataHolder.first_product_id = (Integer) getIntent().getIntExtra("phone1_id", DataHolder.first_product_id);
    }

    public static int getPhoneID() {
        return DataHolder.first_product_id;
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
        // Getting the user search that have been passed using the putExtra() method
        this.user_search = (String) getIntent().getStringExtra("user_search");

        // If the user_search is not null - filter all phones by the user_search and get only the
        // phones whose name is somewhat associated with the user_search
        // if both chosen_category and user_search are null - get all phones
        if (this.user_search != null) {
            filterUserSearches();
        } else {
            filterOutFirstPhoneID();
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
            String current_phone_name = product.getSoldPhone().getName();
            if ((current_phone_name.equalsIgnoreCase(this.user_search)) ||
                    (current_phone_name.toLowerCase().contains(this.user_search.toLowerCase()))) {
                if (product.getSoldPhone().getId() != DataHolder.first_product_id) {
                    this.products.add(product);
                }
            }
        }
    }

    /**
     * Filters out the first_phone_id out of our phones and products ArrayLists as it doesn't make
     * sense to be able to compare a given phone to itself
     */
    public void filterOutFirstPhoneID() {
        for (Product product: DataProvider.getProducts()) {
            if (product.getSoldPhone().getId() != DataHolder.first_product_id) {
                this.products.add(product);
            }
        }
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
                // Passing the user search to the ComparisonFilterActivity
                Intent intent = new Intent(ComparisonFilterActivity.this, ComparisonFilterActivity.class);
                intent.putExtra("user_search", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String new_text) {
                return false;
            }
        });

        return true;
    }
}