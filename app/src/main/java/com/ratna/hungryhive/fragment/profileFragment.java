package com.ratna.hungryhive.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ratna.hungryhive.ChangePassword;
import com.ratna.hungryhive.EditProfile;
import com.ratna.hungryhive.R;
import com.ratna.hungryhive.adapter.RecentItemAdapter;
import com.ratna.hungryhive.databinding.FragmentProfileBinding;
import com.ratna.hungryhive.loginScreen;
import com.ratna.hungryhive.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {
    Button buttonEditProfile, buttonLogout, changePasswordButton, buttonAddPhoto;
    TextView textFullName, textEmailAddress, textPhone, textAddress, textName, textEmail;
    CircleImageView profileImage;
//    RecyclerView profileRecycleView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            imageUri = result.getData().getData();
            UploadPhotoToFirebase();
        }
    });

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
      //  RecyclerView profileRecycleView = view.findViewById(R.id.profileRecycleView);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);
        buttonAddPhoto = view.findViewById(R.id.buttonAddPhoto);
        textName = view.findViewById(R.id.textName);
        textEmail = view.findViewById(R.id.textEmail);
        textFullName = view.findViewById(R.id.textFullName);
        textEmailAddress = view.findViewById(R.id.textEmailAddress);
        textPhone = view.findViewById(R.id.textPhone);
        textAddress = view.findViewById(R.id.textAddress);
        profileImage = view.findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("profile_photos");
        setUserData();

        buttonAddPhoto.setOnClickListener(view1 -> {
            openGallery();
        });

        buttonEditProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), EditProfile.class);
//            startActivity(intent);
            startActivityForResult(intent, 1);
        });

        changePasswordButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), ChangePassword.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(view1 -> {
            mAuth.signOut();
            Intent intent = new Intent(requireContext(), loginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

//        List<String> profileFoodName = Arrays.asList("Pizza", "Burger");
//        List<Integer> profileFoodImage = Arrays.asList(R.drawable.pizza, R.drawable.burger);
//        List<String> profileFoodPrice = Arrays.asList("100", "200");
//
//        RecentItemAdapter adapter = new RecentItemAdapter(profileFoodName, profileFoodImage, profileFoodPrice);
//        profileRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        profileRecycleView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setUserData();  // Refresh profile data
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    private void UploadPhotoToFirebase(){
        if (imageUri != null){
            String userId = mAuth.getCurrentUser().getUid();
            StorageReference fileRef = storageReference.child(userId + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child(userId).child("profileImageUrl").setValue(uri.toString());
                            Picasso.get().load(uri).into(profileImage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // handle error
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // handle error
                }
            });
        }
    }

    private void setUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        if (userId != null){
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        UserModel userProfile = snapshot.getValue(UserModel.class);
                        if (userProfile != null){
                            textName.setText(userProfile.getName());
                            textEmail.setText(userProfile.getEmail());
                            textFullName.setText("Name: "+userProfile.getName());
                            textEmailAddress.setText("Email: "+userProfile.getEmail());
                            textPhone.setText("Phone: "+userProfile.getPhone());
                            textAddress.setText("Address: "+userProfile.getAddress());

                            if (userProfile.getProfileImageUrl() != null){
                                Picasso.get().load(userProfile.getProfileImageUrl()).into(profileImage);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}