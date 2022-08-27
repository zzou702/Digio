package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.adapters.SpecificationAdapter;
import com.example.team7_project_1.models.IProduct;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Specification;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class ComparisonActivity extends AppCompatActivity {

    /**
     * View Holder Class
     */
    class ViewHolder {
        BottomNavigationView bottom_navigation_view;
        ImageView phone_1_image, phone_2_image;
        TextView phone_1_title, phone_1_subtitle, phone_2_title, phone_2_subtitle, action_bar_title;
        ImageButton action_bar_back_button;
        RecyclerView specs_recycler_view;

        /**
         * Constructor
         */
        public ViewHolder() {
            this.phone_1_image = findViewById(R.id.phone_1_image);
            this.phone_2_image = findViewById(R.id.phone_2_image);
            this.phone_1_title = findViewById(R.id.phone_1_title);
            this.phone_2_title = findViewById(R.id.phone_2_title);
            this.phone_1_subtitle = findViewById(R.id.phone_1_subtitle);
            this.phone_2_subtitle = findViewById(R.id.phone_2_subtitle);
            this.bottom_navigation_view = findViewById(R.id.bottom_nav_bar);
            this.specs_recycler_view = findViewById(R.id.specs_recycler_view);
        }
    }

    ViewHolder vh;
    SpecificationAdapter adapter;
    long product1_id, product2_id;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.activity_comparison);

        // Initialising the ViewHolder
        vh = new ViewHolder();


        // Action bar
        initialiseActionBar();

        // Navigation bar
        initializeNavItem();

        // Generating the specification of the two products
        generateProductSpecsList();

        initializeViewDetails();

    }

    /**
     * Generates the list of specifications for each product which are then used to populated the
     * RecyclerView, and setting the adapter needed in order to achieve this
     */
    public void generateProductSpecsList() {
        initializeProducts();
        setSpecificationAdapter();
    }

    /**
     * Sets the adapter for the RecyclerView
     */
    public void setSpecificationAdapter() {
        // Getting the product's specifications
        ArrayList<ArrayList> all_specs = generateSpecsArrays();

        adapter = new SpecificationAdapter(all_specs.get(0), all_specs.get(1), this);

        // Creating horizontal vertical layout
        LinearLayoutManager layout_manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vh.specs_recycler_view.setLayoutManager(layout_manager);

        // Attaching the adapter to the recyclerView in order to populate it
        vh.specs_recycler_view.setAdapter(adapter);
    }

    /**
     * Generating the specifications of each product
     */
    public ArrayList<ArrayList> generateSpecsArrays() {
        ArrayList<ArrayList> all_specs = new ArrayList<>();
        ArrayList<Specification> specs_product1 = IProduct.getProductById(product1_id).getPhone().getSpecifications();
        ArrayList<Specification> specs_product2 = IProduct.getProductById(product2_id).getPhone().getSpecifications();

        Collections.sort(specs_product1);
        Collections.sort(specs_product2);

        all_specs.add(specs_product1);
        all_specs.add(specs_product2);

        return all_specs;

    }


    /**
     * Initialises the action bar using a customer layout
     */
    public void initialiseActionBar() {
        // Use the custom layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        vh.action_bar_title.setText("Comparison");

        // Setting the back button to be invisible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.VISIBLE);

        // On click listener for the back button
        vh.action_bar_back_button.setOnClickListener(view -> {
            finish();

            // Sliding transition
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }


    /**
     * Initialises the two products that have been selected to be compared
     */
    private void initializeProducts() {
        product1_id = getIntent().getLongExtra("product1_id", 1);
        product2_id = getIntent().getLongExtra("product2_id", 1);
    }


    /**
     * Initializes the details of the two phones selected that is displayed
     */
    private void initializeViewDetails() {
        Phone phone1 = DataProvider.getPhoneById(product1_id);
        Phone phone2 = DataProvider.getPhoneById(product2_id);

        vh.phone_1_image.setImageResource(DataProvider.getPhoneImageResourcesById(product1_id, this)[0]);
        vh.phone_2_image.setImageResource(DataProvider.getPhoneImageResourcesById(product2_id, this)[0]);

        vh.phone_1_title.setText(phone1.getName());
        vh.phone_2_title.setText(phone2.getName());

        vh.phone_1_subtitle.setText(phone1.getSubtitle());
        vh.phone_2_subtitle.setText(phone2.getSubtitle());
    }


    /**
     * Takes the user to details page of product 1
     */
    public void viewPhone1ButtonClicked(View v) {
        gotoDetailsActivity(product1_id);
    }

    /**
     * Takes the user to details page of product 2
     */
    public void viewPhone2ButtonClicked(View v) {
        gotoDetailsActivity(product2_id);
    }

    /**
     * Stars the details activity based on the product selected
     */
    private void gotoDetailsActivity(long product_id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("product_id", product_id);
        startActivity(intent);

        // Fade transition
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    /**
     * Initialises the navigation item selected for the comparison page
     */
    public void initializeNavItem() {
        // Setting ItemSelectedListener
        vh.bottom_navigation_view.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_home:
                    startActivity(new Intent(ComparisonActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                case R.id.nav_search:
                    startActivity(new Intent(ComparisonActivity.this, SearchActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
                case R.id.nav_cart:
                    startActivity(new Intent(ComparisonActivity.this, CartActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return true;
            }

            return false;
        });
    }


}
