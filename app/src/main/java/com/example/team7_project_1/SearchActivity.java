package com.example.team7_project_1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;


import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recycler_view_phones;
    ArrayList<Phone> phones;
    PhoneAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recycler_view_phones = (RecyclerView) findViewById(R.id.recycler_view);

        phones = DataProvider.generateData();

        // Getting what the user searched
        String user_search = getIntent().getStringExtra("user_search");


        adapter = new PhoneAdapter(this, phones);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler_view_phones.setLayoutManager(gridLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        recycler_view_phones.setAdapter(adapter);

        initializeNavItem();
        setNavVisibility();

    }

    /** This method initialises the navigation item selected for the search page*/
    public void initializeNavItem() {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        menu.findItem(R.id.search_bar).setOnActionExpandListener(onActionExpandListener);
        SearchView search_view = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        search_view.setQueryHint("Search...");

        // Listening for when the user commits to searching something
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
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
    
    /** This method takes the user back to the previous page*/
    public void backButtonClicked(View v){
        finish();
    }

    /** This method sets the bottom navigation bar visible or invisible depending on whether the
     *  keyboard is activated */
    public void setNavVisibility(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            bottomNavigationView.setVisibility(View.INVISIBLE);
                        }else{
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}


