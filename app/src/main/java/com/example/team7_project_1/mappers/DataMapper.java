package com.example.team7_project_1.mappers;

public abstract class DataMapper {

    //Fields
    private boolean is_fetch_complete = false;
    private final String collection_path;


    /**
     * Constructor
     */
    protected DataMapper(String collection_path) {
        this.collection_path = collection_path;
    }

    public abstract void fetchFromDatabase();

    public boolean isStillFetching() {
        return !is_fetch_complete;
    }

    public void setIsFetchComplete(boolean is_fetch_complete) {
        this.is_fetch_complete = is_fetch_complete;
    }

    public String getCollectionPath() {
        return collection_path;
    }
}
