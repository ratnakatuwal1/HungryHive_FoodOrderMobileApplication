package com.ratna.hungryhive;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratna.hungryhive.model.UserModel;

public class registerScreen extends AppCompatActivity {
    Button buttonSignup, buttonLoginNow;
    EditText editTextName, editTextEmailAddress, editTextPhone, editTextPassword, editTextConfirmPassword, editTextAddress;

    private String Email, Name, Phone, Password, ConfirmPassword, Address;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        editTextName = findViewById(R.id.editTextName);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonLoginNow = findViewById(R.id.buttonLoginNow);
        buttonSignup = findViewById(R.id.buttonSignup);
        editTextAddress = findViewById(R.id.editTextAddress);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        setupPasswordVisibilityToggle(editTextPassword);
        setupPasswordVisibilityToggle(editTextConfirmPassword);

        buttonLoginNow.setOnClickListener(view -> {
            Intent intent = new Intent(registerScreen.this, loginScreen.class);
            startActivity(intent);
            finish();
        });

        buttonSignup.setOnClickListener(view -> {
            Name = editTextName.getText().toString().trim();
            Phone = editTextPhone.getText().toString().trim();
            Email = editTextEmailAddress.getText().toString().trim();
            Password = editTextPassword.getText().toString().trim();
            ConfirmPassword = editTextConfirmPassword.getText().toString().trim();
            Address = editTextAddress.getText().toString().trim();

            if (validateInputs()) {
                createAccount(Email, Password);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPasswordVisibilityToggle(EditText editText) {
        editText.setOnTouchListener((view, motionEvent) -> {
            int DRAWABLE_END = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    togglePasswordVisibility(editText);
                    return true;
                }
            }
            return false;
        });
    }

    // Toggle the visibility of the password field
    private void togglePasswordVisibility(EditText editText) {
        if (isPasswordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_show_svgrepo_com, 0);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eyeoff, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        editText.setSelection(editText.getText().length());
    }

    private boolean validateInputs() {
        if (Name.isEmpty() && Email.isEmpty() && Phone.isEmpty() && Password.isEmpty() && ConfirmPassword.isEmpty() && Address.isEmpty()) {
            Toast.makeText(this, "All fields are missing. Please fill in the required information.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Name.isEmpty()) {
            Toast.makeText(this, "Name field is missing. Please enter your name.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Email.isEmpty()) {
            Toast.makeText(this, "Email field is missing. Please enter your email.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(Email)) {
            Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Phone.isEmpty()) {
            Toast.makeText(this, "Phone field is missing. Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPhone(Phone)) {
            Toast.makeText(this, "Invalid Phone Number. It should contain 10 digits.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Password.isEmpty()) {
            Toast.makeText(this, "Password field is missing. Please enter your password.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPassword(Password)) {
            Toast.makeText(this, "Password must be more than 8 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ConfirmPassword.isEmpty()) {
            Toast.makeText(this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Password.equals(ConfirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Address.isEmpty()) {
            Toast.makeText(this, "Address field is missing. Please enter your address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    private boolean isValidPhone(String phone) {
        return phone.length() == 10 && phone.matches("\\d+");
    }

    private boolean isValidPassword(String password) {
        return password.length() > 8;
    }

    private void createAccount(String email, String password) {
        checkIfEmailOrPhoneExists(Email, Phone, exists -> {
            if (exists) {
                Toast.makeText(registerScreen.this, "Email or Phone already in use. Please use different one.", Toast.LENGTH_SHORT).show();
            } else {
                // Create the user if the email or phone is not in use
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserData();
                        Toast.makeText(registerScreen.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(registerScreen.this, loginScreen.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(registerScreen.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void checkIfEmailOrPhoneExists(String email, String phone, OnCheckCompleteListener listener) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listener.onCheckComplete(true);
                } else {
                    // Check if the phone number exists
                    databaseReference.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listener.onCheckComplete(snapshot.exists());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "onCancelled: DatabaseError", error.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: DatabaseError", error.toException());
            }
        });
    }

    private void saveUserData() {
        Name = editTextName.getText().toString().trim();
        Phone = editTextPhone.getText().toString().trim();
        Email = editTextEmailAddress.getText().toString().trim();
        Password = editTextPassword.getText().toString().trim();
        ConfirmPassword = editTextConfirmPassword.getText().toString().trim();
        Address = editTextAddress.getText().toString().trim();
        String role = "user";


        UserModel user = new UserModel(Name, Email, Phone, Password, ConfirmPassword, Address, null, role);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(userId).setValue(user);
    }

    interface OnCheckCompleteListener {
        void onCheckComplete(boolean exists);
    }
}