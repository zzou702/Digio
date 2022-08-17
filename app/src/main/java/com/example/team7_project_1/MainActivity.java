package com.example.team7_project_1;


import androidx.annotation.NonNull;
import static com.google.android.material.textfield.TextInputLayout.*;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.ImageButton;

import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    //categories to be chosen
    public SearchActivity.Category cat;

    /** View holder class*/
    private class ViewHolder{
        BottomNavigationView bottom_navigation_view;
        RecyclerView top_picks_recycler_view;
        ProgressBar phone_load_progressbar;

        public ViewHolder(){
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            top_picks_recycler_view = findViewById(R.id.top_picks_recycler_view);
            phone_load_progressbar = findViewById(R.id.phone_load_progressBar);
        }
    }

    ArrayList<Phone> phones = new ArrayList<Phone>();
    ArrayList<Product> products = new ArrayList<Product>();
    PhoneAdapter adapter;
    ViewHolder vh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();

        // Generating the Top Picks
        generatedTopPicks();

        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();

    }

    public void generatedTopPicks() {
        initializeArrays();
        adapter = new PhoneAdapter(phones, products,this);

        LinearLayoutManager layout_manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.top_picks_recycler_view.setLayoutManager(layout_manager);

        // Attach the adapter to the recyclerview to populate items
        vh.top_picks_recycler_view.setAdapter(adapter);
    }

    public void initializeArrays() {
        for (Product product : DataProvider.getProducts()) {
            if (product.getRating() >= 4.3) {
                this.products.add(product);
            }
        }

        for (Product product: this.products) {
            for (Phone phone: DataProvider.getPhones()) {
                if (product.getSoldPhoneId() == phone.getId()) {
                    this.phones.add(phone);
                }
            }
        }
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
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
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

    /** this method takes the user to the category page when each of the category buttons are pressed */
    public void categoryButtonPressed(View v){
        switch(v.getId())
        {
            case R.id.category_btn_android: //if android button is pressed
                cat = SearchActivity.Category.ANDROID;
                break;
            case R.id.category_btn_ios: //if ios button is pressed
                cat = SearchActivity.Category.IOS;
                break;
            case R.id.category_btn_other: // if OTHER button is pressed
                cat = SearchActivity.Category.OTHER;
                break;
        }

        // directing to the new activity(ListActivity) and passing the category selected to
        // the new activity via putExtra()
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("CATEGORY_CHOSEN", cat);
        startActivity(intent);
    }

    /** This method initialises the navigation item selected for the home page*/
    public void initializeNavItem(){
        //set home selected
        vh.bottom_navigation_view.setSelectedItemId(R.id.nav_home);

        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        return true;
                    case R.id.nav_cart:
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
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
                            vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                        }else{
                            vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}
