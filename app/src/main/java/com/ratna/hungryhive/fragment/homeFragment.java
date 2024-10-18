package com.ratna.hungryhive.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import android.view.LayoutInflater;
import com.ratna.hungryhive.model.MenuItem;
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
import com.ratna.hungryhive.PopularItemWorker;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.PopularAdapter;
import com.ratna.hungryhive.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class homeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private List<MenuItem> menuItems;
    private DatabaseReference databaseReference;
    private PopularAdapter popularAdapter;
    private Context context;

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(PopularItemWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(calculateDelayUntilMidnight(), TimeUnit.MILLISECONDS) // Schedule for midnight
                .build();

        WorkManager.getInstance(requireContext()).enqueue(workRequest);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Menu");
        menuItems = new ArrayList<>();

        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        ImageSlider imageSlider = binding.imageSlider;
        imageSlider.setImageList(imageList);
        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        loadPopularItems();

//        List<String> foodNames = Arrays.asList("pizza", "burger", "pasta", "pasta");
//        List<String> foodDescription = Arrays.asList("Delicious beef burger", "Cheesy pizza", "Creamy pasta", "Delicious beef burger");
//        List<Integer> foodImages = Arrays.asList(R.drawable.pizza, R.drawable.burger, R.drawable.pasta, R.drawable.pasta);
//        List<String> foodPrices = Arrays.asList("Rs. 100", "Rs. 200", "Rs. 300", "400");

//        PopularAdapter adapter = new PopularAdapter(requireContext(), foodNames, foodDescription, foodImages, foodPrices);
//        binding.PopularRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        binding.PopularRecycleView.setAdapter(adapter);
        return binding.getRoot();
    }

    private long calculateDelayUntilMidnight() {
        Calendar current = Calendar.getInstance();
        Calendar nextMidnight = (Calendar) current.clone();
        nextMidnight.set(Calendar.HOUR_OF_DAY, 0);
        nextMidnight.set(Calendar.MINUTE, 0);
        nextMidnight.set(Calendar.SECOND, 0);
        nextMidnight.add(Calendar.DAY_OF_YEAR, 1);

        return nextMidnight.getTimeInMillis() - current.getTimeInMillis();
    }

    private void loadPopularItems() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MenuItem menuItem = dataSnapshot.getValue(MenuItem.class);
                    menuItems.add(menuItem);
                }

                // Shuffle the items and pick 4
                Collections.shuffle(menuItems);
                List<MenuItem> popularItems = menuItems.subList(0, Math.min(menuItems.size(), 4));

                // Set the adapter for the Popular items
                popularAdapter = new PopularAdapter(requireContext(), popularItems);
                binding.PopularRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.PopularRecycleView.setAdapter(popularAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}