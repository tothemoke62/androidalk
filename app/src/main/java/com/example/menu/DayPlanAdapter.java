package com.example.menu;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.menu.databinding.ItemDayPlanBinding;
import java.util.List;

public class DayPlanAdapter extends RecyclerView.Adapter<DayPlanAdapter.ViewHolder> {

    private final List<DayMealPlan> days;
    private final List<String> availableRecipeNames;

    public DayPlanAdapter(List<DayMealPlan> days, List<String> availableRecipeNames) {
        this.days = days;
        this.availableRecipeNames = availableRecipeNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDayPlanBinding binding = ItemDayPlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayMealPlan day = days.get(position);
        holder.binding.tvDayTitle.setText(day.getDayName());

        holder.binding.etLunch.setText(day.getLunchRecipe());
        holder.binding.etDinner.setText(day.getDinnerRecipe());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                holder.itemView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                availableRecipeNames
        );
        holder.binding.spLunch.setAdapter(spinnerAdapter);
        holder.binding.spDinner.setAdapter(spinnerAdapter);

        holder.binding.etLunch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                day.setLunchRecipe(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        holder.binding.etDinner.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                day.setDinnerRecipe(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        holder.binding.spLunch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String chosen = availableRecipeNames.get(pos);
                if (!chosen.equals("+ Válassz") && !chosen.isEmpty()) {
                    holder.binding.etLunch.setText(chosen);
                    day.setLunchRecipe(chosen);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        holder.binding.spDinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String chosen = availableRecipeNames.get(pos);
                if (!chosen.equals("+ Válassz") && !chosen.isEmpty()) {
                    holder.binding.etDinner.setText(chosen);
                    day.setDinnerRecipe(chosen);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public int getItemCount() { return days.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemDayPlanBinding binding;
        public ViewHolder(ItemDayPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}