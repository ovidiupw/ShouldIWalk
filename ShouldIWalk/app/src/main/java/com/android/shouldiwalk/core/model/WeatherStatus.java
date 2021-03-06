package com.android.shouldiwalk.core.model;

public enum WeatherStatus {
    Rainy("Rainy", 1),
    Sunny("Sunny", 2),
    Cloudy("Cloudy", 3),
    Windy("Windy", 4),
    Snowy("Snowy", 5);

    private String name;
    private int id;

    WeatherStatus(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
