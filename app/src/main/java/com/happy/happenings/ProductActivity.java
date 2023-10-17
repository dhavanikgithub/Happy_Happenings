package com.happy.happenings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.happy.happenings.SetGet.ProductList;
import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProductList> arrayList;
    ProductAdapter adapter;

    SharedPreferences sp;

    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setTitle("Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        add = findViewById(R.id.product_add);

        if(sp.getString(ConstantUrl.TYPE,"").equalsIgnoreCase("Admin") || sp.getString(ConstantUrl.TYPE,"").equalsIgnoreCase("User")){
            add.setVisibility(View.GONE);
        }
        else{
            add.setVisibility(View.VISIBLE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(ConstantUrl.PRODUCT_ADD_EDIT, "Add").commit();
                sp.edit().putString(ConstantUrl.PRODUCT_ID, "").commit();
                sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_ID, "").commit();
                sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_NAME, "").commit();
                sp.edit().putString(ConstantUrl.PRODUCT_NAME, "").commit();
                sp.edit().putString(ConstantUrl.PRODUCT_PRICE, "").commit();
                sp.edit().putString(ConstantUrl.PRODUCT_DESC, "").commit();
                sp.edit().putString(ConstantUrl.PRODUCT_IMAGE, "").commit();
                new CommonMethod(ProductActivity.this, AddProductActivity.class);
            }
        });

        recyclerView = findViewById(R.id.product_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ProductList list = new ProductList();
            list.setId(String.valueOf(i));
            list.setVendorId(String.valueOf(i));
            list.setVendorName("Vendor Name");
            list.setName("Wedding Cloth");
            list.setPrice("7000");
            list.setDesc("Available Wedding Cloths");
            list.setImage(R.mipmap.ic_launcher);
            arrayList.add(list);
        }
        adapter = new ProductAdapter(ProductActivity.this, arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {
        Context context;
        ArrayList<ProductList> arrayList;
        String sId;
        int iPosition;
        ProgressDialog pd;

        public ProductAdapter(ProductActivity adminActivity, ArrayList<ProductList> arrayList) {
            this.context = adminActivity;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ProductAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product, parent, false);
            return new ProductAdapter.MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView name,vendorName, price, edit, delete, book;
            LinearLayout editLayout;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.custom_product_iv);
                name = itemView.findViewById(R.id.custom_product_name);
                vendorName = itemView.findViewById(R.id.custom_product_vendor_name);
                price = itemView.findViewById(R.id.custom_product_price);
                delete = itemView.findViewById(R.id.custom_product_delete);
                edit = itemView.findViewById(R.id.custom_product_edit);
                editLayout = itemView.findViewById(R.id.custom_product_edit_layout);
                book = itemView.findViewById(R.id.custom_product_book);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            holder.vendorName.setText(arrayList.get(position).getVendorName());
            holder.price.setText(context.getResources().getString(R.string.price_symbol) + arrayList.get(position).getPrice());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

            if (sp.getString(ConstantUrl.TYPE, "").equalsIgnoreCase("Admin")) {
                holder.editLayout.setVisibility(View.GONE);
                holder.book.setVisibility(View.GONE);
            } else if (sp.getString(ConstantUrl.TYPE, "").equalsIgnoreCase("User")) {
                holder.editLayout.setVisibility(View.GONE);
                holder.book.setVisibility(View.VISIBLE);
            } else {
                holder.editLayout.setVisibility(View.VISIBLE);
                holder.book.setVisibility(View.GONE);
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sId = arrayList.get(position).getId();
                    iPosition = position;
                    new CommonMethod(context, "Delete Successfully");
                    arrayList.remove(iPosition);
                    adapter.notifyDataSetChanged();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.edit().putString(ConstantUrl.PRODUCT_ADD_EDIT, "Edit").commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_ID, arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_ID, arrayList.get(position).getVendorId()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_NAME, arrayList.get(position).getVendorName()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_NAME, arrayList.get(position).getName()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_PRICE, arrayList.get(position).getPrice()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_DESC, arrayList.get(position).getDesc()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_IMAGE, String.valueOf(arrayList.get(position).getImage())).commit();
                    new CommonMethod(context, AddProductActivity.class);
                }
            });

            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.edit().putString(ConstantUrl.PRODUCT_ID, arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_ID, arrayList.get(position).getVendorId()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_NAME, arrayList.get(position).getVendorName()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_NAME, arrayList.get(position).getName()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_PRICE, arrayList.get(position).getPrice()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_DESC, arrayList.get(position).getDesc()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_IMAGE, String.valueOf(arrayList.get(position).getImage())).commit();
                    new CommonMethod(context, BookNowActivity.class);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.edit().putString(ConstantUrl.PRODUCT_ID, arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_ID, arrayList.get(position).getVendorId()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_VENDOR_NAME, arrayList.get(position).getVendorName()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_NAME, arrayList.get(position).getName()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_PRICE, arrayList.get(position).getPrice()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_DESC, arrayList.get(position).getDesc()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_IMAGE, String.valueOf(arrayList.get(position).getImage())).commit();
                    new CommonMethod(context, ProductDetailActivity.class);
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }
}