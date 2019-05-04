package com.dragonlegend.kidstories;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.BaseResponse;
import com.dragonlegend.kidstories.Api.Responses.RegistrationResponse;
import com.dragonlegend.kidstories.Model.User;
import com.dragonlegend.kidstories.Model.UserReg;
import com.dragonlegend.kidstories.Model.UserRegResponse;
import com.dragonlegend.kidstories.Utils.MainAplication;
import com.dragonlegend.kidstories.Utils.PermissionManager;
import com.dragonlegend.kidstories.Utils.UploadImage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRegisterActivity extends AppCompatActivity {
    public static final String REGISTER_EMAIL = "register_email";
    public static final String REGISTER_PASSWORD = "register_password";
    private static final int GET_FROM_GALLERY = 3;

    private String mEmail,mPassword,mFirstName,mLastname,mDesignation ="";
    private EditText mFirstNameField,mLastNameField,mPhoneNumberField;
    private Spinner mDesignationField;
    private Button mRegisterButton;
    private String phoneNumber;
    CircularImageView profilePic;
    private static final int STOTAGE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);

//        Intent intent = getIntent();
//        if(intent !=null){
//            mEmail = intent.getStringExtra(REGISTER_EMAIL);
//            mPassword = intent.getStringExtra(REGISTER_PASSWORD);
//        }
        mEmail = Prefs.getString("userRegEmail", "");
        mPassword = Prefs.getString("userRegPassword", "");

        initViews();

    }


    public void initViews(){
        Log.d("TAG", "initViews: " + mEmail + mPassword);
        mFirstNameField = findViewById(R.id.first_name_field);
        mLastNameField = findViewById(R.id.last_name_field);
        mPhoneNumberField = findViewById(R.id.phone_number_field);
        mDesignationField = findViewById(R.id.designation);
        mRegisterButton = findViewById(R.id.register_button);
        profilePic = findViewById(R.id.imageButton);
        profilePic.addShadow();
        profilePic.setBorderWidth(10);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileRegisterActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                mFirstName = mFirstNameField.getText().toString().trim();
                mLastname = mLastNameField.getText().toString().trim();
                phoneNumber = mPhoneNumberField.getText().toString().trim();
                if(mFirstName.isEmpty()){
                    mFirstNameField.setError("First Name is required");
                    return;
                }
                if(mLastname.isEmpty()){
                    mLastNameField.setError("Last Name is required");
                    return;
                }

                if (phoneNumber.isEmpty()){
                    mPhoneNumberField.setError("Phone number is required");
                    return;
                }
                mFirstName = capitalize(mFirstName);
                mLastname = capitalize(mLastname);
                mDesignation = mDesignationField.getSelectedItem().toString().trim();
                register();

                String profilePath = Prefs.getString("profile_path",null).replace(".jpg","");
                if(profilePath!=null){
                    UploadImage.uploadProfilePic(profilePath);
                }

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickPicture();
            }
        });
    }
    private String capitalize(String value){
        return value.substring(0,1)+value.substring(1);
    }

    private void register(){
        Log.d("TAG", "register: " + mFirstName+mLastname+ mEmail+ mPassword + phoneNumber);
//        UserReg user = new UserReg(mFirstName, mLastname, mEmail,phoneNumber,mPassword);


        MainAplication.getApiInterface().registerUser(phoneNumber, mEmail,mPassword,mFirstName,mLastname)
                .enqueue(new Callback<BaseResponse<RegistrationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<RegistrationResponse>> call, Response<BaseResponse<RegistrationResponse>> response) {
                Log.d("TAG", "onResponse: " +response.body());

                if(response.isSuccessful()){

                    assert response.body() != null;
                    RegistrationResponse user = response.body().getData();
                    String token = user.getToken();
                    String user_profile_email = user.getEmail();
                    String user_profile_name = user.getFirstName() +
                            " " + user.getLastName();
                    String user_profile_number = user.getPhone();
//                        String user_profile_id = user.getData().getId();

                    Log.d("TAG", "dataResponse:-> " + token+user_profile_email+user_profile_name+user_profile_number);
                    Prefs.putString("reg_token", "Bearer "+token);
                    Prefs.putString("user_profile_email", user_profile_email);
                    Prefs.putString("user_profile_name", user_profile_name);
                    Prefs.putString("user_profile_number", user_profile_number);
                    Intent intent = new Intent(ProfileRegisterActivity.this,Home.class);
                    startActivity(intent);
                    finish();
                }
                if(response.code() == 409){
                    Toast.makeText(ProfileRegisterActivity.this,"User Already Exists!",Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(ProfileRegisterActivity.this,Login.class);
                    startActivity(intent);
                }
            }



            @Override
            public void onFailure(Call<BaseResponse<RegistrationResponse>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(ProfileRegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                profilePic.setImageBitmap(bitmap);

                assert selectedImage != null;
                Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

                Prefs.putString("profile_path",cursor.getString(idx));

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private void PickPicture() {
        Intent pickPictureIntent = new Intent();
        pickPictureIntent.setType("image/*");
        pickPictureIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(pickPictureIntent,GET_FROM_GALLERY);
    }

}
