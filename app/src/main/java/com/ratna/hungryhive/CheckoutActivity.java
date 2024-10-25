package com.ratna.hungryhive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.model.UserModel;

public class CheckoutActivity extends AppCompatActivity {
    TextView editTextName, editTextAddress, editTextPhone, editTextEmailAddress;
    RadioButton payViaKhalti, payViaCashInDelivery;
    TextView textGrandTotalAmount;
    Button buttonConformOrder, buttonCancel;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        payViaKhalti = findViewById(R.id.payViaKhalti);
        payViaCashInDelivery = findViewById(R.id.payViaCashInDelivery);
        textGrandTotalAmount = findViewById(R.id.textGrandTotalAmount);
        buttonConformOrder = findViewById(R.id.buttonConfirmOrder);
        buttonCancel = findViewById(R.id.buttonCancel);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        UserModel userModel = task.getResult().getValue(UserModel.class);
                        if (userModel != null) {
                            editTextName.setText("Name: " + userModel.getName());
                            editTextPhone.setText("Phone: " + userModel.getPhone());
                            editTextEmailAddress.setText("Email: " + userModel.getEmail());
                            editTextAddress.setText("Address: " + userModel.getAddress());
                        }
                    }
                }
            });
        }

        double grandTotalAmount = getIntent().getDoubleExtra("grandTotalAmount", 0.0);
        textGrandTotalAmount.setText(String.format("Rs. %.2f", grandTotalAmount));

        buttonConformOrder.setOnClickListener(view -> {
            if (!payViaKhalti.isChecked() && !payViaCashInDelivery.isChecked()) {
                Toast.makeText(CheckoutActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, homeScreen.class);
            startActivity(intent);
            finish();
        });


    }
}