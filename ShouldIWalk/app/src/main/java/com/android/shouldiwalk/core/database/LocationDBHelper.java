package com.android.shouldiwalk.core.database;

import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;
import com.android.shouldiwalk.core.exceptions.InvalidDataException;
import com.android.shouldiwalk.core.model.Location;

import java.util.List;

public interface LocationDBHelper {

    int insert(Location location) throws InvalidDataException, DatabaseCommFailure;

    void update(Location location) throws InvalidDataException, DatabaseCommFailure;

    List<Location> loadItems(QueryData queryData) throws InvalidDataException, DatabaseCommFailure;
}
