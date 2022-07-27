package com.example.team7_project_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    //categories to be chosen
    public ListActivity.Category cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //this method takes the user to the category page when each of the category buttons are pressed
    public void categoryButtonPressed(View v){
        switch(v.getId())
        {
            case R.id.category_btn_android: //if android button is pressed
                cat = ListActivity.Category.ANDROID;

                // directing to the new activity(ListActivity) and passing the category selected to
                // the new activity via putExtra()
                Intent androidIntent = new Intent(MainActivity.this, ListActivity.class);
                androidIntent.putExtra("CATEGORY_CHOSEN", cat);
                startActivity(androidIntent);
                break;
            case R.id.category_btn_ios: //if ios button is pressed
                cat = ListActivity.Category.IOS;
                Intent iosIntent = new Intent(MainActivity.this, ListActivity.class);
                iosIntent.putExtra("CATEGORY_CHOSEN", cat);
                startActivity(iosIntent);
                break;
            case R.id.category_btn_other: // if OTHER button is pressed
                cat = ListActivity.Category.OTHER;
                Intent otherIntent = new Intent(MainActivity.this, ListActivity.class);
                otherIntent.putExtra("CATEGORY_CHOSEN", cat);
                startActivity(otherIntent);
                break;
        }

    }


}
