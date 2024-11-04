package com.ratna.hungryhive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.model.UserModel;

public class loginScreen extends AppCompatActivity {
    EditText editTextPassword, editTextEmailAddress;
    TextView textForgetPassword;
    Button buttonLogin, buttonSignupNow;
    ImageView imageButtonGoogle;
    private String Name, Email, Phone, password, confirmPassword, address;
    private ActivityResultLauncher<Intent> launcher;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignupNow = findViewById(R.id.buttonSignupNow);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        imageButtonGoogle = findViewById(R.id.imageButtonGoogle);
        textForgetPassword = findViewById(R.id.textForgetPassword);

        textForgetPassword.setOnClickListener(v -> {
            //todo forget password code here.
            // Show an AlertDialog to enter email for password reset
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Reset Password");

            final EditText inputEmail = new EditText(this);
            inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            inputEmail.setHint("Enter your email address");
            builder.setView(inputEmail);

            builder.setPositiveButton("Send", (dialog, which) -> {
                String email = inputEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else {
                    sendPasswordResetEmail(email);
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });



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

        imageButtonGoogle.setOnClickListener(view -> {
            Intent signInIntent = gsc.getSignInIntent();
            launcher.launch(signInIntent);
        });

        buttonLogin.setOnClickListener(view -> {
           String email = editTextEmailAddress.getText().toString().trim();
           String password = editTextPassword.getText().toString().trim();

           if (email.isEmpty() || password.isEmpty()){
               Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
           } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
               Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
           } else {
               loginUser(email, password);
           }
        });

        buttonSignupNow.setOnClickListener(view -> {
            Intent intent = new Intent(loginScreen.this, registerScreen.class);
            startActivity(intent);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(loginScreen.this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Password reset email sent. Check your inbox.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error sending password reset email. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    Toast.makeText(loginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginScreen.this, homeScreen.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(loginScreen.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    saveUserDataGoogle(user);
                    Intent intent = new Intent(loginScreen.this, homeScreen.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(loginScreen.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(loginScreen.this, homeScreen.class);
            startActivity(intent);
            finish();
        }
    }

    private void saveUserDataGoogle(FirebaseUser firebaseUser) {
        Name = firebaseUser.getDisplayName();
        Email = firebaseUser.getEmail();
        Phone = firebaseUser.getPhoneNumber();

        UserModel user = new UserModel(Name, Email, Phone, password, confirmPassword, address, null);
        String userId = firebaseUser.getUid();
        databaseReference.child(userId).setValue(user);
    }
}
