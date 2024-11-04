package com.ratna.hungryhive;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhive.model.UserModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

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
                    if (task.isSuccessful()) {
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
            } else if (payViaKhalti.isChecked()) {
                //TODO khalti payment

            } else {
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    databaseReference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                UserModel userModel = task.getResult().getValue(UserModel.class);
                                if (userModel != null) {
                                    generateBillAsPdf(
                                            userModel.getName(),
                                            userModel.getPhone(),
                                            userModel.getEmail(),
                                            userModel.getAddress(),
                                            grandTotalAmount
                                    );
                                    Intent intent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });
                }
            }
        });

        buttonCancel.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, homeScreen.class);
            startActivity(intent);
            finish();
        });
    }

    private void generateBillAsPdf(String name, String phone, String email, String address, double totalAmount) {
        String pdfFileName = "HungryHive_Food_Bill.pdf";
        File pdfFile = new File(getExternalFilesDir(null), pdfFileName);

        try {
            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(12f);

            canvas.drawText("HungryHive Food Order Bill", 80, 40, paint);
            canvas.drawText("Name: " + name, 10, 60, paint);
            canvas.drawText("Phone: " + phone, 10, 80, paint);
            canvas.drawText("Email: " + email, 10, 100, paint);
            canvas.drawText("Address: " + address, 10, 120, paint);
            canvas.drawText("Total Amount: Rs. " + String.format("%.2f", totalAmount), 10, 160, paint);

            pdfDocument.finishPage(page);
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            pdfDocument.close();
            showDownloadNotification(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating bill: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDownloadNotification(File pdfFile) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("orderChannel", "Order Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "orderChannel").setSmallIcon(R.drawable.logo_2).setContentTitle("Order Successful").setContentText("your order have been placed and bill download successfully").setAutoCancel(true);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }

}