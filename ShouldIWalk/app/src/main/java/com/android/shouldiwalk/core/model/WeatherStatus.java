package com.android.shouldiwalk.core.model;

public enum WeatherStatus {
    Rainy("Rainy"),
    Sunny("Sunny"),
    Cloudy("Cloudy"),
    Windy("Windy"),
    Snowy("Snowy");

    private String name;

    WeatherStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
