package com.android.shouldiwalk.core.database;

public enum SqlQueryNames {
    CreateUserDataTable("create_user_data_table"),
    DeleteUserDataTable("delete_user_data_table"),
    FetchUserData("fetch_user_data"),
    UpdateUserData("update_user_data"),
    CreateUserData("create_user_data_item"),
    CreateLocationsTable("create_locations_table"),
    CreateLocationsTableCoordinatesIndex("create_locations_table_coordinates_index"),
    CreateTripDataTable("create_trip_data_table"),
    GetTripDataItemCount("get_trip_data_item_count");

    private String name;

    SqlQueryNames(String desc) {
        this.name=desc;
    }

    public String getQueryName() {
        return name;
    }
}
