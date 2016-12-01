package com.android.shouldiwalk.core.model;

public enum MeanOfTransport {
    Car("Car", 1),
    Bus("Bus", 2),
    Subway("Subway", 3),
    Walk("Walk", 4),
    Bike("Bike", 5),
    Motorcycle("Motorcycle", 6),
    Train("Train", 7);

    private String name;
    private int id;

    MeanOfTransport(String name, int id) {
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
