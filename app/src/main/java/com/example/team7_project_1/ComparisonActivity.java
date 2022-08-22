package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.models.Phone;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class ComparisonActivity extends AppCompatActivity {

    int phone1_id, phone2_id;

    class ViewHolder {
        BottomNavigationView bottom_navigation_view;
        ImageView phone_1_image, phone_2_image;
        TextView phone_1_title, phone_1_subtitle, phone_2_title, phone_2_subtitle;

        public ViewHolder() {
            phone_1_image = findViewById(R.id.phone_1_image);
            phone_2_image = findViewById(R.id.phone_2_image);
            phone_1_title = findViewById(R.id.phone_1_title);
            phone_2_title = findViewById(R.id.phone_2_title);
            phone_1_subtitle = findViewById(R.id.phone_1_subtitle);
            phone_2_subtitle = findViewById(R.id.phone_2_subtitle);
            bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
        }
    }

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.activity_comparison);

        vh = new ViewHolder();

        initializeNavItem();

        initializePhones();
        initializeViewDetails();
    }

    private void initializePhones() {
        phone1_id = getIntent().getIntExtra("phone1_id", 1);
        phone2_id = getIntent().getIntExtra("phone2_id", 1);
    }

    private void initializeViewDetails() {
        Phone phone1 = DataProvider.getPhoneById(phone1_id);
        Phone phone2 = DataProvider.getPhoneById(phone2_id);

        vh.phone_1_image.setImageResource(DataProvider.getPhoneImageResourcesById(phone1_id, this)[0]);
        vh.phone_2_image.setImageResource(DataProvider.getPhoneImageResourcesById(phone2_id, this)[0]);

        vh.phone_1_title.setText(phone1.getName());
        vh.phone_2_title.setText(phone2.getName());

        vh.phone_1_subtitle.setText(phone1.getSubtitle());
        vh.phone_2_subtitle.setText(phone2.getSubtitle());
    }

    public void viewPhone1ButtonClicked(View v) {
        gotoDetailsActivity(phone1_id);
    }
    public void viewPhone2ButtonClicked(View v) {
        gotoDetailsActivity(phone2_id);
    }
    private void gotoDetailsActivity(int phone_id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("phone_id", phone_id);
        startActivity(intent);
    }

    /**
     * Initialises the navigation item selected for the comparison page
     */
    public void initializeNavItem() {
        //setting ItemSelectedListener
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(ComparisonActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(ComparisonActivity.this, SearchActivity.class));
                        return true;
                    case R.id.nav_cart:
                        startActivity(new Intent(ComparisonActivity.this, CartActivity.class));
                        return true;
                }

                return false;
            }
        });
    }


}
