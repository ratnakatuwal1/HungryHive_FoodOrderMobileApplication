package com.ratna.hungryhive.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.PopularAdapter;
import com.ratna.hungryhive.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class homeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        ImageSlider imageSlider = binding.imageSlider;
        imageSlider.setImageList(imageList);
        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        List<String> foodNames = Arrays.asList("pizza", "burger", "pasta", "pasta");
        List<String> foodDescription = Arrays.asList("Delicious beef burger", "Cheesy pizza", "Creamy pasta", "Delicious beef burger");
        List<Integer> foodImages = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);
        List<String> foodPrices = Arrays.asList("Rs. 100", "Rs. 200", "Rs. 300", "400");

        PopularAdapter adapter = new PopularAdapter(requireContext(), foodNames, foodDescription, foodImages, foodPrices);
        binding.PopularRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.PopularRecycleView.setAdapter(adapter);
        return binding.getRoot();
    }

}