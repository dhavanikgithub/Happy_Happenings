package com.happy.happenings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.happy.happenings.Utils.ConstantUrl;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity
{
	//EditText
	EditText edfogetemailId;

	//Button
	Button submit;

	//otp
	String otpNumber;

	//Random nuber
	final int min = 1111;
	final int max = 9999;
	final Random random = new Random();

	SharedPreferences sp;

	private String verificationId;
	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		getSupportActionBar().setTitle("Forgot Password");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mAuth = FirebaseAuth.getInstance();
		sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);
		edfogetemailId = findViewById(R.id.ed_forgot_email);

		submit = findViewById(R.id.reset_button);
		submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (edfogetemailId.getText().toString().trim().equals(""))
				{
					edfogetemailId.setError("Email Id Required");
					return;
				}
				else
				{
					String email = edfogetemailId.getText().toString().trim();

					//email store in sharedPReference
					sp.edit().putString(ConstantUrl.FORGOT_EMAIL_ID, String.valueOf(email)).commit();
					startActivity(new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class));
				}

			}
		});

	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
		if(id==android.R.id.home){
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
