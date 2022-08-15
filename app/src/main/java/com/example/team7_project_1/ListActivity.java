package com.example.team7_project_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private Category chosenCat; //the chosen category

    /** Represents the type of category the user selected */
    public enum Category {
        ANDROID,
        IOS,
        OTHER,
    }

    /** View holder class*/
    private class ViewHolder{
        BottomNavigationView bottomNavigationView;
        TextView test;
        RecyclerView recycler_view_phones;
        ArrayList<Phone> phones;

        public ViewHolder(){
            bottomNavigationView = findViewById(R.id.bottom_nav_bar);
            test = findViewById(R.id.test);
            recycler_view_phones = (RecyclerView) findViewById(R.id.recycler_view);
            phones = DataProvider.generateData();

        }
    }

    PhoneAdapter adapter;
    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        vh = new ViewHolder();

        adapter = new PhoneAdapter(this, vh.phones);

        //getting the chosen category that has been passed through the putExtra() method
        chosenCat = (Category) getIntent().getSerializableExtra("CATEGORY_CHOSEN");

        //calling the method to populate the ListActivity
        populateList(chosenCat);

        initializeNavItem();
        setNavVisibility();
    }

    /** This method populates the ListActivity based on the category chosen in main activity*/
    public void populateList(Category category){

        // Compare to see which category was chosen from the main activity page
        /**
        switch (category){
            case ANDROID:
                vh.test.setText("Android was chosen");
            break;
            case IOS:
                vh.test.setText("IOS was chosen");
            break;
            case OTHER:
                vh.test.setText("Other Operating systems was chosen");
            break;
        }*/

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        vh.recycler_view_phones.setLayoutManager(gridLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        vh.recycler_view_phones.setAdapter(adapter);
    }


    /** This method initialises the navigation item selected for the home page*/
    public void initializeNavItem(){
        //set home selected
        vh.bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //setting ItemSelectedListener
        vh.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(ListActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(ListActivity.this, SearchActivity.class));
                        return true;
                    case R.id.nav_cart:
                        startActivity(new Intent(ListActivity.this, CartActivity.class));
                        return true;
                }

                return false;
            }
        });
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
}