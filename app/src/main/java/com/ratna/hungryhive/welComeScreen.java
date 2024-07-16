package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class welComeScreen extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come_screen);

        button = findViewById(R.id.buttonGetStarted);

        button.setOnClickListener(view -> {
            Intent iNext = new Intent(welComeScreen.this, loginHome.class);
            startActivity(iNext);
        });
    }
}