package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.MenuAdapter;
import com.ratna.hungryhive.databinding.FragmentMenuBinding;

import java.util.Arrays;
import java.util.List;


public class menuFragment extends Fragment {

    private FragmentMenuBinding binding;

    public menuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);

        List<String> foodNames = Arrays.asList("Pizza", "Burger", "Pasta", "Pasta", "Pizza", "Burger", "Pasta", "Pasta");
        List<String> foodPrices = Arrays.asList("100", "200", "300", "400", "100", "200", "300", "400");
        List<String> foodDescription = Arrays.asList("Delicious beef burger", "Cheesy pizza", "Creamy pasta", "Delicious beef burger", "Cheesy pizza", "Creamy pasta", "Delicious beef burger", "Cheesy pizza");
        List<Integer> foodImages = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta, R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);

        MenuAdapter adapter = new MenuAdapter(requireContext(), foodNames, foodDescription, foodPrices, foodImages);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);
        return binding.getRoot();
    }
}