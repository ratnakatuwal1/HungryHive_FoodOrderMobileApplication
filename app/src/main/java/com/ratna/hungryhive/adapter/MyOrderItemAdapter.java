package com.ratna.hungryhive.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ratna.hungryhive.databinding.MyOrderItemBinding;
import com.ratna.hungryhive.model.CartItem;

import java.util.List;

public class MyOrderItemAdapter extends RecyclerView.Adapter<MyOrderItemAdapter.MyOrderItemViewHolder> {
//    private List<String> items;
//    private List<Integer> images;
//    private List<String> prices;
//    private List<String> quantities;
    private List<CartItem> cartItems;

    public MyOrderItemAdapter(List<CartItem> cartItems){
//        this.items = items;
//        this.images = images;
//        this.prices = prices;
//        this.quantities = quantities;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public MyOrderItemAdapter.MyOrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyOrderItemBinding binding = MyOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyOrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderItemAdapter.MyOrderItemViewHolder holder, int position) {
//        String item = items.get(position);
//        int image = images.get(position);
//        String price = prices.get(position);
//        String quantity = quantities.get(position);
//        holder.bind(item, image, price, quantity);
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class MyOrderItemViewHolder extends RecyclerView.ViewHolder {
        private MyOrderItemBinding binding;

        public MyOrderItemViewHolder(@NonNull MyOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem cartItem) {
//            binding.foodName.setText(item);
//            binding.foodImage.setImageResource(image);
//            binding.foodAmount.setText(price);
//            binding.foodQuantity.setText(quantity);
            binding.foodName.setText(cartItem.getFoodName());
            binding.foodAmount.setText(cartItem.getFoodPrice());
            binding.foodQuantity.setText(String.valueOf(cartItem.getFoodQuantity()));

            // Load the food image using Glide
            Glide.with(binding.foodImage.getContext())
                    .load(cartItem.getFoodImage())
                    .into(binding.foodImage);
        }
    }
}
