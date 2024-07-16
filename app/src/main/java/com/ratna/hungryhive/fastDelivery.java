package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class fastDelivery extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_delivery);

        button = findViewById(R.id.buttonNextWelcome);

        button.setOnClickListener(view -> {
            Intent iNext = new Intent(fastDelivery.this, welComeScreen.class);
            startActivity(iNext);
        });
    }
}