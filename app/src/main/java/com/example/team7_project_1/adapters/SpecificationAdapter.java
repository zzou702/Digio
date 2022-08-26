package com.example.team7_project_1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team7_project_1.ComparisonActivity;
import com.example.team7_project_1.R;
import android.content.Context;
import com.example.team7_project_1.models.Product;
import com.example.team7_project_1.models.Specification;

import java.util.ArrayList;

public class SpecificationAdapter extends RecyclerView.Adapter<SpecificationAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        // Declare objects of all the views to be manipulated
        public TextView spec_title;
        public TextView spec_value_product1;
        public TextView spec_value_product2;

        /**
         * Constructor
         */
        public ViewHolder(View v) {
            super(v);

            // Initialize the view objects
            this.spec_title = v.findViewById(R.id.spec_title);
            this.spec_value_product1 = v.findViewById(R.id.spec_value_product1);
            this.spec_value_product2 = v.findViewById(R.id.spec_value_product2);

        }
    }


    // Fields
    private ArrayList<Specification> specs_product1;
    private ArrayList<Specification> specs_product2;
    private Context context;

    /**
     * Constructor
     */
    public SpecificationAdapter(ArrayList<Specification> specs_product1, ArrayList<Specification> specs_product2, Context context) {
        this.specs_product1 = specs_product1;
        this.specs_product2 = specs_product2;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View comparison_view = inflater.inflate(R.layout.spec_comparison_item, parent, false);

        // create ViewHolder
        ViewHolder holder = new ViewHolder(comparison_view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Specification current_spec_product1 = specs_product1.get(position);
        Specification current_spec_product2 = specs_product2.get(position);

        String spec_name = current_spec_product1.getDisplayName();

        String spec_value_product1 = current_spec_product1.getValue();
        String spec_value_product2 = current_spec_product2.getValue();

        if (spec_value_product1 == null) {
            spec_value_product1 = "N/A";
        } else if (spec_value_product2 == null) {
            spec_value_product2 = "N/A";
        }

        holder.spec_title.setText(spec_name);
        holder.spec_value_product1.setText(spec_value_product1);
        holder.spec_value_product2.setText(spec_value_product2);


    }

    /**
     * Gets the number of items
     */
    @Override
    public int getItemCount() {
        if (specs_product1.size() >= specs_product2.size()) {
            return specs_product1.size();
        } else {
            return specs_product2.size();
        }
    }
}
