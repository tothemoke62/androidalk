package com.example.menu;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.menu.databinding.FragmentPlannerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlannerFragment extends Fragment {

    private FragmentPlannerBinding binding;
    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlannerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // 1. Időszak választó
        List<String> periods = Arrays.asList("Heti menü", "3 napos menü", "2 hetes menü", "Havi menü");
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, periods);
        binding.spPeriod.setAdapter(periodAdapter);

        String currentPeriod = viewModel.getSelectedPeriod().getValue() != null ? viewModel.getSelectedPeriod().getValue() : "Heti menü";
        binding.spPeriod.setSelection(Math.max(periods.indexOf(currentPeriod), 0));

        binding.spPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setPeriod(periods.get(position));
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 2. Létszám beállítása
        int portionsVal = viewModel.getPortions().getValue() != null ? viewModel.getPortions().getValue() : 2;
        binding.etPortions.setText(String.valueOf(portionsVal));
        binding.etPortions.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int countVal = Integer.parseInt(s.toString().trim());
                    viewModel.setPortions(countVal);
                } catch (NumberFormatException ignored) {}
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // 3. Napok listája
        binding.rvDays.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getAvailableRecipes().observe(getViewLifecycleOwner(), recipes -> {
            List<String> names = new ArrayList<>();
            names.add("+ Válassz");
            if (recipes != null) {
                for (Recipe r : recipes) names.add(r.getName());
            }
            List<DayMealPlan> currentDays = viewModel.getDayMealPlans().getValue();
            if (currentDays != null) {
                binding.rvDays.setAdapter(new DayPlanAdapter(currentDays, names));
            }
        });

        viewModel.getDayMealPlans().observe(getViewLifecycleOwner(), days -> {
            List<Recipe> recipes = viewModel.getAvailableRecipes().getValue();
            List<String> names = new ArrayList<>();
            names.add("+ Válassz");
            if (recipes != null) {
                for (Recipe r : recipes) names.add(r.getName());
            }
            binding.rvDays.setAdapter(new DayPlanAdapter(days, names));
        });

        // 4. Generálás gomb kattintása
        binding.btnGenerateList.setOnClickListener(v -> {
            viewModel.generateShoppingList();
            BottomNavigationView nav = requireActivity().findViewById(R.id.bottomNavigationView);
            if (nav != null) {
                nav.setSelectedItemId(R.id.nav_shopping_list);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}