package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class registerScreen extends AppCompatActivity {
    Button buttonSignup;
    EditText editTextName, editTextEmailAddress, editTextPhone, editTextPassword, editTextConfirmPassword;
    TextView textCreateAccount, textGetStarted, textSignupWith, textAlreadyHaveAccount;
    ImageView imageGoogle, imageFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        buttonSignup = findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(view -> {
            Intent intent = new Intent(registerScreen.this, loginScreen.class);
            startActivity(intent);
        });
    }
}