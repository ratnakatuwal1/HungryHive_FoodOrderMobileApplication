package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        filteredMenuItems.clear();
        adapter = new MenuAdapter(requireContext(), filteredMenuItems);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.menuRecyclerView.setAdapter(adapter);
        binding.noItemsTextView.setVisibility(View.GONE);

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
                adapter.notifyDataSetChanged();
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
        // Convert SearchView's input into an EditText to attach a TextWatcher
        int searchEditTextId = binding.searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = binding.searchView.findViewById(searchEditTextId);

        // Add TextWatcher to filter items dynamically
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter items dynamically as the user types
                filterMenuItems(charSequence.toString());
            }

            public void afterTextChanged(Editable editable) {
                // No action needed
            }
        });
    }


    private void filterMenuItems(String query) {
        filteredMenuItems.clear();

        //Linear Search Algorithm
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