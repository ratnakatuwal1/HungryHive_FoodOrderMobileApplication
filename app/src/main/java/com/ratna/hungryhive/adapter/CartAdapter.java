package com.ratna.hungryhive.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhive.databinding.CartItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private int[] itemQuantity;
    private List<String> cartItems;
    private List<String> cartPrices;
    private List<Integer> cartImages;

    public CartAdapter(List<String> cartItems, List<String> cartPrices, List<Integer> cartImages) {
        this.cartItems = new ArrayList<>(cartItems);
        this.cartPrices = new ArrayList<>(cartPrices);
        this.cartImages = new ArrayList<>(cartImages);
        this.itemQuantity = new int[cartItems.size()];
        Arrays.fill(itemQuantity, 1);
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        String item = cartItems.get(position);
        String price = cartPrices.get(position);
        int image = cartImages.get(position);
        int quantity = itemQuantity[position];

        holder.bind(item, price, image, quantity, position);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CartItemBinding binding;

        public CartViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String item, String price, int image, int quantity, int position) {
            binding.textFoodName.setText(item);
            binding.textFoodAmount.setText(price);
            binding.imageCartFood.setImageResource(image);
            binding.textQty.setText(String.valueOf(quantity));

            binding.buttonMinus.setOnClickListener(view -> {
                if (itemQuantity[position] > 1) {
                    itemQuantity[position]--;
                    binding.textQty.setText(String.valueOf(itemQuantity[position]));
                }
            });

            binding.buttonPlus.setOnClickListener(view -> {
                if (itemQuantity[position] < 10) {
                    itemQuantity[position]++;
                    binding.textQty.setText(String.valueOf(itemQuantity[position]));
                }
            });

            binding.buttonDelete.setOnClickListener(view -> {
                removeItem(position);
            });
        }
    }

    private void removeItem(int position) {
        cartItems.remove(position);
        cartPrices.remove(position);
        cartImages.remove(position);

        int[] newQuantities = new int[itemQuantity.length - 1];
        System.arraycopy(itemQuantity, 0, newQuantities, 0, position);
        System.arraycopy(itemQuantity, position + 1, newQuantities, position, itemQuantity.length - position - 1);
        itemQuantity = newQuantities;

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItems.size());
    }
}
