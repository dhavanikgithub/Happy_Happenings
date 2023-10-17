package com.happy.happenings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// implements PaymentResultListener
public class BookNowActivity extends AppCompatActivity {

    TextView productName, vendorName;
    Button submit;
    EditText qty, functionDate, address, remark, totalAmount, advanceAmount;

    SharedPreferences sp;

    ProgressDialog pd;

    int iPrice = 0;
    int productPrice = 0;
    int iPrice50 = 0;
    String sTransactionId = "";

    private static final String TAG = BookNowActivity.class.getSimpleName();

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        getSupportActionBar().setTitle("Book Now");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        //Checkout.preload(getApplicationContext());

        productName = findViewById(R.id.book_now_product_name);
        vendorName = findViewById(R.id.book_now_vendor_name);
        submit = findViewById(R.id.book_now_continue);
        qty = findViewById(R.id.book_now_function_qty);
        functionDate = findViewById(R.id.book_now_function_date);
        address = findViewById(R.id.book_now_function_address);
        remark = findViewById(R.id.book_now_function_remark);
        advanceAmount = findViewById(R.id.book_now_function_advance);
        totalAmount = findViewById(R.id.book_now_function_total);

        productName.setText(sp.getString(ConstantUrl.PRODUCT_NAME, ""));
        vendorName.setText(sp.getString(ConstantUrl.PRODUCT_VENDOR_NAME, ""));
        productPrice = Integer.parseInt(sp.getString(ConstantUrl.PRODUCT_PRICE, ""));

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                functionDate.setText(format.format(calendar.getTime()));

            }
        };

        functionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BookNowActivity.this, dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (qty.getText().toString().trim().equalsIgnoreCase("") || qty.getText().toString().equals("0")) {
                    submit.setText("Pay Now");
                    totalAmount.setText(getResources().getString(R.string.price_symbol) + "0");
                    advanceAmount.setText(getResources().getString(R.string.price_symbol) + "0");
                } else {
                    iPrice = productPrice * Integer.parseInt(s.toString());
                    iPrice50 = iPrice / 2;
                    totalAmount.setText(getResources().getString(R.string.price_symbol) + iPrice);
                    advanceAmount.setText(getResources().getString(R.string.price_symbol) + iPrice50);
                    submit.setText("Pay Now (" + getResources().getString(R.string.price_symbol) + iPrice50 + ")");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty.getText().toString().trim().equalsIgnoreCase("") || qty.getText().toString().equals("0")) {
                    qty.setError("Quantity Required");
                } else if (functionDate.getText().toString().trim().equalsIgnoreCase("")) {
                    functionDate.setError("Function Name Required");
                } else if (address.getText().toString().trim().equalsIgnoreCase("")) {
                    address.setError("Address Required");
                } else {
                    new CommonMethod(BookNowActivity.this, "Booked Successfully");
                    new CommonMethod(BookNowActivity.this, HomeActivity.class);
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