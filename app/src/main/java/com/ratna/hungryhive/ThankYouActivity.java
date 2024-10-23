package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ThankYouActivity extends AppCompatActivity {
    Button buttonGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thank_you);

        buttonGoHome = findViewById(R.id.buttonGoHome);
        buttonGoHome.setOnClickListener(view -> {
            Intent intent = new Intent(ThankYouActivity.this, homeScreen.class);
            startActivity(intent);
            finish();
        });

    }
}