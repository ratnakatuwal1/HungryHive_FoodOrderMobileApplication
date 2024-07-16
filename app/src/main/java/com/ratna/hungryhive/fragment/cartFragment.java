package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.CartAdapter;
import com.ratna.hungryhive.adapter.PopularAdapter;
import com.ratna.hungryhive.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class cartFragment extends Fragment {
    private FragmentCartBinding binding;

    public cartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        List<String> foodNames = Arrays.asList("pizza", "burger", "pasta", "pasta");
        List<Integer> foodImages = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);
        List<String> foodPrices = Arrays.asList("Rs. 100", "Rs. 200", "Rs. 300", "400");

        CartAdapter adapter = new CartAdapter(foodNames, foodPrices, foodImages);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}