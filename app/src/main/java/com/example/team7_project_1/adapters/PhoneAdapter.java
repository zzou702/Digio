package com.example.team7_project_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.R;
import com.example.team7_project_1.models.Phone;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {

    // To make your view item clickable ensure that the view holder class implements View.OnClickListener and it has the onClick(View v) method.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Declare objects of all the views to be manipulated in item_contact.xml
        public TextView phone_name;
        public ImageView phone_main_image;
        public TextView phone_price;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            // Initialize the view objects
            phone_name = v.findViewById(R.id.phone_name);
            phone_main_image = v.findViewById(R.id.phone_main_image);
            phone_price = v.findViewById(R.id.phone_price);
        }

        @Override
        public void onClick(View v) {
            // What to do when the view item is clicked
            Phone clicked_phone = phones.get(getAdapterPosition());
            Toast.makeText(search_context, clicked_phone.getBrand() + " is clicked in position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    // Declare the data collection object that holds the data to be populated in the RecyclerView
    private List<Phone> phones;
    private Context search_context;

    public PhoneAdapter(Context context, ArrayList<Phone> phones) {
        this.search_context = context;
        this.phones = phones;
    }



    @NonNull
    @Override
    public PhoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  inflater = LayoutInflater.from(search_context);

        // Inflate the custom layout
        View phone_view = inflater.inflate(R.layout.phone_list_view_item, parent, false);

        // Return a new holder instance
        ViewHolder holder = new ViewHolder(phone_view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter. ViewHolder holder, int position) {
        Phone this_phone = phones.get(position);
        holder.phone_name.setText(this_phone.getBrand());
        holder.phone_price.setText(Integer.toString(this_phone.getPrice()) + "$");
        holder.phone_main_image.setImageResource(R.drawable.ic_home);
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }
}
