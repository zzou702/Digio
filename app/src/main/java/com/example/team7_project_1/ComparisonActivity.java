package com.example.team7_project_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.models.Phone;

public class ComparisonActivity extends AppCompatActivity {

    int phone1_id, phone2_id;

    class ViewHolder {
        ImageView phone_1_image, phone_2_image;
        TextView phone_1_title, phone_1_subtitle, phone_2_title, phone_2_subtitle;

        public ViewHolder() {
            phone_1_image = findViewById(R.id.phone_1_image);
            phone_2_image = findViewById(R.id.phone_2_image);
            phone_1_title = findViewById(R.id.phone_1_title);
            phone_2_title = findViewById(R.id.phone_2_title);
            phone_1_subtitle = findViewById(R.id.phone_1_subtitle);
            phone_2_subtitle = findViewById(R.id.phone_2_subtitle);
        }
    }

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.activity_comparison);

        vh = new ViewHolder();

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
}
