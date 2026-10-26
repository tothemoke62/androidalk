package com.example.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.menu.databinding.FragmentShoppingListBinding;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding binding;
    private SharedViewModel viewModel;
    private ShoppingSectionAdapter adapter;
    private String selectedStore = "Mind";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        adapter = new ShoppingSectionAdapter(new ArrayList<>(), productToRemove -> {
            viewModel.removeProduct(productToRemove);
            Toast.makeText(requireContext(), productToRemove.getDisplayText() + " törölve", Toast.LENGTH_SHORT).show();
        });

        binding.rvShoppingList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvShoppingList.setAdapter(adapter);

        viewModel.getShoppingDisplayItems().observe(getViewLifecycleOwner(), items -> filterList());

        // Bolti szűrő gombok
        binding.chipGroupStores.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chipLidl)) {
                selectedStore = "Lidl";
            } else if (checkedIds.contains(R.id.chipSpar)) {
                selectedStore = "SPAR";
            } else if (checkedIds.contains(R.id.chipMarket)) {
                selectedStore = "Piac";
            } else {
                selectedStore = "Mind";
            }
            filterList();
        });

        // Új tétel hozzáadása
        binding.btnAdd.setOnClickListener(v -> {
            String text = binding.etNewItem.getText() != null ? binding.etNewItem.getText().toString().trim() : "";
            if (!text.isEmpty()) {
                String targetStore = "Mind".equals(selectedStore) ? "Egyéb" : selectedStore;
                viewModel.addCustomItem(text, targetStore);
                binding.etNewItem.setText("");
                Toast.makeText(requireContext(), text + " hozzáadva!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList() {
        List<ShoppingDisplayItem> master = viewModel.getShoppingDisplayItems().getValue();
        if (master == null) master = new ArrayList<>();

        if ("Mind".equals(selectedStore)) {
            adapter.updateData(master);
        } else {
            List<ShoppingDisplayItem> filtered = new ArrayList<>();
            for (ShoppingDisplayItem item : master) {
                if (selectedStore.equals(item.getStore())) {
                    filtered.add(item);
                }
            }
            adapter.updateData(filtered);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}