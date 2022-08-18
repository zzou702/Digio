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

    /** View holder class*/
    private class ViewHolder {
        RecyclerView recyclerViewPhones;

        public ViewHolder() {
            recyclerViewPhones = (RecyclerView) findViewById(R.id.comparison_recycler_view);
        }
    }

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
        this.first_phone_id = (Integer) getIntent().getIntExtra("first_phone_id", 0);
    }

    public static int getPhoneID() {
        return this.first_phone_id;
    }

    public void generatePhoneList() {
        initializeArrays();
        setListAdapter();
    }

    public void setListAdapter() {
        adapter = new PhoneAdapter(phones, products,this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        vh.recyclerViewPhones.setLayoutManager(gridLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        vh.recyclerViewPhones.setAdapter(adapter);
    }

    public void initializeArrays() {
        //getting the chosen category that has been passed through the putExtra() method
        this.user_search = (String) getIntent().getStringExtra("user_search");
        if (this.user_search != null) {
            filterUserSearches();
        } else {
            this.phones = DataProvider.getPhones();
            this.products = DataProvider.getProducts();
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
                Intent intent = new Intent(ComparisonFilterActivity.this, ComparisonFilterActivity.class);
                intent.putExtra("phone_id", first_phone_id);
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