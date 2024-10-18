package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.MenuAdapter;
import com.ratna.hungryhive.databinding.FragmentSearchBinding;
import com.ratna.hungryhive.model.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class searchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MenuAdapter adapter;
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<MenuItem> filteredMenuItems = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

//    private List<String> menuFoodName = Arrays.asList("Pizza", "Burger", "Pasta", "Pasta", "Pizza", "Burger", "Pasta", "Pasta");
//    private List<String> foodDescription = Arrays.asList("Delicious beef burger", "Cheesy pizza", "Creamy pasta", "Delicious beef burger", "Cheesy pizza", "Creamy pasta", "Delicious beef burger", "Cheesy pizza");
//    private List<String> menuFoodPrice = Arrays.asList("100", "200", "300", "400", "100", "200", "300", "400");
//    private List<Integer> menuFoodImage = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta, R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);
//
//    private List<String> filteredFoodName = new ArrayList<>();
//    private List<String> filteredDescription = new ArrayList<>();
//    private List<String> filteredFoodPrice = new ArrayList<>();
//    private List<Integer> filteredFoodImage = new ArrayList<>();

    public searchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Menu");
        adapter = new MenuAdapter(requireContext(), filteredMenuItems);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        fetchMenuItemsFromFirebase();
        setupSearchView();
        return binding.getRoot();
//        populateMenuItems();
//        setupSearchView();
//        return binding.getRoot();
//        setupSearchView();
//        showAllMenu();
//        return binding.getRoot();
    }

    private void fetchMenuItemsFromFirebase() {
//        menuItems.add(new MenuItem("Pizza", "100", "Cheesy pizza", "https://example.com/pizza_image.jpg"));
//        menuItems.add(new MenuItem("Burger", "200", "Delicious beef burger", "https://example.com/burger_image.jpg"));
//        menuItems.add(new MenuItem("Pasta", "300", "Creamy pasta", "https://example.com/pasta_image.jpg"));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MenuItem menuItem = dataSnapshot.getValue(MenuItem.class);
                    menuItems.add(menuItem);
                }
                showAllMenuItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        showAllMenuItems();

    }

    private void showAllMenuItems() {
        filteredMenuItems.clear();
        filteredMenuItems.addAll(menuItems);
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

    private void filterMenuItems(String query) {
        filteredMenuItems.clear();
        for (MenuItem item : menuItems) {
            if (item.getFoodName().toLowerCase().contains(query.toLowerCase())) {
                filteredMenuItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();
        if (filteredMenuItems.isEmpty()) {
            binding.noItemsTextView.setVisibility(View.VISIBLE);  // Show message if no matching items
        } else {
            binding.noItemsTextView.setVisibility(View.GONE);  // Hide message if matching items are found
        }
    }

}