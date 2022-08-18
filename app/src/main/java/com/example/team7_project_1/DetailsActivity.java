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

    // images array
    int[] images = { R.drawable.mphapp113111__1, R.drawable.mphapp113111__2, R.drawable.mphapp113111__3, R.drawable.mphapp113111__4};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;

    /** View holder class*/
    private class ViewHolder{
        BottomNavigationView bottomNavigationView;
        ViewPager mViewPager; // creating object of ViewPager
        TextView phoneName, phoneSubtitle, phonePrice, phoneDescription, phoneRating;

        public ViewHolder(){
            bottomNavigationView = findViewById(R.id.bottom_nav_bar);
            mViewPager = (ViewPager) findViewById(R.id.viewPager);

            phoneName = (TextView) findViewById(R.id.phone_name);
            phoneSubtitle = (TextView) findViewById(R.id.phone_subtitle);
            phonePrice = (TextView) findViewById(R.id.phone_price);
            phoneDescription = (TextView) findViewById(R.id.phone_description);
            phoneRating = (TextView) findViewById(R.id.phone_rating);
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

        setTitle(vh.phoneName.getText());

        // Adding the Adapter to the ViewPager
        vh.mViewPager.setAdapter(mViewPagerAdapter);

        initializeNavItem();
        setNavVisibility();
    }

    public void loadProduct() {
        this.phone_id = (Integer) getIntent().getIntExtra("phone_id", 1);
        Product currentProduct = DataProvider.getProductByPhoneId(phone_id);

        if (currentProduct == null) {
            Log.e("LoadProduct", "Could not get product; product was NULL.");
            return;
        }

        Phone currentPhone = currentProduct.getSoldPhone();

        // TODO: get images based on id
        mViewPagerAdapter = new ViewPagerAdapter(DetailsActivity.this, images);

        vh.phoneName.setText(currentProduct.getName());
        vh.phoneSubtitle.setText(currentPhone.getSubtitle());
        vh.phonePrice.setText(String.format(Locale.getDefault(),"$%.2f", currentProduct.getPrice()));
        vh.phoneDescription.setText(currentProduct.getDescription());
        vh.phoneRating.setText(String.format(Locale.getDefault(), "%.1f", currentProduct.getRating()));
    }

    /** This method sets the bottom navigation bar visible or invisible depending on whether the
     *  keyboard is activated */
    public void setNavVisibility() {
        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            vh.bottomNavigationView.setVisibility(View.INVISIBLE);
                        }else{
                            vh.bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    /** This method initialises the navigation item selected for the home page*/
    public void initializeNavItem(){
        //setting ItemSelectedListener
        vh.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
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
