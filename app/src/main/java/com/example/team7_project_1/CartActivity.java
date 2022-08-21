package com.example.team7_project_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    /**
     *  View holder class
     */
    private class ViewHolder {
        RecyclerView recycler_view_phones;
        BottomNavigationView bottom_navigation_view;

        /**
         * Constructor
         */
        public ViewHolder(){
            recycler_view_phones = (RecyclerView) findViewById(R.id.cart_recycler_view);
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
        }
    }

    ViewHolder vh;
    PhoneAdapter adapter;
    ArrayList<Product> products = new ArrayList<Product>();

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.activity_cart);

        vh = new ViewHolder();

        //setting the title of the header
        setTitle("Shopping Cart");

        generatePhoneList();

        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();
    }

    /**
     * Generates the phone list used to populated the RecyclerView, and setting the adapter needed
     * in order to achieve this
     */
    public void generatePhoneList() {
        initializeArray();
        setPhoneAdapter();
    }


    public void initializeArray() {
        this.products = DataHolder.shopping_cart_products;
    }


    /**
     * Sets the adapter for the RecyclerView
     */
    public void setPhoneAdapter() {
        adapter = new PhoneAdapter(products,true,this);

        // Creating horizontal vertical layout
        LinearLayoutManager layout_manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vh.recycler_view_phones.setLayoutManager(layout_manager);

        // Attaching the adapter to the recyclerView in order to populate it
        vh.recycler_view_phones.setAdapter(adapter);
    }


    /**
     * Initialises the navigation item selected for the search page
     */
    public void initializeNavItem(){
        //set home selected
        vh.bottom_navigation_view.setSelectedItemId(R.id.nav_cart);

        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menu_item){
                switch(menu_item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(CartActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(CartActivity.this, SearchActivity.class));
                        return true;
                    case R.id.nav_cart:
                        return true;
                }
                return false;
            }
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
                Intent intent = new Intent(CartActivity.this, SearchActivity.class);
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



    /**
     * Sets the bottom navigation bar visible or invisible depending on whether the keyboard is
     * activated
     */
    public void setNavVisibility(){

        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean is_open) {
                        if(is_open){
                            vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                        }else{
                            vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    public void checkoutButtonClicked(View view) {
    }
}