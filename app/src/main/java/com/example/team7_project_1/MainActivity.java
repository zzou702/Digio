package com.example.team7_project_1;


import androidx.annotation.NonNull;
import static com.google.android.material.textfield.TextInputLayout.*;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.ImageButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {

    //categories to be chosen
    public ListActivity.Category cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeNavItem();

//        TextInputEditText search_bar = (TextInputEditText) findViewById(R.id.search_result);
//
//
//        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                // If the user presses enter we want to switch activities
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    // clearing the text bar
//                    search_bar.getText().clear();
//                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
//                    startActivity(searchIntent);
//                    return true;
//                }
//                return false;
//            }
//        });
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
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setQueryHint("Search...");

        return true;
    }

    //this method takes the user to the category page when each of the category buttons are pressed
    public void categoryButtonPressed(View v){
        switch(v.getId())
        {
            case R.id.category_btn_android: //if android button is pressed
                cat = ListActivity.Category.ANDROID;
                break;
            case R.id.category_btn_ios: //if ios button is pressed
                cat = ListActivity.Category.IOS;
                break;
            case R.id.category_btn_other: // if OTHER button is pressed
                cat = ListActivity.Category.OTHER;
                break;
        }

        // directing to the new activity(ListActivity) and passing the category selected to
        // the new activity via putExtra()
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("CATEGORY_CHOSEN", cat);
        startActivity(intent);

    }

    /** This method initialises the navigation item selected for the home page*/
    public void initializeNavItem(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //setting ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
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
}
