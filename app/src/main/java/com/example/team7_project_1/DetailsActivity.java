package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.team7_project_1.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailsActivity extends AppCompatActivity {

    // creating object of ViewPager
    ViewPager mViewPager;

    // images array
    int[] images = {R.drawable.mphapp113111__1, R.drawable.mphapp113111__2, R.drawable.mphapp113111__3, R.drawable.mphapp113111__4};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // View pager initialisation
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPagerAdapter = new ViewPagerAdapter(DetailsActivity.this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    /** This method takes the user back to the previous page*/
    public void backButtonClicked(View v){
        finish();
    }
}
