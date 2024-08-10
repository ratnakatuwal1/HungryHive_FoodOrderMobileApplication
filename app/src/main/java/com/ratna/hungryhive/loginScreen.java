package com.ratna.hungryhive;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class loginScreen extends AppCompatActivity {
   boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        EditText passwordEditText = findViewById(R.id.editTextPassword);

        passwordEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    int selection = passwordEditText.getSelectionEnd();
                    if (isPasswordVisible) {
                        // Hide Password
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eyeoff, 0, R.drawable.eye_show_svgrepo_com, 0);
                        isPasswordVisible = false;
                    } else {
                        // Show Password
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eyeoff, 0, R.drawable.eye_show_svgrepo_com, 0);
                        isPasswordVisible = true;
                    }
                    passwordEditText.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

    }

}

