package com.example.bedtime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    EditText login_emailField ,login_passwordField;
    Button login_btn ;
    TextView login_emailError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

         login_emailField = findViewById(R.id.reg_email);
          login_passwordField = findViewById(R.id.reg_password);
         login_btn = findViewById(R.id.btn_login);
          login_emailError = findViewById(R.id.email_error);
       login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_emailField.getText().toString().trim();
                String password = login_passwordField.getText().toString().trim();

                //validate form input
                if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    login_emailError.setVisibility(View.VISIBLE);
                    return;
                }
                if(password.isEmpty()){
                    login_passwordField.setError("Password cannot be empty!");
                    return;
                }
                //start Home activity .
                Intent i = new Intent(getBaseContext(), Home.class);
                startActivity(i);
            }
        });
    }
}


