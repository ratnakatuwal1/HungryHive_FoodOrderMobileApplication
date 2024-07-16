package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.MenuAdapter;
import com.ratna.hungryhive.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class searchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MenuAdapter adapter;
    private List<String> menuFoodName = Arrays.asList("Pizza", "Burger", "Pasta", "Pasta", "Pizza", "Burger", "Pasta", "Pasta");
    private List<String> menuFoodPrice = Arrays.asList("100", "200", "300", "400", "100", "200", "300", "400");
    private List<Integer> menuFoodImage = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta, R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);

    private List<String> filteredFoodName = new ArrayList<>();
    private List<String> filteredFoodPrice = new ArrayList<>();
    private List<Integer> filteredFoodImage = new ArrayList<>();

    public searchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        adapter = new MenuAdapter(filteredFoodName, filteredFoodPrice, filteredFoodImage);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        setupSearchView();
        showAllMenu();
        return binding.getRoot();
    }

    private void showAllMenu() {
        filteredFoodName.clear();
        filteredFoodPrice.clear();
        filteredFoodImage.clear();

        filteredFoodName.addAll(menuFoodName);
        filteredFoodPrice.addAll(menuFoodPrice);
        filteredFoodImage.addAll(menuFoodImage);

        adapter.notifyDataSetChanged();

    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterMenuItems(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterMenuItems(s);
                return true;
            }
        });
    }

    private void filterMenuItems(String s) {
        filteredFoodName.clear();
        filteredFoodPrice.clear();
        filteredFoodImage.clear();

        for (int i = 0; i < menuFoodName.size(); i++) {
            String foodName = menuFoodName.get(i);
            if (foodName.toLowerCase().contains(s.toLowerCase())) {
                filteredFoodName.add(menuFoodName.get(i));
                filteredFoodPrice.add(menuFoodPrice.get(i));
                filteredFoodImage.add(menuFoodImage.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

}