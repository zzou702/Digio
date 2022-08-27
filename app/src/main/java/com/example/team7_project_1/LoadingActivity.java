package com.example.team7_project_1;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team7_project_1.handlers.MapHandler;
import com.example.team7_project_1.utilities.DataProvider;


public class LoadingActivity extends AppCompatActivity {

    /**
     *  ViewHolder Class
     */
    private class ViewHolder{
        ProgressBar phone_load_progressbar;
        TextView action_bar_title;
        ImageButton action_bar_back_button;

        /**
         * Constructor
         */
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
        MapHandler.setContextToCallback(this);
        MapHandler.initializePhoneMapper("phones");
        MapHandler.initializeSpecificationMapper("specifications");
        MapHandler.beginFetch();
    }



    /**
     * This method initialises the action bar using a customer layout
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
        vh.action_bar_back_button.setVisibility(View.GONE);
    }



    /**
     * Once we have finished loading all our data from the database then we can move the user to
     * the MainActivity
     */
    public void finishLoading() {
        DataProvider.parsePhoneSpecifications();
        vh.phone_load_progressbar.setVisibility(View.INVISIBLE);

        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
        finish();
    }
}
