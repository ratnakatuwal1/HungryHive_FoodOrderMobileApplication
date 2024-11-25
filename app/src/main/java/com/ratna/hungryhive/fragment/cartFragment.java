package com.ratna.hungryhive.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ratna.hungryhive.MyOrderActivity;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.CartAdapter;
import com.ratna.hungryhive.adapter.PopularAdapter;
import com.ratna.hungryhive.databinding.FragmentCartBinding;
import com.ratna.hungryhive.model.CartItem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class cartFragment extends Fragment {
    //private FragmentCartBinding binding;
    private DatabaseReference databaseReference;
    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private FirebaseAuth mAuth;
    private String userId;

    public cartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        RecyclerView cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(requireContext(), cartItems);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartRecyclerView.setAdapter(cartAdapter);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("CartItem");

        Button buttonProceed = view.findViewById(R.id.buttonProceed);
        buttonProceed.setOnClickListener(v -> {
            if (cartItems.isEmpty()){
                Toast.makeText(requireContext(), "Cart is empty. Add item to proceed!", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                String cartItemsJson = gson.toJson(cartItems);
//            Log.d("CartFragment", "Cart Items JSON: " + cartItemsJson);
                getOrderItemDetails(cartItemsJson);
            }
        });
        retrieveCartItems();
        return view;
    }

    private void getOrderItemDetails(String cartItemsJson) {
        int totalQuantity = cartAdapter.getTotalQuantity();
        Toast.makeText(requireContext(), "Total Quantity: " + totalQuantity, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(requireContext(), MyOrderActivity.class);
        intent.putExtra("cartItems", cartItemsJson);
        startActivity(intent);
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