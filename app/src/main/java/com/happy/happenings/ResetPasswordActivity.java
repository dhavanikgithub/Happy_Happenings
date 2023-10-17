package com.happy.happenings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.happy.happenings.Utils.ConstantUrl;

public class ResetPasswordActivity extends AppCompatActivity {
    SharedPreferences sp;
    EditText edNewPassword, edcnfpassword;
    Button submitBTN;
    TextView tvEmail;

    String newPassword, cnfpassword, email;

    private static final String validPassword = "^" + "(?=.*[A-Z])" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{4,}" + "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        //init
        tvEmail = findViewById(R.id.restActivity_email_TV);
        tvEmail.setText(sp.getString(ConstantUrl.FORGOT_EMAIL_ID, ""));
        submitBTN = findViewById(R.id.restActivity_submit_btn);

        edNewPassword = findViewById(R.id.restActivity_new_password);
        edcnfpassword = findViewById(R.id.restActivity_cnf_password);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPassword = edNewPassword.getText().toString().trim();
                cnfpassword = edcnfpassword.getText().toString().trim();

                if (newPassword.equalsIgnoreCase("")) {
                    edNewPassword.setError("Password Required");
                } else if (cnfpassword.equalsIgnoreCase("")) {
                    edcnfpassword.setError("Confirm Password Required");
                    return;
                } else if (!cnfpassword.matches(newPassword)) {
                    edcnfpassword.setError("Password not match");
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Password Changes Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
