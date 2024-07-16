package com.ratna.hungryhive.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhive.databinding.RecentItemBinding;

import java.util.List;

public class RecentItemAdapter extends RecyclerView.Adapter<RecentItemAdapter.RecentItemViewHolder> {
    private List<String> items;
    private List<Integer> images;
    private List<String> prices;

    public RecentItemAdapter(List<String> items, List<Integer> images, List<String> prices) {
        this.items = items;
        this.images = images;
        this.prices = prices;
    }

    @NonNull
    @Override
    public RecentItemAdapter.RecentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecentItemBinding binding = RecentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecentItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentItemAdapter.RecentItemViewHolder holder, int position) {
        String item = items.get(position);
        int image = images.get(position);
        String price = prices.get(position);
        holder.bind(item, image, price);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RecentItemViewHolder extends RecyclerView.ViewHolder {
        private RecentItemBinding binding;

        public RecentItemViewHolder(@NonNull RecentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String item, int image, String price) {
            binding.textFoodName.setText(item);
            binding.imageFood.setImageResource(image);
            binding.textFoodAmount.setText(price);
        }
    }
}