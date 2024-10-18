package com.ratna.hungryhive;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FoodDescriptionActivity extends AppCompatActivity {
    private String foodName;
    private String foodDescription;
    private String foodImage;
    private String foodPrice;
    private String foodIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_description);

        // Get the food details from the intent
        foodName = getIntent().getStringExtra("food_name");
        foodDescription = getIntent().getStringExtra("food_description");
        foodImage = getIntent().getStringExtra("food_image");
        foodPrice = getIntent().getStringExtra("food_price");
        foodIngredients = getIntent().getStringExtra("food_ingredients");

        TextView textViewFoodName = findViewById(R.id.textViewFoodName);
        TextView textViewDescriptionPara = findViewById(R.id.textViewDescriptionPara);
        TextView textViewFoodPrice = findViewById(R.id.textViewFoodPrice);
        TextView textViewFoodIngredients = findViewById(R.id.textViewIngredientsList);
        ImageView imageViewFood = findViewById(R.id.imageFood);

        textViewFoodName.setText(foodName);
        textViewDescriptionPara.setText(foodDescription);
        textViewFoodPrice.setText("Price: Rs." + foodPrice);
        textViewFoodIngredients.setText(foodIngredients);

        // Load the food image using Glide
        Glide.with(this).load(foodImage).into(imageViewFood);



//        String foodName = getIntent().getStringExtra("food_name");
//        String foodDescription = getIntent().getStringExtra("food_description");
//        int foodImage = getIntent().getIntExtra("food_image", 0);
//
//
//        TextView textViewFoodName = findViewById(R.id.textViewFoodName);
//        TextView textViewDescriptionPara = findViewById(R.id.textViewDescriptionPara);
//        ImageView imageFood = findViewById(R.id.imageFood);
//
//        textViewFoodName.setText(foodName);
//        textViewDescriptionPara.setText(foodDescription);
//        imageFood.setImageResource(foodImage);
    }
}