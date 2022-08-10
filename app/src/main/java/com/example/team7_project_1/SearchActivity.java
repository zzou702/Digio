package com.example.team7_project_1;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
    }

    /** This method takes the user back to the previous page*/
    public void backButtonClicked(View v){
        finish();
    }
}
