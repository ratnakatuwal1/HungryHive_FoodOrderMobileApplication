package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class findFood extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_food);

        button = findViewById(R.id.buttonNext);

        button.setOnClickListener(view -> {
            Intent iNext = new Intent(findFood.this, fastDelivery.class);
            startActivity(iNext);
            finish();
        });
    }
}