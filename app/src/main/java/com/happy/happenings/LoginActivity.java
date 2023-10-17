package com.happy.happenings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button signup, login;
    TextView forgotPassword;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        signup = findViewById(R.id.login_signup);
        login = findViewById(R.id.login_button);
        forgotPassword = findViewById(R.id.login_forgot);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Email Id Required");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("Password Required");
                } else {
                    if (email.getText().toString().equalsIgnoreCase("sony@gmail.com") && password.getText().toString().equalsIgnoreCase("sony@123")) {
                        new CommonMethod(LoginActivity.this, "Login Successfully");
                        sp.edit().putString(ConstantUrl.ID, "2").commit();
                        sp.edit().putString(ConstantUrl.TYPE, "User").commit();
                        sp.edit().putString(ConstantUrl.NAME, "Sony").commit();
                        sp.edit().putString(ConstantUrl.EMAIL, "sony@gmail.com").commit();
                        sp.edit().putString(ConstantUrl.CONTACT, "9876123450").commit();
                        sp.edit().putString(ConstantUrl.PASSWORD, "sony@123").commit();
                        sp.edit().putString(ConstantUrl.GENDER, "Male").commit();
                        sp.edit().putString(ConstantUrl.CITY, "Ahmedabad").commit();
                        sp.edit().putString(ConstantUrl.ADDRESS, "Ashramroad").commit();

                        new CommonMethod(LoginActivity.this, HomeActivity.class);
                    } else if (email.getText().toString().equalsIgnoreCase("admin@gmail.com") && password.getText().toString().equalsIgnoreCase("admin@123")) {
                        new CommonMethod(LoginActivity.this, "Login Successfully");
                        sp.edit().putString(ConstantUrl.ID, "1").commit();
                        sp.edit().putString(ConstantUrl.TYPE, "Admin").commit();
                        sp.edit().putString(ConstantUrl.NAME, "Admin").commit();
                        sp.edit().putString(ConstantUrl.EMAIL, "admin@gmail.com").commit();
                        sp.edit().putString(ConstantUrl.CONTACT, "9876123451").commit();
                        sp.edit().putString(ConstantUrl.PASSWORD, "admin@123").commit();
                        sp.edit().putString(ConstantUrl.GENDER, "Male").commit();
                        sp.edit().putString(ConstantUrl.CITY, "Ahmedabad").commit();
                        sp.edit().putString(ConstantUrl.ADDRESS, "Ashramroad").commit();

                        new CommonMethod(LoginActivity.this, AdminActivity.class);
                    } else {
                        new CommonMethod(LoginActivity.this, "Login Unsuccessfully");
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //onBackPressed();
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}