package com.example.team7_project_1.handlers;

import com.example.team7_project_1.LoadingActivity;
import com.example.team7_project_1.mappers.PhoneMapper;
import com.example.team7_project_1.mappers.SpecificationMapper;

public class MapHandler {
    /*
    MapHandler serves as middleware between Application/Activity layer and Persistence/Data layer
     */

    static PhoneMapper phone_mapper;
    static SpecificationMapper specification_mapper;
    static LoadingActivity loading_activity;

    public static void setContextToCallback(LoadingActivity loading_activity) {
        MapHandler.loading_activity = loading_activity;
    }

    public static void initializePhoneMapper(String collection_path) {
        phone_mapper = new PhoneMapper(collection_path);
    }

    public static void initializeSpecificationMapper(String collection_path) {
        specification_mapper = new SpecificationMapper(collection_path);
    }

    public static void beginFetch() {
        phone_mapper.fetchFromDatabase();
        specification_mapper.fetchFromDatabase();
    }

    /*
    Called by mappers when they have finished fetching
     */
    public static void callbackFromFetch() {
        if (phone_mapper.isStillFetching() || specification_mapper.isStillFetching()) {
            return;
        }
        loading_activity.finishLoading();
    }
}
