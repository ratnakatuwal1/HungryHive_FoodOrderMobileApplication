package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ratna.hungryhive.adapter.CartAdapter;
import com.ratna.hungryhive.adapter.MyOrderItemAdapter;
import com.ratna.hungryhive.databinding.ActivityMyOrderBinding;
import com.ratna.hungryhive.model.CartItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity {
    private RecyclerView myOrderRecyclerView;
    private TextView totalFoodAmount, deliveryCharges, grandTotal;
    private Button checkoutButton;
    private ActivityMyOrderBinding binding;
    private MyOrderItemAdapter myOrderItemAdapter;
    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private double totalAmount = 0.0;
    private double deliveryCharge = 100.0; // Example delivery charge
    private double grandTotalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_order);
        binding = ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myOrderRecyclerView = findViewById(R.id.myOrderRecyclerView);
        totalFoodAmount = findViewById(R.id.totalFoodAmount);
        deliveryCharges = findViewById(R.id.deliveryCharges);
        grandTotal = findViewById(R.id.grandTotal);
        checkoutButton = findViewById(R.id.checkoutButton);
        cartItems = new ArrayList<>();

        myOrderRecyclerView = binding.myOrderRecyclerView;
        cartItems = new ArrayList<>();

        String cartItemJson = getIntent().getStringExtra("cartItems");
        if (cartItemJson != null){
            Gson gson = new Gson();
            Type cartItemType = new TypeToken<List<CartItem>>(){}.getType();
            cartItems = gson.fromJson(cartItemJson, cartItemType);

            for (CartItem item : cartItems) {
                totalAmount += Double.parseDouble(item.getFoodPrice()) * item.getFoodQuantity();
            }
        }

        totalFoodAmount.setText(String.format("Rs. %.2f", totalAmount));
        deliveryCharges.setText(String.format("Rs. %.2f", deliveryCharge));
        grandTotalAmount = totalAmount + deliveryCharge;
        grandTotal.setText(String.format("Rs. %.2f", grandTotalAmount));

        // Set up the RecyclerView
        myOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myOrderItemAdapter = new MyOrderItemAdapter(cartItems);
        myOrderRecyclerView.setAdapter(myOrderItemAdapter);

        checkoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(MyOrderActivity.this, CheckoutActivity.class);
            intent.putExtra("grandTotalAmount", grandTotalAmount);

            startActivity(intent);
        });
    }


}