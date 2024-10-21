package com.ratna.hungryhive.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.FoodDescriptionActivity;
import com.ratna.hungryhive.R;
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
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userId;

    public CartAdapter(Context context, List<CartItem> cartItems) {
//        this.cartItems = new ArrayList<>(cartItems);
//        this.cartPrices = new ArrayList<>(cartPrices);
//        this.cartImages = new ArrayList<>(cartImages);
//        this.itemQuantity = new int[cartItems.size()];
        this.context = context;
        this.cartItems = cartItems;
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
       // int cartItemNumber = cartItems.size();
//        int[] itemQuantity = new int[cartItemNumber];
//        Arrays.fill(itemQuantity, 1);
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("CartItem");

//        mAuth = FirebaseAuth.getInstance();
//        userId = mAuth.getCurrentUser().getUid();
//        Arrays.fill(itemQuantity, 1);
    }



    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
//        String item = cartItems.get(position);
//        String price = cartPrices.get(position);
//        int image = cartImages.get(position);
//        int quantity = itemQuantity[position];

        CartItem cartItem = cartItems.get(position);
        Log.d("CartAdapter", "onBindViewHolder: Binding item at position " + position + " with name: " + cartItem.getFoodName());
        holder.bind(cartItem);

//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, FoodDescriptionActivity.class);
//            intent.putExtra("food_name", cartItem.getFoodName());
//            intent.putExtra("food_description", cartItem.getFoodDescription());
//            intent.putExtra("food_price", cartItem.getFoodPrice());
//            intent.putExtra("food_image", cartItem.getFoodImage());
//            intent.putExtra("food_ingredients", cartItem.getFoodIngredients());
//            intent.putExtra("food_quantity", cartItem.getFoodQuantity());
//            context.startActivity(intent);
//        });

//        holder.binding.textFoodName.setText(cartItem.getFoodName());
//        holder.binding.textFoodAmount.setText(cartItem.getFoodPrice());
//        holder.binding.textQty.setText(String.valueOf(cartItem.getFoodQuantity()));

        // Ensure Glide is loading the correct image
//        Glide.with(context)
//                .load(cartItem.getFoodImage())   // Ensure the image URL is correctly passed
//                .into(holder.binding.imageCartFood);
//
//        holder.bind(cartItem, position);
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

        public void bind(CartItem cartItem) {
            Log.d("CartAdapter", "Binding item: " + cartItem.getFoodName());
//            binding.textFoodName.setText(item);
//            binding.textFoodAmount.setText(price);
//            binding.imageCartFood.setImageResource(image);
//            binding.textQty.setText(String.valueOf(quantity));
            binding.textFoodName.setText(cartItem.getFoodName());
            binding.textFoodAmount.setText(cartItem.getFoodPrice());
            binding.textQty.setText(String.valueOf(cartItem.getFoodQuantity()));
//            Glide.with(context)
//                    .load(cartItem.getFoodImage())  // Assuming 'foodDescription' holds the image URL
//                    .into(binding.imageCartFood);
            Glide.with(context)
                    .load(cartItem.getFoodImage())  // Ensure the image URL is valid
                    .placeholder(R.drawable.burger)  // Add a placeholder
                    .error(R.drawable.burger)  // Add an error image in case of failure
                    .into(binding.imageCartFood);

// Log for successful Glide loading
            Log.d("CartAdapter", "Glide loading image: " + cartItem.getFoodImage());


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
                removeItem(getAdapterPosition());
            });
        }
    }

    private void removeItem(int position) {
        CartItem cartItem = cartItems.get(position);
     //   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("CartItem");
        databaseReference.child(cartItem.getFoodName()).removeValue()
                .addOnSuccessListener(unused -> {
                    cartItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartItems.size());
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle possible errors
                Log.e("CartAdapter", "Failed to remove item: " + e.getMessage());
            }
        });
//        cartItems.remove(position);
//        notifyItemRemoved(position);
 //notifyItemRangeChanged(position, cartItems.size());

//        int[] newQuantities = new int[itemQuantity.length - 1];
//        System.arraycopy(itemQuantity, 0, newQuantities, 0, position);
//        System.arraycopy(itemQuantity, position + 1, newQuantities, position, itemQuantity.length - position - 1);
//        itemQuantity = newQuantities;
//
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, cartItems.size());
    }
}
