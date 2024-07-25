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
    private List<String> prices;
    private List<Integer> images;
    private Context context;

    public MenuAdapter(Context context, List<String> items, List<String> prices, List<Integer> images) {
        this.context = context;
        this.items = items;
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
        String price = prices.get(position);
        int image = images.get(position);
        holder.bind(item, price, image);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FoodDescriptionActivity.class);
            intent.putExtra("item_name", item);
            intent.putExtra("item_price", price);
            intent.putExtra("item_image", image);
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

        public void bind(String item, String price, int image) {
            binding.menuFoodName.setText(item);
            binding.menuFoodPrice.setText(price);
            binding.menuFoodImage.setImageResource(image);
        }
    }
}
