package com.example.team7_project_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    /**
     *  View holder class
     */
    private class ViewHolder {
        RecyclerView recycler_view_phones;
        BottomNavigationView bottom_navigation_view;
        TextView cart_empty_text, subtotal_text, total_text, gst;
        LinearLayout cart_details;
        TextView action_bar_title;
        ImageButton action_bar_back_button;

        /**
         * Constructor
         */
        public ViewHolder(){
            recycler_view_phones = findViewById(R.id.cart_recycler_view);
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            cart_empty_text = findViewById(R.id.cart_empty_text);
            cart_details = findViewById(R.id.cart_details);
            subtotal_text = findViewById(R.id.subtotal_text);
            total_text = findViewById(R.id.total_text);
            gst = findViewById(R.id.gst);
        }
    }

    ViewHolder vh;
    PhoneAdapter adapter;
    ArrayList<Product> products = new ArrayList<>();
    double GST_PERCENTAGE = 0.15;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.activity_cart);

        vh = new ViewHolder();

        // Action bar
        initialiseActionBar();

        generatePhoneList();

        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();

        updateCheckoutVisibility();
    }


    /**
     * This method initialises the action bar using a custom layout
     * */
    public void initialiseActionBar(){
        // Use the customer layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        vh.action_bar_title.setText("Shopping Cart");

        // Setting the back button to be invisible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.INVISIBLE);
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
        this.products = DataProvider.getShoppingCartProducts();
    }


    /**
     * Sets the adapter for the RecyclerView
     */
    public void setPhoneAdapter() {
        adapter = new PhoneAdapter(products,this);

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
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(menu_item -> {
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
                is_open -> {
                    if(is_open){
                        vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                    }else{
                        vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                    }
                }
        );
    }


    /**
     * Shows "cart is empty" text if empty, otherwise show list of added phones
     * and checkout details
     */
    public void updateCheckoutVisibility() {
        if (DataProvider.isCartEmpty()) {
            vh.cart_details.setVisibility(View.GONE);
            vh.cart_empty_text.setVisibility(View.VISIBLE);
        } else {
            vh.cart_details.setVisibility(View.VISIBLE);
            vh.cart_empty_text.setVisibility(View.GONE);

            // variable for total price
            double total_price = 0;

            // Goes through every product in the cart and add their price to total price

            ArrayList<Product> products = DataProvider.getShoppingCartProducts();

            for(int i = 0; i < products.size(); i++){
                total_price += products.get(i).getPrice();
            }

            // Setting the text for each of the checkout section
            vh.subtotal_text.setText(String.format(Locale.getDefault(),"Subtotal:  $%.2f", total_price * (1 - GST_PERCENTAGE)));
            vh.gst.setText(String.format(Locale.getDefault(),"GST:  $%.2f", total_price * GST_PERCENTAGE));
            vh.total_text.setText(String.format(Locale.getDefault(),"Total (incl. GST):  $%.2f", total_price));
        }
    }

    public void checkoutButtonClicked(View view) {
        DataProvider.emptyCart();
        Toast.makeText(this,"Thank you for your purchase!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}