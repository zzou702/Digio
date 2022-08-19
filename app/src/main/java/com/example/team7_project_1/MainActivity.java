package com.example.team7_project_1;


import androidx.annotation.NonNull;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


import com.example.team7_project_1.adapters.ViewPagerAdapter;
import com.example.team7_project_1.adapters.PhoneAdapter;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    //categories to be chosen
    public SearchActivity.Category category;

    //array for banners
    int[] banner = {R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3};

    //adapter for the banner images
    ViewPagerAdapter banner_view_pager_adapter;

    /* variables for the banner and timer */
    int current_page = 0; // Keeps track of the current item in the banner
    Timer timer; // Timer for auto sliding for the banner
    final long DELAY_MS = 500;
    final long PERIOD_MS = 5000; // 5 seconds before executing the next task


    /**
     * View holder class
     */
    private class ViewHolder {
        BottomNavigationView bottom_navigation_view;
        RecyclerView top_pics_recycler_view;
        ProgressBar phone_load_progressbar;
        ViewPager banner_view_pager;

        /**
         * Constructor
         */
        public ViewHolder() {
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            top_pics_recycler_view = findViewById(R.id.top_picks_recycler_view);
            phone_load_progressbar = findViewById(R.id.phone_load_progressBar);
            banner_view_pager = findViewById(R.id.banner);
        }
    }

    // Fields
    ArrayList<Phone> phones = new ArrayList<Phone>();
    ArrayList<Product> products = new ArrayList<Product>();
    PhoneAdapter adapter;
    ViewHolder vh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising the viewholder
        vh = new ViewHolder();

        initialiseBanner();

        // Generating the Top Picks
        generatedTopPicks();

        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();
    }

    /**
     * Initializes the banner images with a timer that automatically scrolls indefinitely
     */
    public void initialiseBanner(){
        //setting up the adapter for the banner image
        banner_view_pager_adapter = new ViewPagerAdapter(MainActivity.this, banner);
        vh.banner_view_pager.setAdapter(banner_view_pager_adapter);

        //initialise the last item of the banner
        vh.banner_view_pager.setCurrentItem(current_page, true);


        //setting the timer for the banner image
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                // Getting the current item index
                current_page = vh.banner_view_pager.getCurrentItem();

                // If the current banner image is the last one in the array
                if(current_page == banner.length-1){
                    current_page = 0; // Set the current page to the first item
                }else {
                    current_page++; // Else set to the next page
                }

                //setting the current item for the banner
                vh.banner_view_pager.setCurrentItem(current_page, true);
            }
        };

        //new thread for the timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);

        // Generating the Top Picks
        generatedTopPicks();


        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();
    }

    /**
     * Generates the top picks section of the main activity
     */
    public void generatedTopPicks() {
        initializeArrays();
        adapter = new PhoneAdapter(phones, products,this);

        LinearLayoutManager layout_manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.top_pics_recycler_view.setLayoutManager(layout_manager);

        // Attach the adapter to the recyclerview to populate items
        vh.top_pics_recycler_view.setAdapter(adapter);
    }

    /**
     * Adds the corresponding product and phone to array for later use
     */
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

    /**
     * Creates the top bar menu used for the user to search for phones
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creating menu inflater
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

        // Setting the listener for search bar
        menu.findItem(R.id.search_bar).setOnActionExpandListener(onActionExpandListener);
        SearchView search_view = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        search_view.setQueryHint("Search...");

        // Listening for when the user commits to searching something
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Passing the user search to the SearchActivity
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

    /**
     * Takes the user to the category page depending on what category button is pressed
     */
    public void categoryButtonPressed(View v){
        switch(v.getId())
        {
            case R.id.category_btn_android: //if android button is pressed
                this.category = SearchActivity.Category.ANDROID;
                break;
            case R.id.category_btn_ios: //if ios button is pressed
                this.category = SearchActivity.Category.IOS;
                break;
            case R.id.category_btn_other: // if OTHER button is pressed
                this.category = SearchActivity.Category.OTHER;
                break;
        }

        // directing to the new activity(ListActivity) and passing the category selected to
        // the new activity via putExtra()
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("CATEGORY_CHOSEN", this.category);
        startActivity(intent);
    }

    /**
     * Initialises the navigation item selected for the main page
     */
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

    /**
     * Sets the bottom navigation bar visible or invisible depending on whether the keyboard is
     * activated
     */
    public void setNavVisibility(){
        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean is_open) {
                        if(is_open){
                            vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                        }else{
                            vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}
