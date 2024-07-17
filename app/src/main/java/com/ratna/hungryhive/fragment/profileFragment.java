package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.RecentItemAdapter;
import com.ratna.hungryhive.databinding.FragmentProfileBinding;

import java.util.Arrays;
import java.util.List;


public class profileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        List<String> profileFoodName = Arrays.asList("Pizza", "Burger");
        List<Integer> profileFoodImage = Arrays.asList(R.drawable.pizza, R.drawable.burger);
        List<String> profileFoodPrice = Arrays.asList("100", "200");

        RecentItemAdapter adapter = new RecentItemAdapter(profileFoodName, profileFoodImage, profileFoodPrice);
        binding.profileRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.profileRecycleView.setAdapter(adapter);
        return binding.getRoot();
    }
}