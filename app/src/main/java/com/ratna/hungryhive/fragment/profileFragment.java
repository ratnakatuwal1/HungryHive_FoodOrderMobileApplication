package com.ratna.hungryhive.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.ChangePassword;
import com.ratna.hungryhive.EditProfile;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.RecentItemAdapter;
import com.ratna.hungryhive.databinding.FragmentProfileBinding;
import com.ratna.hungryhive.loginScreen;

import java.util.Arrays;
import java.util.List;


public class profileFragment extends Fragment {
    Button buttonEditProfile, buttonLogout, changePasswordButton;
    RecyclerView profileRecycleView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    private FragmentProfileBinding binding;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        RecyclerView profileRecycleView = view.findViewById(R.id.profileRecycleView);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setUserData();

        buttonEditProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), EditProfile.class);
            startActivity(intent);
        });

        changePasswordButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ChangePassword.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(view1 -> {
            mAuth.signOut();
            Intent intent = new Intent(requireContext(), loginScreen.class);
            startActivity(intent);
        });

        List<String> profileFoodName = Arrays.asList("Pizza", "Burger");
        List<Integer> profileFoodImage = Arrays.asList(R.drawable.pizza, R.drawable.burger);
        List<String> profileFoodPrice = Arrays.asList("100", "200");

        RecentItemAdapter adapter = new RecentItemAdapter(profileFoodName, profileFoodImage, profileFoodPrice);
        profileRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        profileRecycleView.setAdapter(adapter);
        return view;
    }

    private void setUserData() {
    }
}