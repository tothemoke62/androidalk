package com.example.menu;

public class DayMealPlan {
    private String dayName;
    private String lunchRecipe;
    private String dinnerRecipe;

    public DayMealPlan(String dayName, String lunchRecipe, String dinnerRecipe) {
        this.dayName = dayName;
        this.lunchRecipe = lunchRecipe;
        this.dinnerRecipe = dinnerRecipe;
    }

    public String getDayName() { return dayName; }
    public String getLunchRecipe() { return lunchRecipe; }
    public void setLunchRecipe(String lunchRecipe) { this.lunchRecipe = lunchRecipe; }
    public String getDinnerRecipe() { return dinnerRecipe; }
    public void setDinnerRecipe(String dinnerRecipe) { this.dinnerRecipe = dinnerRecipe; }
}