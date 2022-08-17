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

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vh = new ViewHolder();

        loadProduct();

        // Adding the Adapter to the ViewPager
        vh.mViewPager.setAdapter(mViewPagerAdapter);

        setNavVisibility();
    }

    public void loadProduct() {
        int phoneId = getIntent().getIntExtra("phone_id", 1);
        Product currentProduct = DataProvider.getProductByPhoneId(phoneId);

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
    public void setNavVisibility(){
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

    /** This method takes the user back to the previous page*/
    public void backButtonClicked(View v){
        finish();
    }
}
