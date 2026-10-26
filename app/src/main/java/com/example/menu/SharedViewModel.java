package com.example.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<List<Recipe>> availableRecipes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> selectedPeriod = new MutableLiveData<>("Heti menü");
    private final MutableLiveData<Integer> portions = new MutableLiveData<>(2);
    private final MutableLiveData<List<DayMealPlan>> dayMealPlans = new MutableLiveData<>();
    private final MutableLiveData<List<ShoppingDisplayItem>> shoppingDisplayItems = new MutableLiveData<>(new ArrayList<>());

    public SharedViewModel() {
        updateDaysForPeriod("Heti menü");
    }

    public void setAvailableRecipes(List<Recipe> recipes) {
        this.availableRecipes.setValue(recipes != null ? recipes : new ArrayList<>());
    }

    public LiveData<List<Recipe>> getAvailableRecipes() { return availableRecipes; }
    public LiveData<String> getSelectedPeriod() { return selectedPeriod; }
    public LiveData<Integer> getPortions() { return portions; }
    public LiveData<List<DayMealPlan>> getDayMealPlans() { return dayMealPlans; }
    public LiveData<List<ShoppingDisplayItem>> getShoppingDisplayItems() { return shoppingDisplayItems; }

    public void setPeriod(String period) {
        selectedPeriod.setValue(period);
        updateDaysForPeriod(period);
    }

    public void setPortions(int count) {
        portions.setValue(count > 0 ? count : 1);
    }

    public void updateDaysForPeriod(String period) {
        List<DayMealPlan> list = new ArrayList<>();
        if ("3 napos menü".equals(period)) {
            list.add(new DayMealPlan("1. NAP (Hétfő)", "", ""));
            list.add(new DayMealPlan("2. NAP (Kedd)", "", ""));
            list.add(new DayMealPlan("3. NAP (Szerda)", "", ""));
        } else if ("2 hetes menü".equals(period)) {
            for (int i = 1; i <= 14; i++) {
                list.add(new DayMealPlan(i + ". NAP", "", ""));
            }
        } else if ("Havi menü".equals(period)) {
            for (int i = 1; i <= 30; i++) {
                list.add(new DayMealPlan(i + ". NAP", "", ""));
            }
        } else {
            list.add(new DayMealPlan("HÉTFŐ", "", ""));
            list.add(new DayMealPlan("KEDD", "", ""));
            list.add(new DayMealPlan("SZERDA", "", ""));
            list.add(new DayMealPlan("CSÜTÖRTÖK", "", ""));
            list.add(new DayMealPlan("PÉNTEK", "", ""));
            list.add(new DayMealPlan("SZOMBAT", "", ""));
            list.add(new DayMealPlan("VASÁRNAP", "", ""));
        }
        dayMealPlans.setValue(list);
    }

    public void generateShoppingList() {
        List<DayMealPlan> days = dayMealPlans.getValue() != null ? dayMealPlans.getValue() : new ArrayList<>();
        int count = portions.getValue() != null ? portions.getValue() : 2;
        List<Recipe> recipes = availableRecipes.getValue() != null ? availableRecipes.getValue() : new ArrayList<>();

        List<String> chosenMealNames = new ArrayList<>();
        for (DayMealPlan day : days) {
            if (day.getLunchRecipe() != null && !day.getLunchRecipe().trim().isEmpty() && !day.getLunchRecipe().equals("+ Válassz")) {
                chosenMealNames.add(day.getLunchRecipe().trim());
            }
            if (day.getDinnerRecipe() != null && !day.getDinnerRecipe().trim().isEmpty() && !day.getDinnerRecipe().equals("+ Válassz")) {
                chosenMealNames.add(day.getDinnerRecipe().trim());
            }
        }

        class Aggregated {
            String name;
            double totalAmount;
            String unit;
            String store;
            Set<String> sources = new HashSet<>();
        }

        Map<String, Aggregated> aggMap = new HashMap<>();

        for (String mealName : chosenMealNames) {
            Recipe foundRecipe = null;
            for (Recipe r : recipes) {
                if (r.getName().equalsIgnoreCase(mealName)) {
                    foundRecipe = r;
                    break;
                }
            }

            if (foundRecipe != null && !foundRecipe.getIngredients().isEmpty()) {
                double scale = (double) count / (double) foundRecipe.getDefaultPortions();
                for (Ingredient ing : foundRecipe.getIngredients()) {
                    String store = (ing.getPreferredStore() != null && !ing.getPreferredStore().isEmpty()) ? ing.getPreferredStore() : "Lidl";
                    String key = ing.getName().toLowerCase() + "_" + ing.getUnit().toLowerCase() + "_" + store;
                    Aggregated agg = aggMap.get(key);
                    if (agg == null) {
                        agg = new Aggregated();
                        agg.name = ing.getName();
                        agg.unit = ing.getUnit();
                        agg.store = store;
                        agg.totalAmount = ing.getAmountPerPortion() * scale;
                        aggMap.put(key, agg);
                    } else {
                        agg.totalAmount += (ing.getAmountPerPortion() * scale);
                    }
                    agg.sources.add(foundRecipe.getName());
                }
            } else {
                String key = mealName.toLowerCase() + "_custom";
                Aggregated agg = aggMap.get(key);
                if (agg == null) {
                    agg = new Aggregated();
                    agg.name = mealName;
                    agg.unit = "";
                    agg.store = "Egyéb";
                    agg.totalAmount = 1;
                    aggMap.put(key, agg);
                }
            }
        }

        Map<String, List<Aggregated>> grouped = new HashMap<>();
        for (Aggregated a : aggMap.values()) {
            if (!grouped.containsKey(a.store)) {
                grouped.put(a.store, new ArrayList<>());
            }
            grouped.get(a.store).add(a);
        }

        List<ShoppingDisplayItem> result = new ArrayList<>();
        for (Map.Entry<String, List<Aggregated>> entry : grouped.entrySet()) {
            String store = entry.getKey();
            String headerTitle;
            if ("Lidl".equals(store)) headerTitle = "🥩 HÚSÁRU & ZÖLDSÉG (Lidl)";
            else if ("SPAR".equals(store)) headerTitle = "🥛 TEJTERMÉK & SZÁRAZÁRU (SPAR)";
            else if ("Piac".equals(store)) headerTitle = "🥕 PIAC & FRISS ÁRU (Piac)";
            else headerTitle = "🛒 " + store;

            result.add(new ShoppingDisplayItem.Header(headerTitle, store));

            for (Aggregated a : entry.getValue()) {
                String amountStr = (a.unit != null && !a.unit.isEmpty()) ? formatAmount(a.totalAmount, a.unit) + " " : "";
                StringBuilder sources = new StringBuilder();
                if (a.sources.size() > 1) {
                    sources.append(" (").append(String.join(" + ", a.sources)).append(")");
                }
                String line = amountStr + a.name + sources.toString();
                result.add(new ShoppingDisplayItem.Product(line, store, false));
            }
        }

        List<ShoppingDisplayItem> prev = shoppingDisplayItems.getValue();
        if (prev != null) {
            boolean hasOther = false;
            for (ShoppingDisplayItem item : prev) {
                if (item instanceof ShoppingDisplayItem.Product && "Egyéb".equals(item.getStore())) {
                    if (!hasOther) {
                        result.add(new ShoppingDisplayItem.Header("🧼 EGYÉB", "Egyéb"));
                        hasOther = true;
                    }
                    result.add(item);
                }
            }
        }

        shoppingDisplayItems.setValue(result);
    }

    private String formatAmount(double amount, String unit) {
        if ("g".equalsIgnoreCase(unit) && amount >= 1000.0) {
            double kg = amount / 1000.0;
            return (kg % 1.0 == 0) ? ((int) kg) + " kg" : String.format("%.1f kg", kg);
        } else if (amount % 1.0 == 0) {
            return ((int) amount) + " " + unit;
        } else {
            return String.format("%.1f %s", amount, unit);
        }
    }

    public void addCustomItem(String name, String store) {
        List<ShoppingDisplayItem> current = shoppingDisplayItems.getValue() != null ? new ArrayList<>(shoppingDisplayItems.getValue()) : new ArrayList<>();
        boolean hasHeader = false;
        for (ShoppingDisplayItem item : current) {
            if (item instanceof ShoppingDisplayItem.Header && store.equals(item.getStore())) {
                hasHeader = true;
                break;
            }
        }
        if (!hasHeader) {
            String title = "Egyéb".equals(store) ? "🧼 EGYÉB" : "🛒 " + store;
            current.add(new ShoppingDisplayItem.Header(title, store));
        }
        current.add(new ShoppingDisplayItem.Product(name, store, false));
        shoppingDisplayItems.setValue(current);
    }

    public void removeProduct(ShoppingDisplayItem.Product product) {
        List<ShoppingDisplayItem> current = shoppingDisplayItems.getValue() != null ? new ArrayList<>(shoppingDisplayItems.getValue()) : new ArrayList<>();
        current.remove(product);
        boolean remaining = false;
        for (ShoppingDisplayItem item : current) {
            if (item instanceof ShoppingDisplayItem.Product && product.getStore().equals(item.getStore())) {
                remaining = true;
                break;
            }
        }
        if (!remaining) {
            current.removeIf(item -> item instanceof ShoppingDisplayItem.Header && product.getStore().equals(item.getStore()));
        }
        shoppingDisplayItems.setValue(current);
    }
}
