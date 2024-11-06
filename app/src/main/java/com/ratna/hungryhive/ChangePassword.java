package com.ratna.hungryhive;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {
    EditText editTextPassword, editTextNewPassword, editTextConfirmPassword;
    Button buttonChangePassword;
    Boolean isPasswordVisible = false;
    private FirebaseAuth mAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextTextPassword3);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);

        mAuth = FirebaseAuth.getInstance();

        buttonChangePassword.setOnClickListener(v -> {
            changePassword();
        });

        editTextPassword.setOnTouchListener((v, motionEvent) -> {
            int DRAWABLE_END = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (isPasswordVisible) {
                        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_show_svgrepo_com, 0);
                    } else {
                        editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeoff, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    editTextPassword.setSelection(editTextPassword.getText().length());

                    v.performClick();
                    return true;
                }
            }
            return false;
        });


        editTextNewPassword.setOnTouchListener((v, motionEvent) -> {
            int DRAWABLE_END = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (editTextNewPassword.getRight() - editTextNewPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (isPasswordVisible) {
                        editTextNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_show_svgrepo_com, 0);
                    } else {
                        editTextNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeoff, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    editTextPassword.setSelection(editTextNewPassword.getText().length());

                    v.performClick();
                    return true;
                }
            }
            return false;
        });

        editTextConfirmPassword.setOnTouchListener((v, motionEvent) -> {
            int DRAWABLE_END = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (editTextConfirmPassword.getRight() - editTextConfirmPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    if (isPasswordVisible) {
                        editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_show_svgrepo_com, 0);
                    } else {
                        editTextConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeoff, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    editTextConfirmPassword.setSelection(editTextConfirmPassword.getText().length());

                    v.performClick();
                    return true;
                }
            }
            return false;
        });

    }


    private void changePassword() {
        String currentPassword = editTextPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All field are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New password and confirm password is not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPassword.equals(confirmPassword)){
            Toast.makeText(this, "New password cannot be the same as the old password", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            String userId = user.getUid();
                            databaseReference.child(userId).child("password").setValue(newPassword);
                            databaseReference.child(userId).child("confirmPassword").setValue(newPassword).addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Failed to update password in database", Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(this, "Authentication failed. Incorrect old password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}