package com.example.team7_project_1.handlers;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team7_project_1.DataProvider;
import com.example.team7_project_1.LoadingActivity;
import com.example.team7_project_1.MainActivity;
import com.example.team7_project_1.mappers.PhoneMapper;
import com.example.team7_project_1.mappers.SpecificationMapper;

public class MapHandler {
    public static final Object wait_obj = new Object();
    static PhoneMapper phoneMapper;
    static SpecificationMapper specificationMapper;

    static AppCompatActivity requestingActivity;

    public static void initializePhoneMapper(String collection_path) {
        phoneMapper = new PhoneMapper(collection_path);
    }

    public static void initializeSpecificationMapper(String collection_path) {
        specificationMapper = new SpecificationMapper(collection_path);
    }

    public static void setEndActivity() {

    }

    public static void beginFetch() {
        phoneMapper.fetchFromDatabase();
        specificationMapper.fetchFromDatabase();
    }

    public static void callbackFromFetch() {
        Log.i("test", "=============== called");
        synchronized (wait_obj) {
            // Check if both fetch tasks are complete
            if (phoneMapper.isStillFetching() || specificationMapper.isStillFetching()) {
                return;
            }
            wait_obj.notify();
        }
    }
}
