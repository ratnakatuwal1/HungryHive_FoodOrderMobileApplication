package com.ratna.hungryhive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ratna.hungryhive.databinding.CartItemBinding;
import com.ratna.hungryhive.model.CartItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
//    private int[] itemQuantity;
//    private List<String> cartItems;
//    private List<String> cartPrices;
//    private List<Integer> cartImages;
//    private List<String> cartDescriptions;
//    private List<String> cartIngredients;
//    private List<Integer> cartQuantities;
//    private Context context;
//    private FirebaseAuth mAuth;
//    private String userId;
    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
//        this.cartItems = new ArrayList<>(cartItems);
//        this.cartPrices = new ArrayList<>(cartPrices);
//        this.cartImages = new ArrayList<>(cartImages);
//        this.itemQuantity = new int[cartItems.size()];
        this.context = context;
        this.cartItems = new ArrayList<>(cartItems);


//        mAuth = FirebaseAuth.getInstance();
//        userId = mAuth.getCurrentUser().getUid();
//        Arrays.fill(itemQuantity, 1);
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
//        String item = cartItems.get(position);
//        String price = cartPrices.get(position);
//        int image = cartImages.get(position);
//        int quantity = itemQuantity[position];

        CartItem cartItem = cartItems.get(position);

        holder.bind(cartItem, position);
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

        public void bind(CartItem cartItem, int position) {
//            binding.textFoodName.setText(item);
//            binding.textFoodAmount.setText(price);
//            binding.imageCartFood.setImageResource(image);
//            binding.textQty.setText(String.valueOf(quantity));
            binding.textFoodName.setText(cartItem.getFoodName());
            binding.textFoodAmount.setText(cartItem.getFoodPrice());
            binding.textQty.setText(String.valueOf(cartItem.getFoodQuantity()));
            Glide.with(context)
                    .load(cartItem.getFoodDescription())  // Assuming 'foodDescription' holds the image URL
                    .into(binding.imageCartFood);


            binding.buttonMinus.setOnClickListener(view -> {
                if (cartItem.getFoodQuantity() > 1) {
                    cartItem.setFoodQuantity(cartItem.getFoodQuantity() - 1);
                    binding.textQty.setText(String.valueOf(cartItem.getFoodQuantity()));
                }
            });

            binding.buttonPlus.setOnClickListener(view -> {
                if (cartItem.getFoodQuantity() < 10) {
                    cartItem.setFoodQuantity(cartItem.getFoodQuantity() + 1);
                    binding.textQty.setText(String.valueOf(cartItem.getFoodQuantity()));
                }
            });

            binding.buttonDelete.setOnClickListener(view -> {
                removeItem(position);
            });
        }
    }

    private void removeItem(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItems.size());

//        int[] newQuantities = new int[itemQuantity.length - 1];
//        System.arraycopy(itemQuantity, 0, newQuantities, 0, position);
//        System.arraycopy(itemQuantity, position + 1, newQuantities, position, itemQuantity.length - position - 1);
//        itemQuantity = newQuantities;
//
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, cartItems.size());
    }
}
