package com.ratna.hungryhive;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class forgetPassword extends AppCompatActivity {
    EditText editTextEmailReset;
    Button buttonSendResetEmail, buttonCancelReset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        editTextEmailReset = findViewById(R.id.editTextEmailReset);
        buttonSendResetEmail = findViewById(R.id.buttonSendResetEmail);
        buttonCancelReset = findViewById(R.id.buttonCancelReset);

        mAuth = FirebaseAuth.getInstance();

        buttonSendResetEmail.setOnClickListener(v -> {
            sendResetEmail();
        });

        buttonCancelReset.setOnClickListener(v -> finish());
    }

    private void sendResetEmail() {
        String email = editTextEmailReset.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(forgetPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(forgetPassword.this, "Reset email sent successfully! Please check your inbox.", Toast.LENGTH_LONG).show();
                        updatePasswordInDatabase(email);
                        finish();
                    } else {
                        Toast.makeText(forgetPassword.this, "Failed to send reset email. Please check the email entered.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(forgetPassword.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updatePasswordInDatabase(String email) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            String newPassword = "newPassword";
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Password updated in Firebase Authentication.", Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    databaseReference.child(userId).child("password").setValue(newPassword);
                    databaseReference.child(userId).child("confirmPassword").setValue(newPassword)
                            .addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    Toast.makeText(this, "Password updated in database successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Failed to update password in database.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "Failed to update password in Firebase Authentication.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error updating password in Firebase Authentication: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
