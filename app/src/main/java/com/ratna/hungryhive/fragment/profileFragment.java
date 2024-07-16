package com.ratna.hungryhive.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ratna.hungryhive.R;
import com.ratna.hungryhive.databinding.FragmentProfileBinding;


public class profileFragment extends Fragment {
    private FragmentProfileBinding binding;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        
    }
}