package com.example.team7_project_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class ListActivity extends AppCompatActivity {

    private Category chosenCat; //the chosen category

    /** Represents the type of category the user selected */
    public enum Category {
        ANDROID,
        IOS,
        OTHER,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //getting the chosen category that has been passed through the putExtra() method
        chosenCat = (Category) getIntent().getSerializableExtra("CATEGORY_CHOSEN");

        //calling the method to populate the ListActivity
        populateList(chosenCat);

        setNavVisibility();
    }

    /** This method populates the ListActivity based on the category chosen in main activity*/
    public void populateList(Category category){
        TextView test = findViewById(R.id.test);

        // Compare to see which category was chosen from the main activity page
        switch (category){
            case ANDROID:
                test.setText("Android was chosen");
            break;
            case IOS:
                test.setText("IOS was chosen");
            break;
            case OTHER:
                test.setText("Other Operating systems was chosen");
            break;
        }
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