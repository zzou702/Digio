package com.example.team7_project_1.mappers;

import android.util.Log;

import com.example.team7_project_1.utilities.DataProvider;
import com.example.team7_project_1.handlers.MapHandler;
import com.example.team7_project_1.models.SpecificationDatabaseType;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class SpecificationMapper extends DataMapper {

    public SpecificationMapper(String collection_path) {
        super(collection_path);
    }

    @Override
    public void fetchFromDatabase() {
        ArrayList<SpecificationDatabaseType> specification_types = new ArrayList<>();

        // Getting specifications collection from Firestore

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(getCollectionPath()).get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("Load", "onSuccess: LIST EMPTY");
                        return;
                    }

                    for (DocumentSnapshot specs_document : documentSnapshots.getDocuments()) {
                        specification_types.add(parseSpecification(specs_document.getData()));
                    }

                    // Store data in DataProvider
                    DataProvider.setSpecificationTypesList(specification_types);

                    setIsFetchComplete(true);
                    MapHandler.callbackFromFetch();
                });
    }

    private SpecificationDatabaseType parseSpecification(Map<String,Object> data) {
        if (data == null) {
            Log.e("Load", "Document is NULL");
            return null;
        }

        // requiresNonNull to prevent passing null values to constructor
        // NOTE: if NullPointerException, might be due to field not added to database

        return new SpecificationDatabaseType(
                Objects.requireNonNull(data.get("fieldName")).toString(),
                Objects.requireNonNull(data.get("type")).toString(),
                Objects.requireNonNull(data.get("displayName")).toString(),
                Objects.requireNonNull(data.get("unit")).toString());
    }
}
