package com.happy.happenings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.happy.happenings.SetGet.AddGalleryImageList;
import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGalleryImageActivity extends AppCompatActivity {

    TextView select, upload;
    RecyclerView recyclerView;
    ArrayList<AddGalleryImageList> addGalleryImageLists;
    AddGalleryImageAdapter addGalleryImageAdapter;

    ImageView backIv;

    int REQUEST_CODE_CHOOSE = 100;
    List<Uri> mSelected;

    ProgressDialog pd;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gallery_image);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);
        getSupportActionBar().setTitle("Add Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        select = findViewById(R.id.add_gallery_image_select);
        upload = findViewById(R.id.add_gallery_image_upload);

        recyclerView = findViewById(R.id.add_gallery_image_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addGalleryImageLists = new ArrayList<AddGalleryImageList>();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(AddGalleryImageActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen._100sdp))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .showPreview(true)
                        .theme(R.style.Matisse_Zhihu)
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(AddGalleryImageActivity.this, "Added Successfully");
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            if (mSelected == null || mSelected.size() == 0) {

            } else {
                for (int i = 0; i < mSelected.size(); i++) {
                    AddGalleryImageList list = new AddGalleryImageList();
                    list.setImageUri(mSelected.get(i));
                    addGalleryImageLists.add(list);
                }
                addGalleryImageAdapter = new AddGalleryImageAdapter(AddGalleryImageActivity.this, addGalleryImageLists);
                recyclerView.setAdapter(addGalleryImageAdapter);
            }
            if (addGalleryImageLists.size() <= 0) {

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                upload.setVisibility(View.VISIBLE);
            }
        }
    }

    private String getImage(Uri uri) {
        /*if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            return path;
        } else {
            return "";
        }*/

        if (uri != null) {
            String path = null;
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor c = managedQuery(uri, s_array, null, null, null);
            int id = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (c.moveToFirst()) {
                do {
                    path = c.getString(id);
                }
                while (c.moveToNext());
                //c.close();
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }

    private class AddGalleryImageAdapter extends RecyclerView.Adapter<AddGalleryImageAdapter.MyHolder> {

        Context context;
        ArrayList<AddGalleryImageList> addGalleryImageLists;
        ProgressDialog pd;
        SharedPreferences sp;
        View view;

        AddGalleryImageAdapter(Context context, ArrayList<AddGalleryImageList> addGalleryImageLists) {
            this.context = context;
            this.addGalleryImageLists = addGalleryImageLists;
            sp = context.getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_add_gallery_image, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            Glide.with(context).load(addGalleryImageLists.get(position).getImageUri()).placeholder(R.mipmap.ic_launcher).into(holder.iv);

            holder.deleteFloat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addGalleryImageLists.remove(position);
                    addGalleryImageAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return addGalleryImageLists.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            ImageView iv;
            FloatingActionButton deleteFloat;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.custom_add_gallery_image_iv);
                deleteFloat = itemView.findViewById(R.id.custom_add_gallery_image_delete);
            }
        }
    }
}