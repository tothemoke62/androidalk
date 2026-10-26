package com.example.receptbongeszo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.w3c.dom.Text;
//Popup menü DONE
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {
    private List<Recipe> items;

    public RecipeAdapter(List<Recipe> items) { this.items = items; }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        VH(View v) {
            super(v);
            img = v.findViewById(R.id.imgRecipe);
            name = v.findViewById(R.id.tvName);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Recipe r = items.get(position);
        h.name.setText(r.getName());
        Glide.with(h.img).load(r.getImageUrl()).centerCrop().into(h.img);
        h.itemView.setOnClickListener(v -> showDetails(v.getContext(), r));
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void update(List<Recipe> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    private void showDetails(Context ctx, Recipe r)
    {
        View view = LayoutInflater.from(ctx).inflate(R.layout.dialog_recipe, null);
        ImageView imgv = view.findViewById(R.id.dialogImage);
        TextView title = view.findViewById(R.id.dialogTitle);
        TextView desc = view.findViewById(R.id.dialogDesc);

        title.setText(r.getName());
        desc.setText(r.getDescription());
        Glide.with(imgv).load(r.getImageUrl()).centerCrop().into(imgv);

        new MaterialAlertDialogBuilder(ctx).setView(view).show();
    }
}
