package com.ratna.hungryhive.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhive.databinding.PopularItemBinding;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private List<String> items;
    private List<Integer> images;
    private List<String> prices;

    public PopularAdapter(List<String> items, List<Integer> images, List<String> prices) {
        this.items = items;
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
        int image = images.get(position);
        String price = prices.get(position);
        holder.bind(item, image, price);
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

