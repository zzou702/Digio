package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.models.Phone;

public class ComparisonActivity extends AppCompatActivity {

    int phone1_id, phone2_id;

//    class ViewHolder {
//        ImageView
//
//        public ViewHolder() {
//            recycler_view_phones = (RecyclerView) findViewById(R.id.comparison_recycler_view);
//        }
//    }

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.activity_comparison);

        initializePhones();
    }

    private void initializePhones() {
        phone1_id = (Integer) getIntent().getIntExtra("phone1_id", 1);
        phone2_id = (Integer) getIntent().getIntExtra("phone2_id", 1);
    }

    public void viewPhone1ButtonClicked(View v) {
//        Intent intent = new Intent(context, DetailsActivity.class);
//        intent.putExtra("phone_id", clicked_product.getSoldPhoneId());
//        context.startActivity(intent);
    }
}
