package com.happy.happenings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;
import com.squareup.picasso.Picasso;

public class AddProductActivity extends AppCompatActivity {

    EditText name, price, description;
    ImageView imageView;
    Button select, upload;
    int IMAGE_CODE = 1;
    int STORAGE_CODE = 123;
    Uri filePath;
    String sSelectedPath = "";
    ProgressDialog pd;
    String sImageSelect = "";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        requestStoragePermission();
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        if (sp.getString(ConstantUrl.PRODUCT_ADD_EDIT, "").equalsIgnoreCase("Add")) {
            getSupportActionBar().setTitle("Add Product");
        } else {
            getSupportActionBar().setTitle("Update Product");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.add_product_name);
        price = findViewById(R.id.add_product_price);
        description = findViewById(R.id.add_product_description);
        imageView = findViewById(R.id.add_product_iv);
        select = findViewById(R.id.add_product_select);
        upload = findViewById(R.id.add_product_button);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_CODE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Name Required");
                } else if (price.getText().toString().trim().equalsIgnoreCase("")) {
                    price.setError("Price Required");
                } else if (description.getText().toString().trim().equalsIgnoreCase("")) {
                    description.setError("Description Required");
                } else {
                    if (sp.getString(ConstantUrl.PRODUCT_ADD_EDIT, "").equalsIgnoreCase("Add")) {
                        if (sSelectedPath != "") {
                            new CommonMethod(AddProductActivity.this, "Product Added Successfully");
                            onBackPressed();
                        } else {
                            new CommonMethod(AddProductActivity.this, "Please Select Image");
                        }
                    } else {
                        if (sImageSelect == "") {
                            new CommonMethod(AddProductActivity.this, "Product Update Successfully");
                            onBackPressed();
                        } else {
                            new CommonMethod(AddProductActivity.this, "Product Update Successfully");
                            onBackPressed();
                        }
                    }
                }

            }
        });

        if (sp.getString(ConstantUrl.PRODUCT_ADD_EDIT, "").equalsIgnoreCase("Add")) {

        } else {
            name.setText(sp.getString(ConstantUrl.PRODUCT_NAME, ""));
            price.setText(sp.getString(ConstantUrl.PRODUCT_PRICE, ""));
            description.setText(sp.getString(ConstantUrl.PRODUCT_DESC, ""));
            Picasso.get().load(sp.getString(ConstantUrl.PRODUCT_IMAGE, "")).placeholder(R.mipmap.ic_launcher).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new CommonMethod(AddProductActivity.this, "Permission Granted");
            } else {
                new CommonMethod(AddProductActivity.this, "Permission Denied");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            imageView.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
            filePath = data.getData();
            imageView.setImageURI(filePath);
            Log.d("RESPONSE_URI", String.valueOf(filePath));
            sSelectedPath = getImage(filePath);
            Log.d("RESPONSE_PATH", sSelectedPath);
            sImageSelect = "Yes";
        }
    }

    private String getImage(Uri uri) {
        if (uri != null) {
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, s_array, null, null, null);
            int id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = null;
            if (cursor.moveToFirst()) {
                do {
                    path = cursor.getString(id);
                }
                while (cursor.moveToNext());
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }
}