package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.model.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void fetchSimilarItems(String foodNames) {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Menu");

        menuRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                Map <String, String> selectedFood = null;

                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String itemFoodName = itemSnapshot.child("foodName").getValue(String.class);
                        if (foodNames.equals(itemFoodName)) {
                            selectedFood = new HashMap<>();
                            selectedFood.put("Description", itemSnapshot.child("foodDescription").getValue(String.class));
                            selectedFood.put("Ingredients", itemSnapshot.child("foodIngredients").getValue(String.class));
                            break;
                        }
                    }

                if (selectedFood != null) {
                    List<Map<String, String>> similarItems = new ArrayList<>();

                    // Find items with similar descriptions or ingredients
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String foodName = itemSnapshot.child("foodName").getValue(String.class);

                        if (!foodNames.equals(foodName)) {
                            String description = itemSnapshot.child("foodDescription").getValue(String.class);
                            String ingredients = itemSnapshot.child("foodIngredients").getValue(String.class);

                            if (isSimilar(selectedFood.get("Description"), description) ||
                                    isSimilar(selectedFood.get("Ingredients"), ingredients)) {
                                Map<String, String> item = new HashMap<>();
                                item.put("name", foodName);
                                item.put("description", description);
                                item.put("image", itemSnapshot.child("foodImage").getValue(String.class));
                                item.put("price", itemSnapshot.child("foodPrice").getValue(String.class));
                                item.put("ingredients", ingredients);
                                similarItems.add(item);
                            }
                        }
                    }

                    // Show the first similar item (or choose randomly)
                    if (!similarItems.isEmpty()) {
                        // Choose a random item from the similar items
                        int randomIndex = (int) (Math.random() * similarItems.size());
                        Map<String, String> recommendedItem = similarItems.get(randomIndex);

                        // Display the recommendation
                        showYouMayLikeDialog(
                                recommendedItem.get("name"),
                                recommendedItem.get("description"),
                                recommendedItem.get("image"),
                                recommendedItem.get("price"),
                                recommendedItem.get("ingredients")
                        );
                    } else {
                        Toast.makeText(FoodDescriptionActivity.this, "No similar items found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FoodDescriptionActivity.this, "Selected food details not found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(FoodDescriptionActivity.this, "Menu data not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(FoodDescriptionActivity.this, "Failed to fetch menu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Helper method to check similarity between two text fields based on common words.
     */
    private boolean isSimilar(String referenceText, String targetText) {
        if (referenceText == null || targetText == null) return false;

        // Convert text to lowercase and split into words
        String[] referenceWords = referenceText.toLowerCase().split("\\s+");
        String[] targetWords = targetText.toLowerCase().split("\\s+");

        int matchCount = 0;
        for (String word : referenceWords) {
            for (String targetWord : targetWords) {
                if (word.equals(targetWord)) {
                    matchCount++;
                }
            }
        }

        // Consider it similar if there's at least one common word
        return matchCount > 0;
    }

    private void showYouMayLikeDialog(String name, String description, String image, String price, String ingredients) {
        if (name == null || description == null || image == null || price == null || ingredients == null) {
            Toast.makeText(FoodDescriptionActivity.this, "Error: Incomplete item data.", Toast.LENGTH_SHORT).show();
            return; // Exit early if any value is null
        }
        // Create a custom dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_you_may_like, null);
        builder.setView(dialogView);

        // Dialog Views
        ImageView imageView = dialogView.findViewById(R.id.imageViewYouMayLike);
        TextView textName = dialogView.findViewById(R.id.textViewYouMayLikeName);
        TextView textDescription = dialogView.findViewById(R.id.textViewYouMayLikeDescription);
        TextView textPrice = dialogView.findViewById(R.id.textViewYouMayLikePrice);
        Button buttonViewDetails = dialogView.findViewById(R.id.buttonViewDetails);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        // Set Data
        Glide.with(this).load(image).into(imageView);
        textName.setText(name);
        textDescription.setText(description);
        textPrice.setText("Price: Rs." + price);

        // Button Actions
        buttonViewDetails.setOnClickListener(v -> {
            // Navigate to FoodDescriptionActivity for the recommended item
            Intent intent = new Intent(FoodDescriptionActivity.this, FoodDescriptionActivity.class);
            intent.putExtra("food_name", name);
            intent.putExtra("food_description", description);
            intent.putExtra("food_image", image);
            intent.putExtra("food_price", price);
            intent.putExtra("food_ingredients", ingredients);
            startActivity(intent);
        });

        buttonCancel.setOnClickListener(v -> {
            // Dismiss the dialog
            dialogView.setVisibility(View.GONE);
        });

        // Create and show the dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}