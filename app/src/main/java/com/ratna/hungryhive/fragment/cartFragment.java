package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.CartAdapter;
import com.ratna.hungryhive.adapter.PopularAdapter;
import com.ratna.hungryhive.databinding.FragmentCartBinding;
import com.ratna.hungryhive.model.CartItem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class cartFragment extends Fragment {
    private FragmentCartBinding binding;
    private DatabaseReference databaseReference;
    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private String userId;

    public cartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(requireContext(), cartItems);

        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(cartAdapter);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("CartItem");


//        List<String> foodNames = Arrays.asList("pizza", "burger", "pasta", "pasta");
//        List<Integer> foodImages = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);
//        List<String> foodPrices = Arrays.asList("Rs. 100", "Rs. 200", "Rs. 300", "400");

       // CartAdapter adapter = new CartAdapter(foodNames, foodPrices, foodImages);


        retrieveCartItems();
        return binding.getRoot();
    }

    private void retrieveCartItems() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                        if (cartItem != null) {
                            cartItems.add(cartItem);
                            Log.d("CartItem", "Added: " + cartItem.getFoodName());
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                    Log.d("cartFragment", "Data loaded with " + cartItems.size() + " items.");
                } else {
                    Log.d("cartFragment", "No data found for CartItem in Firebase");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Log.d("CartError", "Database error: " + error.getMessage());
            }
        });
    }
}