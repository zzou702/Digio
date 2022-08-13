package com.example.team7_project_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeNavItem();
    }

    /** This method initialises the navigation item selected for the search page*/
    public void initializeNavItem(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        //setting ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
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

    public void detailsButtonClicked(View v) {
        startActivity(new Intent(CartActivity.this, DetailsActivity.class));
    }
}