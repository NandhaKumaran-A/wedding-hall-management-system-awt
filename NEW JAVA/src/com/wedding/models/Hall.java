package com.wedding.models;

public class Hall {
    private final String id;
    private String name;
    private int capacity;
    private double pricePerHour;

    public Hall(String id, String name, int capacity, double pricePerHour) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public double getPricePerHour() { return pricePerHour; }

    public void setName(String name) { this.name = name; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }

    @Override
    public String toString() {
        return name + " (" + capacity + ") - " + pricePerHour + "/hr";
    }
}
