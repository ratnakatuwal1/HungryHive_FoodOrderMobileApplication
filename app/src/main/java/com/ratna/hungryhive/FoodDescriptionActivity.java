package com.ratna.hungryhive;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ratna.hungryhive.databinding.ActivityFoodDescriptionBinding;

public class FoodDescriptionActivity extends AppCompatActivity {
    private ActivityFoodDescriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_description);

        binding = ActivityFoodDescriptionBinding.inflate(getLayoutInflater());

        String itemName = getIntent().getStringExtra("item_name");
        String itemPrice = getIntent().getStringExtra("item_price");
        int itemImage = getIntent().getIntExtra("item_image", -1);

        binding.textViewFoodName.setText(itemName);
        binding.imageFood.setImageResource(itemImage);
        binding.textViewDescriptionPara.setText(String.format("Price: %s", itemPrice));

    }
}