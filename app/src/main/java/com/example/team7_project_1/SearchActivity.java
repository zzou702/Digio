package com.example.team7_project_1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
                        startActivity(new Intent(SearchActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_search:
                        return true;
                    case R.id.nav_cart:
                        startActivity(new Intent(SearchActivity.this, CartActivity.class));
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

}
