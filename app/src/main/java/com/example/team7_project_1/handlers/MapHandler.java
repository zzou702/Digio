package com.example.team7_project_1.handlers;

import com.example.team7_project_1.LoadingActivity;
import com.example.team7_project_1.mappers.PhoneMapper;
import com.example.team7_project_1.mappers.SpecificationMapper;

public class MapHandler {
    /*
    MapHandler serves as middleware between Application/Activity layer and Persistence/Data layer
     */

    static PhoneMapper phoneMapper;
    static SpecificationMapper specificationMapper;
    static LoadingActivity loadingActivity;

    public static void setContextToCallback(LoadingActivity loadingActivity) {
        MapHandler.loadingActivity = loadingActivity;
    }

    public static void initializePhoneMapper(String collection_path) {
        phoneMapper = new PhoneMapper(collection_path);
    }

    public static void initializeSpecificationMapper(String collection_path) {
        specificationMapper = new SpecificationMapper(collection_path);
    }

    public static void beginFetch() {
        phoneMapper.fetchFromDatabase();
        specificationMapper.fetchFromDatabase();
    }

    /*
    Called by mappers when they have finished fetching
     */
    public static void callbackFromFetch() {
        if (phoneMapper.isStillFetching() || specificationMapper.isStillFetching()) {
            return;
        }
        loadingActivity.finishLoading();
    }
}
