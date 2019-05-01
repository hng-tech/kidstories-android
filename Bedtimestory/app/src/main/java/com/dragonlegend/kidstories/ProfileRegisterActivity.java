package com.dragonlegend.kidstories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Model.User;

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

        Intent intent = getIntent();
        if(intent !=null){
            mEmail = intent.getStringExtra(REGISTER_EMAIL);
            mPassword = intent.getStringExtra(REGISTER_PASSWORD);
        }
        initViews();
    }

    public void initViews(){
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
                if(mFirstName.isEmpty()){
                    mFirstNameField.setError("First Name is required");
                    return;
                }
                if(mLastname.isEmpty()){
                    mLastNameField.setError("Last Name is required");
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
        User user = new User();
        user.setEmail(mEmail);
        user.setFirstName(mFirstName);
        user.setLastName(mLastname);
        user.setPassword(mPassword);
        //user.setDesignation(mDesignation);

        Client.getInstance().create(ApiInterface.class).registerUser(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()){

                    if(response.code() == 200){

                        Intent intent = new Intent(ProfileRegisterActivity.this,Login.class);
                        startActivity(intent);
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
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
