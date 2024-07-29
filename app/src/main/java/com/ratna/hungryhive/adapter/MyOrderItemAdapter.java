package com.ratna.hungryhive.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhive.databinding.MyOrderItemBinding;

import java.util.List;

public class MyOrderItemAdapter extends RecyclerView.Adapter<MyOrderItemAdapter.MyOrderItemViewHolder> {
    private List<String> items;
    private List<Integer> images;
    private List<String> prices;
    private List<String> quantities;

    public MyOrderItemAdapter(List<String> items, List<Integer> images, List<String> prices, List<String> quantities){
        this.items = items;
        this.images = images;
        this.prices = prices;
        this.quantities = quantities;
    }

    @NonNull
    @Override
    public MyOrderItemAdapter.MyOrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyOrderItemBinding binding = MyOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyOrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderItemAdapter.MyOrderItemViewHolder holder, int position) {
        String item = items.get(position);
        int image = images.get(position);
        String price = prices.get(position);
        String quantity = quantities.get(position);
        holder.bind(item, image, price, quantity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyOrderItemViewHolder extends RecyclerView.ViewHolder {
        private MyOrderItemBinding binding;

        public MyOrderItemViewHolder(@NonNull MyOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String item, int image, String price, String quantity) {
            binding.foodName.setText(item);
            binding.foodImage.setImageResource(image);
            binding.foodAmount.setText(price);
            binding.foodQuantity.setText(quantity);
        }
    }
}
