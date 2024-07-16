package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class loginHome extends AppCompatActivity {
    Button buttonLogin;
    Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonLogin.setOnClickListener(view -> {
            Intent iNext = new Intent(loginHome.this, loginScreen.class);
            startActivity(iNext);
        });

        buttonSignup.setOnClickListener(view -> {
            Intent iNext = new Intent(loginHome.this, homeScreen.class);
            startActivity(iNext);
        });
    }
}