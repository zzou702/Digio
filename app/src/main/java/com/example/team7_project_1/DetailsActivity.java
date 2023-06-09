package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.team7_project_1.adapters.ViewPagerAdapter;
import com.example.team7_project_1.models.IProduct;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class DetailsActivity extends AppCompatActivity {

    /**
     * ViewHolder Class
     */
    private class ViewHolder {
        BottomNavigationView bottom_navigation_view;
        ViewPager view_pager; // creating object of ViewPager
        TextView phone_name, phone_subtitle, phone_price, phone_description,
                phone_rating, phone_storage, phone_memory, phone_battery_capacity,
                action_bar_title;
        ImageButton action_bar_back_button;

        public ViewHolder() {
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);

            view_pager = findViewById(R.id.viewPager);

            phone_name = findViewById(R.id.phone_name);
            phone_subtitle = findViewById(R.id.phone_subtitle);
            phone_price = findViewById(R.id.phone_price);
            phone_description = findViewById(R.id.phone_description);
            phone_rating = findViewById(R.id.phone_rating);

            phone_storage = findViewById(R.id.phone_storage);
            phone_memory = findViewById(R.id.phone_memory);
            phone_battery_capacity = findViewById(R.id.phone_battery_capacity);
        }
    }

    // Fields
    ViewHolder vh;
    ViewPagerAdapter view_pager_adapter;
    long product_id;
    Product current_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vh = new ViewHolder();

        loadProduct();

        // Action bar
        initialiseActionBar();

        // Adding the Adapter to the ViewPager
        vh.view_pager.setAdapter(view_pager_adapter);

        // Bottom navigation menu
        initializeNavItem();
        setNavVisibility();
    }



    /**
     * Initialises the action bar using a custom layout
     */
    public void initialiseActionBar() {
        // Use the customer layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        vh.action_bar_title.setText(vh.phone_name.getText());
        vh.action_bar_title.setTextSize(21);

        // Setting the back button to be visible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.VISIBLE);

        // On click listener for the back button that returns to the previous activity
        vh.action_bar_back_button.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }


    /**
     * This method loads the product that has been selected from the previous page and populates the
     * detailed page
     */
    public void loadProduct() {
        product_id = getIntent().getLongExtra("product_id", 1);
        current_product = IProduct.getProductById(product_id);

        // Incrementing the frequency of the current product
        current_product.incrementFrequency();

        if (current_product == null) {
            Log.e("LoadProduct", "Could not get product; product was NULL.");
            return;
        }

        Phone current_phone = current_product.getPhone();


        int[] images = DataProvider.getPhoneImageResourcesById(product_id, this);
        view_pager_adapter = new ViewPagerAdapter(DetailsActivity.this, images);

        vh.phone_name.setText(current_product.getName());
        vh.phone_subtitle.setText(current_phone.getSubtitle());
        vh.phone_price.setText(String.format(Locale.getDefault(),"$%.2f", current_product.getPrice()));
        vh.phone_description.setText(current_product.getDescription());
        vh.phone_rating.setText(String.format(Locale.getDefault(), "%.1f", current_product.getRating()));
        vh.phone_storage.setText(current_phone.getSpecification("storageSize").getValue());
        vh.phone_memory.setText(current_phone.getSpecification("memorySize").getValue());
        vh.phone_battery_capacity.setText(current_phone.getSpecification("batteryCapacity").getValue());
    }



    /**
     * Sets the bottom navigation bar visible or invisible depending on whether the keyboard is
     * activated
     */
    public void setNavVisibility() {
        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                is_open -> {
                    if(is_open) {
                        vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                    }else{
                        vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                    }
                }
        );
    }



    /**
     * Initialises the navigation item selected for the details page
     */
    public void initializeNavItem(){
        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_home) {
                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            } else if (menuItem.getItemId() == R.id.nav_search){
                startActivity(new Intent(DetailsActivity.this, SearchActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            }else if (menuItem.getItemId() == R.id.nav_cart){
                startActivity(new Intent(DetailsActivity.this, CartActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            }
            return false;
        });
    }



    /**
     * Takes the user to the compare filter page
     * @param v
     */
    public void compareButtonClicked(View v) {
        Intent intent = new Intent(DetailsActivity.this, ComparisonFilterActivity.class);
        intent.putExtra("product1_id", this.product_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    /**
     * Adds the current item/product to cart(DataHolder)
     * @param v
     */
    public void addToCartButtonClicked(View v) {
        boolean is_in_shopping_cart = DataProvider.addToShoppingCart(product_id);
        if (!is_in_shopping_cart) {
            Toasty.normal(this, "Added to cart!", Toast.LENGTH_SHORT).show();
        } else {
            Toasty.normal(this, "This phone is already inside your cart!", Toast.LENGTH_SHORT).show();
        }
    }
}
