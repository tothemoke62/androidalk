package com.example.receptszerkeszts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;

public class RecipeDetailFragment extends Fragment {

    private int servings = 2;
    private TextView tvServingsCount;
    private ImageView ivRecipeImage;
    private TextView tvRecipeTitle;
    private TextView tvIngredients;
    private TextView tvDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipedetail, container, false);

        // UI elemek összekötése
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage);
        tvRecipeTitle = view.findViewById(R.id.tvRecipeTitle);
        tvServingsCount = view.findViewById(R.id.tvServingsCount);
        tvIngredients = view.findViewById(R.id.tvIngredients);
        tvDescription = view.findViewById(R.id.tvDescription);

        Button btnDecrease = view.findViewById(R.id.btnDecrease);
        Button btnIncrease = view.findViewById(R.id.btnIncrease);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        // Adatok betöltése
        loadRecipeDetails();

        // Adagbeállító gombok
        btnDecrease.setOnClickListener(v -> {
            if (servings > 1) {
                servings--;
                updateServingsDisplay();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            servings++;
            updateServingsDisplay();
        });

        // Szerkesztés
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Szerkesztő nézet megnyitása...", Toast.LENGTH_SHORT).show();

        });

        btnEdit.setOnClickListener(v -> {
            showEditDialog();
        });

        //Törlés
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Recept törölve", Toast.LENGTH_SHORT).show();

        });

        //Vissza lépés
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        return view;
    }

    private void loadRecipeDetails() {
        tvRecipeTitle.setText("Gulyásleves");
        tvDescription.setText("Hagyományos magyar gulyásleves gazdagon, friss zöldségekkel és puha marhahússal.");

        // Helyes string formázás sortörésekkel:
        tvIngredients.setText("- 600g marhahús\n- 3 db sárgarépa\n- 2 db petrezselyem\n- 1 fej hagyma\n- 2 ek pirospaprika");

        // Nagy kép betöltése online URL-ről Glide-dal
        String imageUrl = "https://example.com/gulyasleves.jpg";
        Glide.with(this)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivRecipeImage);

        updateServingsDisplay();
    }

    private void updateServingsDisplay() {
        tvServingsCount.setText(servings + " adag");
    }

    private void showEditDialog() {
        // 1. Inflate-eljük a dialógus XML layoutját
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_dialog_recipe_edit, null);

        // 2. Összekötjük a dialógus belső elemeit
        EditText etEditTitle = dialogView.findViewById(R.id.etEditTitle);
        EditText etEditIngredients = dialogView.findViewById(R.id.etEditIngredients);
        EditText etEditDescription = dialogView.findViewById(R.id.etEditDescription);

        // 3. Beletöltjük a meglévő adatokat, hogy ne üresen jelenjen meg
        etEditTitle.setText(tvRecipeTitle.getText().toString());
        etEditIngredients.setText(tvIngredients.getText().toString());
        etEditDescription.setText(tvDescription.getText().toString());

        // 4. Felépítjük az AlertDialog-ot Material / AppCompat stílusban
        new AlertDialog.Builder(requireContext())
                .setTitle("Recept szerkesztése")
                .setView(dialogView)
                .setPositiveButton("Mentés", (dialog, which) -> {
                    // Itt mentjük el a módosításokat a képernyőre
                    String newTitle = etEditTitle.getText().toString();
                    String newIngredients = etEditIngredients.getText().toString();
                    String newDescription = etEditDescription.getText().toString();

                    tvRecipeTitle.setText(newTitle);
                    tvIngredients.setText(newIngredients);
                    tvDescription.setText(newDescription);

                    Toast.makeText(requireContext(), "Recept sikeresen frissítve!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Mégse", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

}