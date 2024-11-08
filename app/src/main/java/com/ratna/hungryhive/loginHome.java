package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginHome extends AppCompatActivity {
    Button buttonLogin;
    Button buttonSignup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        mAuth = FirebaseAuth.getInstance();
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonLogin.setOnClickListener(view -> {
            Intent iNext = new Intent(loginHome.this, loginScreen.class);
            startActivity(iNext);
            finish();
        });

        buttonSignup.setOnClickListener(view -> {
            Intent iNext = new Intent(loginHome.this, registerScreen.class);
            startActivity(iNext);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If user is logged in, navigate to homeScreen
            Intent intent = new Intent(loginHome.this, homeScreen.class);
            startActivity(intent);
            finish(); // Close the login screen
        }
    }
}