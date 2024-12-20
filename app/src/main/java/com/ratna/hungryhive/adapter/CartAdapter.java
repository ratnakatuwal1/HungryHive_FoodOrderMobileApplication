package com.ratna.hungryhive.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratna.hungryhive.FoodDescriptionActivity;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.databinding.CartItemBinding;
import com.ratna.hungryhive.model.CartItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userId;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("CartItem");
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (CartItem cartItem : cartItems) {
            totalQuantity += cartItem.getFoodQuantity();
        }
        return totalQuantity;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CartItemBinding binding;

        public CartViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem cartItem) {

//            binding.imageCartFood.setImageResource(image);
//            binding.textQty.setText(String.valueOf(quantity));
            binding.textFoodName.setText(cartItem.getFoodName());
            binding.textFoodAmount.setText(cartItem.getFoodPrice());
            binding.textQty.setText(String.valueOf(cartItem.getFoodQuantity()));

            Glide.with(context)
                    .load(cartItem.getFoodImage())  // Ensure the image URL is valid
                    .placeholder(R.drawable.burger)  // Add a placeholder
                    .error(R.drawable.burger)  // Add an error image in case of failure
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
                removeItem(getAdapterPosition());
            });
        }
    }

    private void removeItem(int position) {
  CartItem cartItem = cartItems.get(position);

        // Retrieve the unique key at the position in Firebase
        getUniqueKeyAtPosition(position, new UniqueKeyCallback() {
            @Override
            public void onUniqueKeyRetrieved(String uniqueKey) {
                if (uniqueKey != null) {
                    deleteItem(position, uniqueKey);  // Call deleteItem using the unique key
                } else {
                    Log.d("CartAdapter", "No unique key found for position: " + position);
                    Toast.makeText(context, "Failed to find item in Firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteItem(int position, String uniqueKey) {
            // if (uniqueKey != null) {
            databaseReference.child(uniqueKey).removeValue()
                    .addOnSuccessListener(unused -> {
                        // Successfully removed from Firebase
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItems.size());
                        Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();

                    }).addOnFailureListener(e -> {
                        // Failed to remove the item
                        Log.d("CartAdapter", "Failed to remove item: " + e.getMessage());
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    });
        }


    private void getUniqueKeyAtPosition(int position, UniqueKeyCallback callback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uniqueKey = null;
                int index = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (index == position) {
                        uniqueKey = dataSnapshot.getKey();
                        break;
                    }
                    index++;
                }
                callback.onUniqueKeyRetrieved(uniqueKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("CartAdapter", "Failed to retrieve unique key: " + error.getMessage());
            }
        });
    }
    public interface UniqueKeyCallback {
        void onUniqueKeyRetrieved(String uniqueKey);
    }
}
