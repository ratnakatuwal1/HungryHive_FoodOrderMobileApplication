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
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("CartItem");
//        //DatabaseReference itemRef = databaseReference.child(cartItem.getFoodName());
//        databaseReference.child(cartItem.getFoodName()).removeValue()
//        .addOnSuccessListener(unused -> {
//                    cartItems.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, cartItems.size());
//                    Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // Handle possible errors
//                Log.d("CartAdapter", "Failed to remove item: " + e.getMessage());
//                Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
//
//            }
//        });

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

    private void deleteItem(int positionRetrieve, String uniqueKey) {
            // if (uniqueKey != null) {
            databaseReference.child(uniqueKey).removeValue()
                    .addOnSuccessListener(unused -> {
                        // Successfully removed from Firebase
                        cartItems.remove(positionRetrieve);
                        notifyItemRemoved(positionRetrieve);
                        notifyItemRangeChanged(positionRetrieve, cartItems.size());
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
