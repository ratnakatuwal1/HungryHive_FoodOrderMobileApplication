package com.ratna.hungryhive;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FoodDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_description);

        String foodName = getIntent().getStringExtra("food_name");
        String foodDescription = getIntent().getStringExtra("food_description");
        int foodImage = getIntent().getIntExtra("food_image", 0);

        TextView textViewFoodName = findViewById(R.id.textViewFoodName);
        TextView textViewDescriptionPara = findViewById(R.id.textViewDescriptionPara);
        ImageView imageFood = findViewById(R.id.imageFood);

        textViewFoodName.setText(foodName);
        textViewDescriptionPara.setText(foodDescription);
        imageFood.setImageResource(foodImage);
    }
}