package com.example.team7_project_1.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.example.team7_project_1.models.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {

    // To make your view item clickable ensure that the view holder class implements View.OnClickListener and it has the onClick(View v) method
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Declare objects of all the views to be manipulated
        public TextView phone_subtitle;
        public TextView phone_name;
        public ImageView phone_main_image;
        public TextView phone_price;

        /**
         * Constructor
         */
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
            Product clicked_product = products.get(getAdapterPosition());

            Context current_context = v.getContext();

            // If the context is an instance of ComparisonFilterActivity we want to go to the
            // comparison activity, otherwise we want to go to the details activity.
            if (current_context instanceof ComparisonFilterActivity) {
                Intent intent = new Intent(context, ComparisonActivity.class);
                Bundle extras = new Bundle();
                // Passing the required phone IDs for comparison
                extras.putInt("first_phone_id", ComparisonFilterActivity.getPhoneID());
                extras.putInt("second_phone_id", clicked_product.getSoldPhoneId());
                intent.putExtras(extras);
                Toast.makeText(context, clicked_product.getSoldPhone().getBrand() + " is clicked in position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, DetailsActivity.class);
                Bundle extras = new Bundle();
                intent.putExtra("phone_id",  clicked_product.getSoldPhoneId());
                Toast.makeText(context, clicked_product.getSoldPhone().getBrand() + " is clicked in position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }

        }
    }

    // Fields
    private ArrayList<Product> products;
    private Context context;

    /** Constructor */
    public PhoneAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public PhoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View phone_view;

        // If the current context is an instance of MainActivity then we want to use the
        // top_picks_items, otherwise we use the phone_list_view_item.
        if(this.context instanceof MainActivity) {
            // Inflate the custom layout for the top picks items
            phone_view = inflater.inflate(R.layout.top_picks_items, parent, false);
        }else {
            // Inflate the custom layout for the recycleView items
            phone_view = inflater.inflate(R.layout.phone_list_view_item, parent, false);
        }

        // Create ViewHolder
        ViewHolder holder = new ViewHolder(phone_view);

        return holder;
    }

    /**
     * Changes the views in the given holder according to the phone and product information in the
     * given position in the RecyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter. ViewHolder holder, int position) {
        // Getting the phone and product associated with it in the given position in the RecyclerView
        Product this_product = products.get(position);

        holder.phone_name.setText(this_product.getName());
        holder.phone_subtitle.setText(this_product.getSoldPhone().getSubtitle());
        holder.phone_price.setText(String.format(Locale.getDefault(), "$%.2f",this_product.getPrice()));

        int image = DataProvider.getPhoneImageResourcesById(this_product.getSoldPhoneId(), this.context)[0];
        holder.phone_main_image.setImageResource(image);
    }


    @Override
    public int getItemCount() {
        return products.size();
    }


//    public Filter getFilter() {
//        return filter;
//    }
//
//    Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence char_sequence) {
//            ArrayList<Product> filtered_products = new ArrayList<>();
//
//            if (char_sequence.toString().isEmpty()) {
//                filtered_products.addAll(all_products);
//            } else {
//                for (Product product : all_products) {
//                    if ((product.getName().equalsIgnoreCase(char_sequence.toString().toLowerCase())) ||
//                            (product.getName().toLowerCase().contains(char_sequence.toString().toLowerCase()))) {
//                        filtered_products.add(product);
//                    }
//                }
//            }
//
//            FilterResults filter_results = new FilterResults();
//            filter_results.values = filtered_products;
//            return filter_results;
//        }
//
//
//        @Override
//        protected void publishResults(CharSequence char_sequence, FilterResults filter_results) {
//            products.clear();
//            products.addAll((Collection<? extends Product>) filter_results.values);
//            notifyDataSetChanged();
//        }
//    };
}
