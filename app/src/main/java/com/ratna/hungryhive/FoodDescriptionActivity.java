package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.model.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDescriptionActivity extends AppCompatActivity {
    private String foodName, foodDescription, foodImage, foodPrice, foodIngredients;
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
                fetchSimilarItems(foodName);
                //Intent intent = new Intent(FoodDescriptionActivity.this, homeScreen.class);
              //  startActivity(intent);
                //finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodDescriptionActivity.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSimilarItems(String foodName) {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Menu");

        menuRef.get().addOnSuccessListener(dataSnapshot -> {
            if (!dataSnapshot.exists()) {
                Toast.makeText(this, "Menu data not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> selectedFood = null;

            // Find the selected food details
            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                String itemFoodName = itemSnapshot.child("foodName").getValue(String.class);
                if (foodName.equals(itemFoodName)) {
                    selectedFood = new HashMap<>();
                    selectedFood.put("Description", itemSnapshot.child("foodDescription").getValue(String.class));
                    selectedFood.put("Ingredients", itemSnapshot.child("foodIngredients").getValue(String.class));
                    break;
                }
            }

            if (selectedFood == null) {
                Toast.makeText(this, "Selected food details not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Map<String, String>> similarItems = new ArrayList<>();

            // Find similar items in the menu
            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                String otherFoodName = itemSnapshot.child("foodName").getValue(String.class);
                if (otherFoodName == null || foodName.equals(otherFoodName)) continue;

                String description = itemSnapshot.child("foodDescription").getValue(String.class);
                String ingredients = itemSnapshot.child("foodIngredients").getValue(String.class);

                if (isSimilar(selectedFood.get("Description"), description) ||
                        isSimilar(selectedFood.get("Ingredients"), ingredients)) {

                    Map<String, String> item = new HashMap<>();
                    item.put("name", otherFoodName);
                    item.put("description", description);
                    item.put("image", itemSnapshot.child("foodImage").getValue(String.class));
                    item.put("price", itemSnapshot.child("foodPrice").getValue(String.class));
                    item.put("ingredients", ingredients);
                    similarItems.add(item);
                }
            }

            // Show the recommended item
            if (similarItems.isEmpty()) {
                Toast.makeText(this, "No similar items found.", Toast.LENGTH_SHORT).show();
            } else {
                int randomIndex = (int) (Math.random() * similarItems.size());
                Map<String, String> recommendedItem = similarItems.get(randomIndex);
                showYouMayLikeDialog(
                        recommendedItem.get("name"),
                        recommendedItem.get("description"),
                        recommendedItem.get("image"),
                        recommendedItem.get("price"),
                        recommendedItem.get("ingredients")
                );
            }
        }).addOnFailureListener(e ->
                Toast.makeText(FoodDescriptionActivity.this, "Failed to fetch menu: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private boolean isSimilar(String referenceText, String targetText) {
        if (referenceText == null || targetText == null) return false;

        // Convert text to lowercase and split into words
        String[] referenceWords = referenceText.toLowerCase().split("\\s+");
        String[] targetWords = targetText.toLowerCase().split("\\s+");

        // Calculate Jaccard similarity
        int intersectionCount = 0;
        for (String word : referenceWords) {
            for (String targetWord : targetWords) {
                if (word.equals(targetWord)) {
                    intersectionCount++;
                }
            }
        }

        int unionCount = referenceWords.length + targetWords.length - intersectionCount;
        return unionCount > 0 && ((double) intersectionCount / unionCount) >= 0.2; // Threshold: 20% match
    }

    private void showYouMayLikeDialog(String name, String description, String image, String price, String ingredients) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_you_may_like, null);
        builder.setView(dialogView);

        ImageView imageView = dialogView.findViewById(R.id.imageViewYouMayLike);
        TextView textName = dialogView.findViewById(R.id.textViewYouMayLikeName);
        TextView textDescription = dialogView.findViewById(R.id.textViewYouMayLikeDescription);
        TextView textPrice = dialogView.findViewById(R.id.textViewYouMayLikePrice);
        Button buttonViewDetails = dialogView.findViewById(R.id.buttonViewDetails);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        Button buttonAddToCart = dialogView.findViewById(R.id.buttonAddToCart);

        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.pizza) // Add a placeholder if needed
                .error(R.drawable.pizza)           // Add an error image if needed
                .into(imageView);

        textName.setText(name != null ? name : "Unnamed Food");
        textDescription.setText(description != null ? description : "No description available");
        textPrice.setText(price != null ? "Price: Rs." + price : "Price not available");

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        buttonViewDetails.setOnClickListener(v -> {

            Intent intent = new Intent(FoodDescriptionActivity.this, FoodDescriptionActivity.class);
            intent.putExtra("food_name", name);
            intent.putExtra("food_description", description);
            intent.putExtra("food_image", image);
            intent.putExtra("food_price", price);
            intent.putExtra("food_ingredients", ingredients);
            dialog.dismiss();
            startActivity(intent);
            //finish();
        });

        buttonCancel.setOnClickListener(v -> {
            // Dismiss the dialog
            dialog.dismiss();

            // Navigate to homeScreen
            Intent intent = new Intent(FoodDescriptionActivity.this, homeScreen.class);
            intent.putExtra("food_name", name);
            intent.putExtra("food_description", description);
            intent.putExtra("food_image", image);
            intent.putExtra("food_price", price);
            intent.putExtra("food_ingredients", ingredients);
            startActivity(intent);  // Start the homeScreen activity
            finish();  // Optionally close this activity
        });

        buttonAddToCart.setOnClickListener(v -> {
            addItemToCart(name, description, image, price, ingredients);
            Toast.makeText(FoodDescriptionActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addItemToCart(String name, String description, String image, String price, String ingredients) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users")
                .child(mAuth.getCurrentUser().getUid())
                .child("CartItem");

        CartItem cartItem = new CartItem(name, price, description, image, ingredients, 1);

        databaseReference.push().setValue(cartItem)
                .addOnSuccessListener(unused -> {
                    // On success, show the success message
                    Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show();

                    // Start the homeScreen activity after adding the item
                    Intent intent = new Intent(FoodDescriptionActivity.this, homeScreen.class);
                    intent.putExtra("food_name", name);
                    intent.putExtra("food_description", description);
                    intent.putExtra("food_image", image);
                    intent.putExtra("food_price", price);
                    intent.putExtra("food_ingredients", ingredients);
                    startActivity(intent);  // Start the homeScreen activity
                    finish();  // Optionally close this activity
                })
                .addOnFailureListener(e -> {
                    // Show failure message
                    Toast.makeText(this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                });
    }
}