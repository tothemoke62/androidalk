package com.example.receptbongeszo;

public class Recipe {
    private final String name;
    private final String category;
    private final String imageUrl;
    private final String description;

    public Recipe(String name, String category, String imageUrl, String description) {
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}
