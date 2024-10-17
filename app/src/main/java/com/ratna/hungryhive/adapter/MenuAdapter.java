package com.ratna.hungryhive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ratna.hungryhive.FoodDescriptionActivity;
import com.ratna.hungryhive.databinding.MenuItemBinding;
import com.ratna.hungryhive.model.MenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItems;
    private Context context;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MenuItemBinding binding = MenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder holder, int position) {
       MenuItem menuItem = menuItems.get(position);
       holder.bind(menuItem);

       holder.itemView.setOnClickListener(view -> {
           Intent intent = new Intent(context, FoodDescriptionActivity.class);
           intent.putExtra("food_name", menuItem.getFoodName());
           intent.putExtra("food_description", menuItem.getFoodDescription());
           intent.putExtra("food_price", menuItem.getFoodPrice());
           intent.putExtra("food_image", menuItem.getFoodImage());
           context.startActivity(intent);
       });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        private MenuItemBinding binding;

        public MenuViewHolder(@NonNull MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MenuItem menuItem) {
           binding.menuFoodName.setText(menuItem.getFoodName());
           binding.menuFoodPrice.setText(menuItem.getFoodPrice());
            Glide.with(binding.menuFoodImage.getContext()).load(menuItem.getFoodImage()).into(binding.menuFoodImage);
        }
    }
}
