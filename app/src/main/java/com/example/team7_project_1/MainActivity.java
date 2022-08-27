package com.example.team7_project_1;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


import com.example.team7_project_1.adapters.ViewPagerAdapter;
import com.example.team7_project_1.adapters.ProductAdapter;
import com.example.team7_project_1.models.Category;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    /**
     * ViewHolder Class
     */
    private class ViewHolder {
        BottomNavigationView bottom_navigation_view;
        RecyclerView top_picks_recycler_view;
        ProgressBar phone_load_progressbar;
        ViewPager banner_view_pager;
        TextView action_bar_title;
        ImageButton action_bar_back_button;

        /**
         * Constructor
         */
        public ViewHolder() {
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            top_picks_recycler_view = findViewById(R.id.top_picks_recycler_view);
            phone_load_progressbar = findViewById(R.id.phone_load_progressBar);
            banner_view_pager = findViewById(R.id.banner);
        }
    }

    // Fields
    ArrayList<Product> products = new ArrayList<Product>();
    ProductAdapter adapter;
    ViewHolder vh;
    // Categories to be chosen
    public Category.Names category;

    // Array for banners
    int[] banner = {R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3};
    // Adapter for the banner images
    ViewPagerAdapter banner_view_pager_adapter;

    /* Variables for the banner and timer */
    int current_page = 0; // Keeps track of the current item in the banner
    Timer timer; // Timer for auto sliding for the banner
    final long DELAY_MS = 500;
    final long PERIOD_MS = 5000; // 5 seconds before executing the next task

    int NUM_TOP_PICKS = 8; // Number of products in the top picks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising the ViewHolder
        vh = new ViewHolder();

        // Action Bar
        initialiseActionBar();

        // Banner
        initialiseBanner();

        // Generating the top picks
        generatedTopPicks();

        // Setup navigation bar
        initializeNavItem();
        setNavVisibility();
    }



    /**
     * This method initialises the action bar using a custom layout
     */
    public void initialiseActionBar() {
        // Use the customer layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        vh.action_bar_title.setText("DIGIO");

        // Setting the back button to be invisible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.INVISIBLE);
    }



    /**
     * Initializes the banner images with a timer that automatically scrolls indefinitely
     */
    public void initialiseBanner() {
        // Setting up the adapter for the banner image
        banner_view_pager_adapter = new ViewPagerAdapter(MainActivity.this, banner);
        vh.banner_view_pager.setAdapter(banner_view_pager_adapter);

        // Initialise the last item of the banner
        vh.banner_view_pager.setCurrentItem(current_page, true);


        // Setting the timer for the banner image
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                // Getting the current item index
                current_page = vh.banner_view_pager.getCurrentItem();

                // If the current banner image is the last one in the array set the current page to
                // the first page. Otherwise set it to the next page.
                if(current_page == banner.length-1) {
                    current_page = 0;
                }else {
                    current_page++;
                }

                // Setting the current item for the banner
                vh.banner_view_pager.setCurrentItem(current_page, true);
            }
        };

        // New thread for the timer
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }



    /**
     * Generates the top picks product list used to populated the RecyclerView, and setting the adapter needed
     * in order to achieve this
     */
    public void generatedTopPicks() {
        initializeArray();
        setProductAdapter();
    }



    /**
     * Sets the adapter for the RecyclerView
     */
    public void setProductAdapter() {
        adapter = new ProductAdapter(products,this);

        // Creating horizontal linear layout
        LinearLayoutManager layout_manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.top_picks_recycler_view.setLayoutManager(layout_manager);

        // Attaching the adapter to the recyclerView in order to populate it
        vh.top_picks_recycler_view.setAdapter(adapter);
    }



    /**
     * Adds the corresponding product and phone to array for later use
     */
    public void initializeArray() {
        ArrayList<Product> all_products = DataProvider.getProducts();
        Collections.sort(all_products);

        for(int i = 0; i < NUM_TOP_PICKS; i++) {
            this.products.add(all_products.get(i));
        }
    }


    /**
     * Creates the top bar menu used for the user to search for phones
     * @param menu
     * @return boolean (true/false)
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
     * @param v
     */
    public void categoryButtonPressed(View v) {
        switch(v.getId())
        {
            case R.id.category_btn_android: //if android button is pressed
                this.category = Category.Names.ANDROID;
                break;
            case R.id.category_btn_ios: //if ios button is pressed
                this.category = Category.Names.IOS;
                break;
            case R.id.category_btn_other: // if OTHER button is pressed
                this.category = Category.Names.OTHER;
                break;
        }

        // directing to the new activity(ListActivity) and passing the category selected to
        // the new activity via putExtra()
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("CATEGORY_CHOSEN", this.category);
        startActivity(intent);

        // transition
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }



    /**
     * Initialises the navigation item selected for the main page
     */
    public void initializeNavItem() {
        //set home selected
        vh.bottom_navigation_view.setSelectedItemId(R.id.nav_home);

        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_home:
                    return true;
                case R.id.nav_search:
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.nav_cart:
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }

            return false;
        });
    }



    /**
     * Sets the bottom navigation bar visible or invisible depending on whether the keyboard is
     * activated
     */
    public void setNavVisibility() {
        // listens to the keyboard, if the keyboard is opened, set the bottom nav bar invisible
        KeyboardVisibilityEvent.setEventListener(
                this,
                is_open -> {
                    if(is_open){
                        vh.bottom_navigation_view.setVisibility(View.INVISIBLE);
                    }else{
                        vh.bottom_navigation_view.setVisibility(View.VISIBLE);
                    }
                }
        );
    }


    /**
     * This method takes the user to the website on the browser if the user clicks on one
     * of the news
     * @param v
     */
    public void newsClicked(View v) {
        // Default website
        String url_string = "https://www.google.com/";

        // Conditional statement to check which item is selected
        int view_id = v.getId();

        // Switch statement does not work, duplicate case: R.ids not const at compile time
        if (view_id == R.id.news_1 | view_id == R.id.news_text_1) {
            url_string = "https://9to5mac.com/2022/08/19/iphone-14-news/";
        } else if (view_id == R.id.news_2 | view_id == R.id.news_text_2) {
            url_string = "https://www.samsung.com/nz/news/local/seize-the-night-with-the-" +
                    "samsung-galaxy-s22-ultra/";
        } else if (view_id == R.id.news_3 | view_id == R.id.news_text_3) {
            url_string = "https://www.theverge.com/23278136/xiaomi-12s-ultra-camera-leica-" +
                    "versus-pixel-6-pro-galaxy-s22-iphone-13-max";
        }else if (view_id == R.id.news_4 | view_id == R.id.news_text_4) {
            url_string = "https://www.imore.com/iphone/iphone-14/iphone-14-price-leak-could-" +
                    "be-great-news-ahead-of-launch";
        }

        // Directing the user to an external browser based on the selected news
        Intent browser_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_string));
        startActivity(browser_intent);
    }
}
