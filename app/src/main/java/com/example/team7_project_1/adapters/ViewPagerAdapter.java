package com.example.team7_project_1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.Objects;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.team7_project_1.DetailsActivity;
import com.example.team7_project_1.MainActivity;
import com.example.team7_project_1.R;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class ViewPagerAdapter extends PagerAdapter {

    // reference: https://www.digitalocean.com/community/tutorials/android-viewpager-example-tutorial

    Context context;
    //int[] images;
    StorageReference[] images;
    LayoutInflater m_layout_inflater;

    /*
    public ViewPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        m_layout_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    public ViewPagerAdapter(Context context, StorageReference[] images) {
        this.context = context;
        this.images = images;
        m_layout_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the pager_item.xml
        View item_view = m_layout_inflater.inflate(R.layout.pager_item, container, false);

        // referencing the image view from the pager_item.xml file
        ImageView image_view = (ImageView) item_view.findViewById(R.id.imageViewMain);

        // setting the image in the imageView
        //image_view.setImageResource(images[position]);


        images[position].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .into(image_view);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        // Adding the View
        Objects.requireNonNull(container).addView(item_view);


        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}