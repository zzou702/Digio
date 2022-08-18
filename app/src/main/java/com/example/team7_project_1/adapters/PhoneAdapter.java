package com.example.team7_project_1.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.ComparisonActivity;
import com.example.team7_project_1.ComparisonFilterActivity;
import com.example.team7_project_1.DataProvider;
import com.example.team7_project_1.DetailsActivity;
import com.example.team7_project_1.MainActivity;
import com.example.team7_project_1.R;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.models.Product;

import java.util.ArrayList;
import java.util.Locale;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {

    // To make your view item clickable ensure that the view holder class implements View.OnClickListener and it has the onClick(View v) method.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Declare objects of all the views to be manipulated in item_contact.xml
        public TextView phone_subtitle;
        public TextView phone_name;
        public ImageView phone_main_image;
        public TextView phone_price;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            // Initialize the view objects
            this.phone_name = v.findViewById(R.id.phone_name);
            this.phone_main_image = v.findViewById(R.id.phone_main_image);
            this.phone_price = v.findViewById(R.id.phone_price);
            this.phone_subtitle = v.findViewById(R.id.phone_subtitle);
        }

        @Override
        public void onClick(View v) {
            // What to do when the view item is clicked
            Phone clicked_phone = phones.get(getAdapterPosition());

            Context current_context = v.getContext();

            if (current_context instanceof ComparisonFilterActivity) {
                Intent intent = new Intent(context, ComparisonActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("first_phone_id", ComparisonFilterActivity.getPhoneID());
                extras.putInt("second_phone_id", clicked_phone.getId());
                intent.putExtras(extras);
                Toast.makeText(context, clicked_phone.getBrand() + " is clicked in position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, DetailsActivity.class);
                Bundle extras = new Bundle();
                intent.putExtra("phone_id", clicked_phone.getId());
                Toast.makeText(context, clicked_phone.getBrand() + " is clicked in position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }

        }
    }

    // Declare the data collection object that holds the data to be populated in the RecyclerView
    private ArrayList<Phone> phones;
    private ArrayList<Product> products;
    private Context context;

    public PhoneAdapter(ArrayList<Phone> phones, ArrayList<Product> products, Context context) {
        this.phones = phones;
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public PhoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View phone_view;

        // If the current context is main
        if(this.context instanceof MainActivity) {
            // Inflate the custom layout for the top picks items
            phone_view = inflater.inflate(R.layout.top_picks_items, parent, false);
        }else {
            // Inflate the custom layout for the listview/recycleView items
            phone_view = inflater.inflate(R.layout.phone_list_view_item, parent, false);
        }

        // Return a new holder instance
        ViewHolder holder = new ViewHolder(phone_view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter. ViewHolder holder, int position) {
        Phone this_phone = phones.get(position);
        Product equivalent_product = getProductByPhoneId(this_phone.getId());

        holder.phone_name.setText(equivalent_product.getName());
        holder.phone_subtitle.setText(this_phone.getSubtitle());
        holder.phone_price.setText(String.format(Locale.getDefault(), "$%.2f",equivalent_product.getPrice()));

        int image = DataProvider.getPhoneImageResourcesById(this_phone.getId(), this.context)[0];
        holder.phone_main_image.setImageResource(image);
    }

    public Product getProductByPhoneId(int phoneId) {
        for (Product product : products) {
            if (product.getSoldPhoneId() == phoneId) {
                return product;
            }
        }
        // could not find product of phoneId
        return null;
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }
}
