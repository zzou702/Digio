package com.example.team7_project_1.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.CartActivity;
import com.example.team7_project_1.ComparisonActivity;
import com.example.team7_project_1.ComparisonFilterActivity;
import com.example.team7_project_1.models.Category;
import com.example.team7_project_1.models.Phone;
import com.example.team7_project_1.utilities.DataProvider;
import com.example.team7_project_1.DetailsActivity;
import com.example.team7_project_1.MainActivity;
import com.example.team7_project_1.R;
import com.example.team7_project_1.SearchActivity;
import com.example.team7_project_1.models.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> implements IPhoneAdapter {

    // To make your view item clickable ensure that the view holder class implements View.OnClickListener and it has the onClick(View v) method
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Declare objects of all the views to be manipulated
        public TextView phone_subtitle;
        public TextView phone_name;
        public ImageView phone_main_image;
        public ImageView os_icon;
        public ImageView brand_icon;
        public TextView phone_price;
        public Button remove_from_cart_button;

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
            if (context instanceof SearchActivity || context instanceof ComparisonFilterActivity) {
                this.os_icon = v.findViewById(R.id.os_icon);
                this.brand_icon = v.findViewById(R.id.brand_icon);
            }
            if (context instanceof CartActivity) {
                this.remove_from_cart_button = v.findViewById(R.id.remove_from_cart_button);
                remove_from_cart_button.setOnClickListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            // What to do when the view item is clicked
            Product clicked_product = products.get(getAdapterPosition());

            Context current_context = v.getContext();

            // Context instance of ComparisonFilterActivity - go to the comparison activity
            // Context instance of CartActivity - check if the view is the remove button or the
            // cardView:
            //      - Remove button ---> remove product from the shopping cart
            //      - CardView ---> go to to that product's details activity
            // Otherwise we want to go to the details activity
            if (current_context instanceof ComparisonFilterActivity) {
                Intent intent = new Intent(context, ComparisonActivity.class);
                Bundle extras = new Bundle();
                // Passing the required phone IDs for comparison
                extras.putLong("product1_id", DataProvider.getProductId());
                extras.putLong("product2_id", clicked_product.getId());
                intent.putExtras(extras);

                // Since we already increased the frequency of product1 we must also do the same for product2
                clicked_product.incrementFrequency();
                context.startActivity(intent);
            } else if (current_context instanceof MainActivity || current_context instanceof SearchActivity) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("product_id",  clicked_product.getId());
                context.startActivity(intent);
            } else if (current_context instanceof CartActivity) {
                if (v.equals(remove_from_cart_button)) {
                    removeAt(getAdapterPosition());
                    DataProvider.removeFromShoppingCart(clicked_product.getId());
                    Toast.makeText(context, "Removed from cart!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("product_id", clicked_product.getId());
                    context.startActivity(intent);
                }
            }
        }

        public void removeAt(int position) {
            products.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, products.size());
        }
    }

    // Fields
    private ArrayList<Product> products;
    private ArrayList<Product> products_all;
    private Context context;

    /** Constructor */
    public PhoneAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.products_all = new ArrayList<>(products);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View phone_view;

        // If the current context is an instance of MainActivity then we want to use the
        // top_picks_items, otherwise we use the phone_list_view_item.
        if(this.context instanceof MainActivity) {
            // Inflate the custom layout for the top picks items
            phone_view = inflater.inflate(R.layout.top_picks_items, parent, false);
        }else if (this.context instanceof CartActivity) {
            // Inflate the custom layout for the cart items
            phone_view = inflater.inflate(R.layout.cart_list_view_item, parent, false);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Getting the phone and product associated with it in the given position in the RecyclerView
        Product this_product = products.get(position);
        Phone this_phone = this_product.getPhone();

        holder.phone_name.setText(this_phone.getName());
        holder.phone_subtitle.setText(this_phone.getSubtitle());
        holder.phone_price.setText(String.format(Locale.getDefault(), "$%.2f",this_product.getPrice()));

        int phone_image = DataProvider.getPhoneImageResourcesById(this_phone.getId(), this.context)[0];
        holder.phone_main_image.setImageResource(phone_image);

        // Set OS and brand icons based on phone category

        Category this_category = this_phone.getCategory();

        if (context instanceof SearchActivity || context instanceof ComparisonFilterActivity) {
            holder.os_icon.setImageResource(this_category.getOSImageId(context));
            holder.brand_icon.setImageResource(this_category.getBrandImageId(context));
        }
    }


    @Override
    public int getItemCount() {
        return products.size();
    }


    public Filter getFilter() {
        return filter;
    }

    /**
     * Filters out products dynamically as the user types into the search bar
     */
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence char_sequence) {
            ArrayList<Product> filtered_products = new ArrayList<>();

            // If the user hasn't typed anything then we show all the phones
            if (char_sequence.toString().isEmpty()) {
                filtered_products.addAll(products_all);
            } else {
                for (Product product : products_all) {
                    if ((product.getName().equalsIgnoreCase(char_sequence.toString().toLowerCase())) ||
                            (product.getName().toLowerCase().contains(char_sequence.toString().toLowerCase()))) {
                        filtered_products.add(product);
                    }
                }
            }

            FilterResults filter_results = new FilterResults();
            filter_results.values = filtered_products;
            return filter_results;
        }


        /**
         * Updating the products displayed
         */
        @Override
        protected void publishResults(CharSequence char_sequence, FilterResults filter_results) {
            products.clear();
            products.addAll((Collection<? extends Product>) filter_results.values);
            notifyDataSetChanged();
        }
    };

}
