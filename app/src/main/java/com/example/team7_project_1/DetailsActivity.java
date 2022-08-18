package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.team7_project_1.adapters.ViewPagerAdapter;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private final int IMAGE_COUNT = 4;

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter view_pager_adapter;

    /** View holder class*/
    private class ViewHolder{
        BottomNavigationView bottom_navigation_view;
        ViewPager view_pager; // creating object of ViewPager
        TextView phone_name, phone_subtitle, phone_price, phone_description, phone_rating, phone_storage, phone_memory, phone_battery_capacity;

        public ViewHolder(){
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            view_pager = (ViewPager) findViewById(R.id.viewPager);

            phone_name = (TextView) findViewById(R.id.phone_name);
            phone_subtitle = (TextView) findViewById(R.id.phone_subtitle);
            phone_price = (TextView) findViewById(R.id.phone_price);
            phone_description = (TextView) findViewById(R.id.phone_description);
            phone_rating = (TextView) findViewById(R.id.phone_rating);

            phone_storage = (TextView) findViewById(R.id.phone_storage);
            phone_memory = (TextView) findViewById(R.id.phone_memory);
            phone_battery_capacity = (TextView) findViewById(R.id.phone_battery_capacity);
        }
    }

    private int phone_id;
    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vh = new ViewHolder();

        loadProduct();

        setTitle(vh.phone_name.getText());

        // Adding the Adapter to the ViewPager
        vh.view_pager.setAdapter(view_pager_adapter);

        initializeNavItem();
        setNavVisibility();
    }

    public void loadProduct() {
        this.phone_id = (Integer) getIntent().getIntExtra("phone_id", 1);
        Product current_product = DataProvider.getProductByPhoneId(phone_id);

        if (current_product == null) {
            Log.e("LoadProduct", "Could not get product; product was NULL.");
            return;
        }

        Phone current_phone = current_product.getSoldPhone();

        // TODO: get images based on id



        int[] images = new int[IMAGE_COUNT];

        for (int image_index = 0; image_index < IMAGE_COUNT; image_index++) {
            images[image_index] = this.getResources().getIdentifier(String.format(Locale.getDefault(),"p%d_%d_medium", phone_id, image_index + 1), "drawable", this.getPackageName());
        }
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

    /** This method sets the bottom navigation bar visible or invisible depending on whether the
     *  keyboard is activated */
    public void setNavVisibility() {
        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean is_open) {
                        if(is_open) {
                            vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                        }else{
                            vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    /** This method initialises the navigation item selected for the home page*/
    public void initializeNavItem(){
        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(DetailsActivity.this, SearchActivity.class));
                        return true;
                    case R.id.nav_cart:
                        startActivity(new Intent(DetailsActivity.this, CartActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    /** This method takes the user back to the previous page*/
    public void backButtonClicked(View v){
        finish();
    }

    public void compareButtonClicked(View v) {
        Intent intent = new Intent(DetailsActivity.this, ComparisonFilterActivity.class);
        intent.putExtra("first_phone_id", this.phone_id);
        startActivity(intent);
    }

    private void comparePhone(Phone otherPhone) {
        // TODO (Ou-An): upon getting phone for comparison, open modal with side by side specifications comparison
        // comparePhone(otherPhone)
    }
}
