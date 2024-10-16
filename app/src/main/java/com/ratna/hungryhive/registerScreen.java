package com.ratna.hungryhive;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.model.UserModel;

public class registerScreen extends AppCompatActivity {
    Button buttonSignup, buttonLoginNow;
    EditText editTextName, editTextEmailAddress, editTextPhone, editTextPassword, editTextConfirmPassword;

    private String Email;
    private String Password;
    private String Name;
    private String Phone;
    private String ConfirmPassword;
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


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editTextPassword.setOnTouchListener((view, motionEvent) -> {
            int DRAWABLE_END = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (isPasswordVisible) {
                        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_show_svgrepo_com, 0);
                    } else {
                        editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eyeoff, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    editTextPassword.setSelection(editTextPassword.getText().length());

                    view.performClick();
                    return true;
                }
            }
            return false;
        });

        editTextConfirmPassword.setOnTouchListener((view, motionEvent) -> {
            int DRAWABLE_END = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (editTextConfirmPassword.getRight() - editTextConfirmPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (isPasswordVisible) {
                        editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_show_svgrepo_com, 0);
                    } else {
                        editTextConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eyeoff, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());

                    view.performClick();
                    return true;
                }
            }
            return false;
        });

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

            if (Name.isEmpty() || Phone.isEmpty() || Email.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (!Email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
            } else if (Phone.length() != 10) {
                Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            } else if (!Password.equals(ConfirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                createAccount(Email, Password);
            }
        });


    }


    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                saveUserData();
                Intent intent = new Intent(registerScreen.this, loginScreen.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        Name = editTextName.getText().toString().trim();
        Phone = editTextPhone.getText().toString().trim();
        Email = editTextEmailAddress.getText().toString().trim();
        Password = editTextPassword.getText().toString().trim();
        ConfirmPassword = editTextConfirmPassword.getText().toString().trim();

        UserModel user = new UserModel(Name, Email, Phone, Password, ConfirmPassword);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(userId).setValue(user);
    }
}