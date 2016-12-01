package com.android.shouldiwalk.core.database;

import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;
import com.android.shouldiwalk.core.exceptions.InvalidDataException;
import com.android.shouldiwalk.core.model.TripData;

import java.util.List;

public interface TripDataDBHelper {

    void insert(TripData tripData) throws InvalidDataException, DatabaseCommFailure;

    void update(TripData tripData) throws InvalidDataException, DatabaseCommFailure;

    List<TripData> loadItems(QueryData queryData) throws DatabaseCommFailure;

    int countRecords() throws DatabaseCommFailure;
}
