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
    private static int first_phone_id = -1;
    ArrayList<Phone> phones = new ArrayList<Phone>();
    ArrayList<Product> products = new ArrayList<Product>();
    PhoneAdapter adapter;
    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparison_search);


        vh = new ViewHolder();

        //calling the method to populate the SearchActivity
        generatePhoneList();
        setPhoneID();
    }

    public void setPhoneID() {
        first_phone_id = (Integer) getIntent().getIntExtra("first_phone_id", 0);
    }

    public static int getPhoneID() {
        return first_phone_id;
    }

    /**
     * Generates the phone list used to populated the RecyclerView, and setting the adapter needed
     * in order to achieve this
     */
    public void generatePhoneList() {
        initializeArrays();
        setPhoneAdapter();
    }

    /**
     * Sets the adapter for the RecyclerView
     */
    public void setPhoneAdapter() {
        adapter = new PhoneAdapter(phones, products,this);

        // Creating layout with 2 vertical columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        vh.recycler_view_phones.setLayoutManager(gridLayoutManager);

        // Attaching the adapter to the recyclerView in order to populate it
        vh.recycler_view_phones.setAdapter(adapter);
    }

    /**
     * Initializes the phones and products ArrayLists
     */
    public void initializeArrays() {
        // Getting the user search that have been passed using the putExtra() method
        this.user_search = (String) getIntent().getStringExtra("user_search");

        // If the user_search is not null - filter all phones by the user_search and get only the
        // phones whose name is somewhat associated with the user_search
        // if both chosen_category and user_search are null - get all phones
        if (this.user_search != null) {
            filterUserSearches();
        } else {
            this.phones = DataProvider.getPhones();
            this.products = DataProvider.getProducts();
        }
    }

    /**
     * Filters phones by the given user search and puts these phones and associated products into
     * the phones and products ArrayLists
     */
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

        // Getting associated products
        for (Phone phone: this.phones) {
            for (Product product: DataProvider.getProducts()) {
                if (phone.getId() == product.getSoldPhoneId()) {
                    this.products.add(product);
                }
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
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
}