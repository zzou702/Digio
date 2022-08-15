package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.team7_project_1.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class DetailsActivity extends AppCompatActivity {

    // images array
    int[] images = {R.drawable.mphapp113111__1, R.drawable.mphapp113111__2, R.drawable.mphapp113111__3, R.drawable.mphapp113111__4};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;

    /** View holder class*/
    private class ViewHolder{
        BottomNavigationView bottomNavigationView;
        ViewPager mViewPager; // creating object of ViewPager

        public ViewHolder(){
            bottomNavigationView = findViewById(R.id.bottom_nav_bar);
            mViewPager = (ViewPager)findViewById(R.id.viewPager);
        }
    }

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vh = new ViewHolder();

        // View pager initialisation
        mViewPagerAdapter = new ViewPagerAdapter(DetailsActivity.this, images);

        // Adding the Adapter to the ViewPager
        vh.mViewPager.setAdapter(mViewPagerAdapter);

        setNavVisibility();
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

    /** This method takes the user back to the previous page*/
    public void backButtonClicked(View v){
        finish();
    }
}
