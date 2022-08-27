package com.example.team7_project_1;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.team7_project_1.handlers.MapHandler;
import com.example.team7_project_1.models.Category;
import com.example.team7_project_1.utilities.DataProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class LoadingActivity extends AppCompatActivity {

    private StorageReference storage_ref;
    public static Uri[] uri_array;

    /** View holder class*/
    private class ViewHolder{
        ProgressBar phone_load_progressbar;
        TextView action_bar_title;
        ImageButton action_bar_back_button;

        public ViewHolder(){
            phone_load_progressbar = findViewById(R.id.phone_load_progressBar);
        }
    }

    ViewHolder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        vh = new ViewHolder();

        // Action bar
        initialiseActionBar();

        // Fetch and store data from Firestore
        MapHandler.setContextToCallback(this);
        MapHandler.initializePhoneMapper("phones");
        MapHandler.initializeSpecificationMapper("specifications");
        MapHandler.beginFetch();

        /*
        StorageReference[] ref = loadAllImages(this);
        int index = 0;
        for(StorageReference image : ref){
            image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    uri_array[index] = uri;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(LoadingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    /**
     * This method initialises the action bar using a customer layout
     * */
    public void initialiseActionBar(){
        // Use the customer layout for the action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        // Get the custom view and title id to set title suitable for the current page
        vh.action_bar_title = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        vh.action_bar_title.setText("DIGIO");

        // Setting the back button to be invisible
        vh.action_bar_back_button = getSupportActionBar().getCustomView().findViewById(R.id.action_bar_back_button);
        vh.action_bar_back_button.setVisibility(View.GONE);
    }

    public void finishLoading() {
        DataProvider.parsePhoneSpecifications();
        vh.phone_load_progressbar.setVisibility(View.INVISIBLE);

        finish();
        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
    }

    /*
    public StorageReference[] loadAllImages(Context context){
        StorageReference[] images = new StorageReference[117];

        //storage_ref = FirebaseStorage.getInstance().getReference("phone_images/" + Long.toString(phone_id) + "_" + Integer.toString(image_index+1) + "_medium.jpeg");

        for (int all_index = 0; all_index<117; all_index++) {
            for (int image_index = 0; image_index < 4; image_index++) {
                storage_ref = FirebaseStorage.getInstance().getReference("phone_images/p"
                        + (all_index+1) + "_" + (image_index + 1) + "_medium.jpeg");

                //images[image_index] = storage_ref;
                File local_file = null;

                try {
                    local_file = File.createTempFile("tempfile", "jpeg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int finalImage_index = image_index;
                storage_ref.getFile(local_file)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                images[finalImage_index] = storage_ref;

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to retrieve image from firebase storage", Toast.LENGTH_SHORT);

                            }
                        });

            }
        }
        return images;
    }*/
}
