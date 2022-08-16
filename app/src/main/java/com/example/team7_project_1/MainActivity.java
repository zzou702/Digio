package com.example.team7_project_1;


import androidx.annotation.NonNull;
import static com.google.android.material.textfield.TextInputLayout.*;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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

import com.example.team7_project_1.models.Phone;
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


public class MainActivity extends AppCompatActivity {

    //categories to be chosen
    public ListActivity.Category cat;

    /** View holder class*/
    private class ViewHolder{
        BottomNavigationView bottomNavigationView;
        ProgressBar phoneLoadProgressBar;

        public ViewHolder(){
            bottomNavigationView = findViewById(R.id.bottom_nav_bar);
            phoneLoadProgressBar = findViewById(R.id.phone_load_progressBar);
        }
    }

    ViewHolder vh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();

        fetchPhoneData();

        initializeNavItem();
        setNavVisibility();
    }

    public void fetchPhoneData() {
        ArrayList<Phone> phoneList = new ArrayList<Phone>();

        // Getting phone collection from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("phones").get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("i", "onSuccess: LIST EMPTY");
                    } else {
                        List<DocumentSnapshot> list = documentSnapshots.getDocuments();
                        for (DocumentSnapshot document : list) {
                            // after getting this list we are passing
                            // that list to our object class.

                            Map<String,Object> data = document.getData();

                            // int id, String name, String subtitle, String operatingSystem, String brand, String manufacturerPartNo
                            Phone phone = new Phone();
                            phone.parseSpecifications(data.get("specifications"));

                            // works
                            Log.i("test", document.getData().toString());
                            // fails: Could not deserialize object. Expected a List, but got a class java.util.HashMap
                            // https://stackoverflow.com/questions/55694354/expected-a-list-while-deserializing-but-got-a-class-java-util-hashmap
//                            phone = document.toObject(Phone.class);
//
//                            // and we will pass this object class
//                            // inside our arraylist which we have
//                            // created for recycler view.
//                            phoneList.add(phone);
                        }
                        Log.d("i", "onSuccess: " + phoneList);
                        vh.phoneLoadProgressBar.setVisibility(View.GONE);
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
        //set home selected
        vh.bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //setting ItemSelectedListener
        vh.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
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
                            vh.bottomNavigationView.setVisibility(View.INVISIBLE);
                        }else{
                            vh.bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }
}
