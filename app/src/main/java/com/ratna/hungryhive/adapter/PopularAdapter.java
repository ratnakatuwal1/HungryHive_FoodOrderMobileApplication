package com.ratna.hungryhive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhive.FoodDescriptionActivity;
import com.ratna.hungryhive.databinding.PopularItemBinding;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private List<String> items;
    private List<String> descriptions;
    private List<Integer> images;
    private List<String> prices;
    private Context context;

    public PopularAdapter(Context context, List<String> items, List<String> descriptions, List<Integer> images, List<String> prices) {
        this.context = context;
        this.items = items;
        this.descriptions = descriptions;
        this.images = images;
        this.prices = prices;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PopularItemBinding binding = PopularItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PopularViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.PopularViewHolder holder, int position) {
        String item = items.get(position);
        String description = descriptions.get(position);
        int image = images.get(position);
        String price = prices.get(position);
        holder.bind(item, image, price);

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

    public static class PopularViewHolder extends RecyclerView.ViewHolder {
        private PopularItemBinding binding;

        public PopularViewHolder(@NonNull PopularItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String item, int image, String price) {
            binding.textFoodName.setText(item);
            binding.foodImage.setImageResource(image);
            binding.textFoodAmount.setText(price);
        }
    }
}

