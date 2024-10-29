package com.ratna.hungryhive;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    CircleImageView profileImageChange;
    Button buttonChangeProfileImage, buttonSaveChanges;
    EditText editTextName, editTextEmailAddress, editTextPhone, editTextAddress;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        profileImageChange = findViewById(R.id.profileImageChange);
        buttonChangeProfileImage = findViewById(R.id.buttonChangeProfileImage);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        editTextName = findViewById(R.id.editTextName);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }
}