package com.example.menu;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.menu.databinding.ItemShoppingHeaderBinding;
import com.example.menu.databinding.ItemShoppingRowBinding;
import java.util.List;

public class ShoppingSectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnDeleteItemListener {
        void onDelete(ShoppingDisplayItem.Product product);
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PRODUCT = 1;

    private List<ShoppingDisplayItem> displayList;
    private final OnDeleteItemListener deleteListener;

    public ShoppingSectionAdapter(List<ShoppingDisplayItem> displayList, OnDeleteItemListener deleteListener) {
        this.displayList = displayList;
        this.deleteListener = deleteListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (displayList.get(position) instanceof ShoppingDisplayItem.Header) ? TYPE_HEADER : TYPE_PRODUCT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            ItemShoppingHeaderBinding binding = ItemShoppingHeaderBinding.inflate(inflater, parent, false);
            return new HeaderViewHolder(binding);
        } else {
            ItemShoppingRowBinding binding = ItemShoppingRowBinding.inflate(inflater, parent, false);
            return new ProductViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShoppingDisplayItem item = displayList.get(position);
        if (item instanceof ShoppingDisplayItem.Header) {
            ((HeaderViewHolder) holder).bind((ShoppingDisplayItem.Header) item);
        } else if (item instanceof ShoppingDisplayItem.Product) {
            ((ProductViewHolder) holder).bind((ShoppingDisplayItem.Product) item);
        }
    }

    @Override
    public int getItemCount() { return displayList.size(); }

    public void updateData(List<ShoppingDisplayItem> newList) {
        this.displayList = newList;
        notifyDataSetChanged();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemShoppingHeaderBinding binding;
        public HeaderViewHolder(ItemShoppingHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(ShoppingDisplayItem.Header header) {
            binding.tvHeaderTitle.setText(header.getTitle());
        }
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemShoppingRowBinding binding;
        public ProductViewHolder(ItemShoppingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(ShoppingDisplayItem.Product product) {
            binding.tvItemText.setText(product.getDisplayText());

            binding.cbBought.setOnCheckedChangeListener(null);
            binding.cbBought.setChecked(product.isChecked());
            applyStrikeThrough(product.isChecked());

            binding.cbBought.setOnCheckedChangeListener((btn, isChecked) -> {
                product.setChecked(isChecked);
                applyStrikeThrough(isChecked);
            });

            binding.btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(product);
                }
            });
        }

        private void applyStrikeThrough(boolean isChecked) {
            if (isChecked) {
                binding.tvItemText.setPaintFlags(binding.tvItemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.tvItemText.setAlpha(0.5f);
            } else {
                binding.tvItemText.setPaintFlags(binding.tvItemText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                binding.tvItemText.setAlpha(1.0f);
            }
        }
    }
}