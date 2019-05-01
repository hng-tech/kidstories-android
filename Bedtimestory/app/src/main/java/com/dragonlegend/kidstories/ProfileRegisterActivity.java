package com.dragonlegend.kidstories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Model.User;
import com.dragonlegend.kidstories.Model.UserReg;
import com.dragonlegend.kidstories.Model.UserRegResponse;
import com.pixplicity.easyprefs.library.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRegisterActivity extends AppCompatActivity {
    public static final String REGISTER_EMAIL = "register_email";
    public static final String REGISTER_PASSWORD = "register_password";

    private String mEmail,mPassword,mFirstName,mLastname,mDesignation ="";
    private EditText mFirstNameField,mLastNameField,mPhoneNumberField;
    private Spinner mDesignationField;
    private Button mRegisterButton;
    private String phoneNumber;

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

            }
        });
    }
    private String capitalize(String value){
        return value.substring(0,1)+value.substring(1);
    }

    private void register(){
        Log.d("TAG", "register: " + mFirstName+mLastname+ mEmail+ mPassword + phoneNumber);
//        UserReg user = new UserReg(mFirstName, mLastname, mEmail,phoneNumber,mPassword);


        Client.getInstance().create(ApiInterface.class).registerUser(phoneNumber, mEmail,mPassword,mFirstName,mLastname).
                enqueue(new Callback<UserRegResponse>() {
            @Override
            public void onResponse(Call<UserRegResponse> call, Response<UserRegResponse> response) {
                Log.d("TAG", "onResponse: " +response.message());

                if(response.isSuccessful()){

                    if(response.code() == 201){
                        UserRegResponse user = response.body();
                        String token = "Bearer" + " " +user.getData().getToken();
                        String user_profile_email = user.getData().getEmail();
                        String user_profile_name = user.getData().getFirst_name() +
                                " " + user.getData().getLast_name();
                        String user_profile_number = user.getData().getPhone();
//                        String user_profile_id = user.getData().getId();

                        Log.d("TAG", "dataResponse:-> " +
                                token+user_profile_email+user_profile_name+user_profile_number);
                        Prefs.putString("token", token);
                        Prefs.putString("user_profile_email", user_profile_email);
                        Prefs.putString("user_profile_name", user_profile_name);
                        Prefs.putString("user_profile_number", user_profile_number);
                        Intent intent = new Intent(ProfileRegisterActivity.this,Login.class);
                        startActivity(intent);
                    }else {
                        Log.d("TAG", "onResponse: " + response.code());
                    }
                }
                if(response.code() == 409){
                    Toast.makeText(ProfileRegisterActivity.this,"User Already Exists!",Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(ProfileRegisterActivity.this,Login.class);
                    startActivity(intent);
                }
            }



            @Override
            public void onFailure(Call<UserRegResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}
