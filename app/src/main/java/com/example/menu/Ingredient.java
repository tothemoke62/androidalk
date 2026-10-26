package com.example.menu;

public class Ingredient {
    private String name;
    private double amountPerPortion;
    private String unit;
    private String category;
    private String preferredStore;

    public Ingredient() {}

    public Ingredient(String name, double amountPerPortion, String unit, String category, String preferredStore) {
        this.name = name;
        this.amountPerPortion = amountPerPortion;
        this.unit = unit;
        this.category = category;
        this.preferredStore = preferredStore;
    }

    public String getName() { return name; }
    public double getAmountPerPortion() { return amountPerPortion; }
    public String getUnit() { return unit; }
    public String getCategory() { return category; }
    public String getPreferredStore() { return preferredStore; }
}