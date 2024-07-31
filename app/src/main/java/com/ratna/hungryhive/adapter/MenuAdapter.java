package com.ratna.hungryhive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhive.FoodDescriptionActivity;
import com.ratna.hungryhive.databinding.MenuItemBinding;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<String> items;
    private List<String> descriptions;
    private List<String> prices;
    private List<Integer> images;
    private Context context;

    public MenuAdapter(Context context, List<String> items, List<String> descriptions, List<String> prices, List<Integer> images) {
        this.context = context;
        this.items = items;
        this.descriptions = descriptions;
        this.prices = prices;
        this.images = images;
    }

    @NonNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MenuItemBinding binding = MenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
        String item = items.get(position);
        String description = descriptions.get(position);
        String price = prices.get(position);
        int image = images.get(position);
        holder.bind(item, description, price, image);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FoodDescriptionActivity.class);
            intent.putExtra("food_name", item);
            intent.putExtra("food_description", description);
            intent.putExtra("food_image", image);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        private MenuItemBinding binding;

        public MenuViewHolder(@NonNull MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String item, String description, String price, int image) {
            binding.menuFoodName.setText(item);
            binding.menuFoodPrice.setText(price);
            binding.menuFoodImage.setImageResource(image);
        }
    }
}
