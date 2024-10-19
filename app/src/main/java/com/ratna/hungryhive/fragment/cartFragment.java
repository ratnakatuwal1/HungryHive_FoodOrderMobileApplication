package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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
    private List<CartItem> cartItemList;
    private CartAdapter cartAdapter;

    public cartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("userId").child("CartItem");
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(requireContext(), cartItemList);

        binding = FragmentCartBinding.inflate(inflater, container, false);

//        List<String> foodNames = Arrays.asList("pizza", "burger", "pasta", "pasta");
//        List<Integer> foodImages = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);
//        List<String> foodPrices = Arrays.asList("Rs. 100", "Rs. 200", "Rs. 300", "400");

       // CartAdapter adapter = new CartAdapter(foodNames, foodPrices, foodImages);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(cartAdapter);
        retrieveCartItems();
        return binding.getRoot();
    }

    private void retrieveCartItems() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    cartItemList.add(cartItem);
                }
                cartAdapter.notifyDataSetChanged(); // Notify the adapter about the new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
}