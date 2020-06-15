package com.quoctran.nagoyalhotel.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quoctran.nagoyalhotel.MainActivity;
import com.quoctran.nagoyalhotel.R;
import com.quoctran.nagoyalhotel.utils.FirebaseUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    private TextView tv_nameShow_editProfile;
    private TextView tv_name_editProfile;
    private TextView tv_phone_editProfile;
    private TextView tv_email_editProfile;
    private MaterialButton btn_updateProfile_editProfile;
    private CircleImageView  civ_avatar_editProfile;
    public static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private ProgressDialog loadingBar;
    private StorageReference userAvatarImage;
    private Uri filePath;
    private TextView titleToolbar;
    private final int PICK_IMAGE_REQUEST = 71;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUp();
        getInfoUserProfile();
        btn_updateProfile_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_updateProfile_editProfile.getText().equals(getString(R.string.edit_profile))) {
                    tv_name_editProfile.setEnabled(true);
                    tv_phone_editProfile.setEnabled(true);
                    btn_updateProfile_editProfile.setText(getString(R.string.saveBtn));

                } else {
                    Map infoUser = new HashMap();
                    infoUser.put("name", tv_name_editProfile.getText().toString());
                    infoUser.put("phone", tv_phone_editProfile.getText().toString());

                    DocumentReference acctionUpdate = DB.collection(FirebaseUtils.PROFILES_REF).document(currentUser.getUid());
                    acctionUpdate.update(infoUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @SuppressLint("ShowToast")
                                @Override
                                public void onSuccess(Void aVoid) {
                                    tv_name_editProfile.setEnabled(false);
                                    tv_phone_editProfile.setEnabled(false);
                                    btn_updateProfile_editProfile.setText(getString(R.string.edit_profile));
                                    Toast.makeText(ProfileActivity.this, getString(R.string.infoSuccessfully), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @SuppressLint("ShowToast")
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, getString(R.string.infoError), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        civ_avatar_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromDevice();
            }
        });
    }
    private void chooseImageFromDevice() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.settingProfileTitle)), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                civ_avatar_editProfile.setImageBitmap(bitmap);
                final StorageReference filePathx = userAvatarImage.child(currentUser.getUid());
                filePathx.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        DocumentReference imageUpdate = DB.collection(FirebaseUtils.PROFILES_REF).document(currentUser.getUid());
                                        imageUpdate.update("avatar", task.getResult().toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @SuppressLint("ShowToast")
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(ProfileActivity.this, getString(R.string.infoSuccessfully), Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @SuppressLint("ShowToast")
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(ProfileActivity.this, getString(R.string.infoError), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingBar.dismiss();
                                Toast.makeText(ProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                loadingBar.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void getInfoUserProfile() {
        FirebaseUtils.getDocument(FirebaseUtils.PROFILES_REF, currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    tv_name_editProfile.setText(Objects.requireNonNull(document.get("name")).toString());
                    tv_phone_editProfile.setText(Objects.requireNonNull(document.get("phone")).toString());
                    tv_email_editProfile.setText(currentUser.getEmail());
                    Picasso.get().load((document.get("avatar")==null)?null : String.valueOf(document.get("avatar"))).into(civ_avatar_editProfile);
                }
            }
        });
    }

    private void setUp() {
        Toolbar mToolbar = findViewById(R.id.profile_app_bar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        titleToolbar = mToolbar.findViewById(R.id.titleToolAppBar);
        titleToolbar.setText(getString(R.string.settingProfileTitle));
        userAvatarImage = FirebaseStorage.getInstance().getReference().child(FirebaseUtils.PROFILES_REF);
        loadingBar = new ProgressDialog(this, R.style.DialogStyleApp);
        currentUser = FirebaseUtils.getCurrentUser();
        tv_name_editProfile = findViewById(R.id.tv_name_editProfile);
        tv_phone_editProfile = findViewById(R.id.tv_phone_editProfile);
        tv_nameShow_editProfile = findViewById(R.id.tv_nameShow_editText);
        tv_email_editProfile = findViewById(R.id.tv_email_editProfile);
        btn_updateProfile_editProfile = findViewById(R.id.btn_updateProfile_editProfile);
        civ_avatar_editProfile = findViewById(R.id.civ_avatar_editProfile);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
