package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.model.CartItem;

public class FoodDescriptionActivity extends AppCompatActivity {
    private String foodName;
    private String foodDescription;
    private String foodImage;
    private String foodPrice;
    private String foodIngredients;
    private Button buttonAddToCart;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_description);
        mAuth = FirebaseAuth.getInstance();

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
        buttonAddToCart = findViewById(R.id.buttonAddToCart);

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

        buttonAddToCart.setOnClickListener(view -> {
            addItemToCart();
        });
    }

    private void addItemToCart() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(mAuth.getCurrentUser().getUid()).child("CartItem");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CartItem cartItem = new CartItem(foodName, foodPrice, foodDescription, foodImage, foodIngredients, 1);
        //databaseReference.child("users").child(userId).child("CartItem").push().setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
        databaseReference.push().setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(FoodDescriptionActivity.this, "Item added into cart successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FoodDescriptionActivity.this, homeScreen.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodDescriptionActivity.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}