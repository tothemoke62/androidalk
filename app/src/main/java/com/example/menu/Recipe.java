package com.example.menu;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String id;
    private String name;
    private int defaultPortions = 2;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {}

    public Recipe(String id, String name, int defaultPortions, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.defaultPortions = defaultPortions;
        this.ingredients = ingredients;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getDefaultPortions() { return defaultPortions; }
    public List<Ingredient> getIngredients() { return ingredients; }
}