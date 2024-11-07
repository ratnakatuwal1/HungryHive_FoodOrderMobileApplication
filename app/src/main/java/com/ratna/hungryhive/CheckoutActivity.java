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
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

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
                generateBillAsPdf();

                // Navigate to Thank You activity
                Intent thankYouIntent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                startActivity(thankYouIntent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, homeScreen.class);
            startActivity(intent);
            finish();
        });
    }

    private void generateBillAsPdf() {
        View billView = getLayoutInflater().inflate(R.layout.bill, null);
        TextView billName = billView.findViewById(R.id.billName);
        TextView billAddress = billView.findViewById(R.id.billAddress);
        TextView billContact = billView.findViewById(R.id.billContact);
        TextView billEmail = billView.findViewById(R.id.billEmail);

        billName.setText("Name: " + editTextName.getText().toString());
        billAddress.setText("Address: " + editTextAddress.getText().toString());
        billContact.setText("Contact: " + editTextPhone.getText().toString());
        billEmail.setText("Email: " + editTextEmailAddress.getText().toString());

        TextView textGrandTotalAmount = billView.findViewById(R.id.textGrandTotalAmount);
        textGrandTotalAmount.setText(String.format("Rs. %.2f", textGrandTotalAmount));

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                billView.getMeasuredWidth(), billView.getMeasuredHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        billView.draw(canvas);
        pdfDocument.finishPage(page);

        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "OrderBill.pdf");
        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            pdfDocument.writeTo(outputStream);
            showDownloadNotification(pdfFile);
            Toast.makeText(this, "Bill PDF generated successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }

    }

    private void showDownloadNotification(File pdfFile) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "bill_download_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Bill Download", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo_2)
                .setContentTitle("Order Successful")
                .setContentText("Your order has been placed and bill download successfully")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());

    }

}