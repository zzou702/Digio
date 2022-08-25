package com.example.team7_project_1;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team7_project_1.handlers.MapHandler;
import com.example.team7_project_1.mappers.DataMapper;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.models.SpecificationDatabaseType;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class LoadingActivity extends AppCompatActivity {

    //categories to be chosen
    public SearchActivity.Category category;
    private boolean is_fetch_phones_complete, is_fetch_specifications_complete = false;

    /** View holder class*/
    private class ViewHolder{
        ProgressBar phone_load_progressbar;
        TextView action_bar_title;
        ImageButton action_bar_back_button;

        public ViewHolder(){
            phone_load_progressbar = findViewById(R.id.phone_load_progressBar);
        }
    }

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        vh = new ViewHolder();

        // Action bar
        initialiseActionBar();

        // Fetch and store data from Firestore
        MapHandler.initializePhoneMapper("phones");
        MapHandler.initializeSpecificationMapper("specifications");
        MapHandler.beginFetch();

//        synchronized (MapHandler.wait_obj) {
//            try {
//                MapHandler.wait_obj.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            finishLoading();
//            Log.i("test", "=============== FINISHED");
//        }
    }

    /**
     * This method initialises the action bar using a customer layout
     * */
    public void initialiseActionBar(){
        // Use the customer layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        vh.action_bar_title.setText("DIGIO");

        // Setting the back button to be invisible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.GONE);
    }

    public void finishLoading() {
        DataProvider.parsePhoneSpecifications();
        vh.phone_load_progressbar.setVisibility(View.INVISIBLE);

        finish();
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
    }
}
