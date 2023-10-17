package com.happy.happenings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.happy.happenings.SetGet.GalleryImageList;
import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<GalleryImageList> galleryImageLists;
    ImageAdapter galleryImageAdapter;

    ProgressDialog pd;
    SharedPreferences sp;

    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().setTitle("Images");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantUrl.PREF, Context.MODE_PRIVATE);

        add = findViewById(R.id.activity_gallery_add);

        if (sp.getString(ConstantUrl.TYPE, "").equalsIgnoreCase("User")) {
            add.setVisibility(View.GONE);
        } else {
            add.setVisibility(View.VISIBLE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(GalleryActivity.this, AddGalleryImageActivity.class);
            }
        });

        recyclerView = findViewById(R.id.activity_gallery_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(GalleryActivity.this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        galleryImageLists = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            GalleryImageList list = new GalleryImageList();
            list.setId(String.valueOf(i));
            list.setImage(R.mipmap.ic_launcher);
            galleryImageLists.add(list);
        }
        galleryImageAdapter = new ImageAdapter(GalleryActivity.this, galleryImageLists);
        recyclerView.setAdapter(galleryImageAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyHolder> {

        Context context;
        ArrayList<GalleryImageList> galleryImageLists;
        ProgressDialog pd;
        SharedPreferences sp;
        String sGalleryImageId;
        int galleryImagePosition;
        View view;

        ImageAdapter(Context context, ArrayList<GalleryImageList> galleryImageLists) {
            this.context = context;
            this.galleryImageLists = galleryImageLists;
            sp = context.getSharedPreferences(ConstantUrl.PREF, Context.MODE_PRIVATE);
        }

        @NonNull
        @Override
        public ImageAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_admin_gallery_image, parent, false);
            return new ImageAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageAdapter.MyHolder holder, int position) {
            if (sp.getString(ConstantUrl.TYPE, "").equalsIgnoreCase("User")) {
                holder.deleteFloat.setVisibility(View.GONE);
            } else {
                holder.deleteFloat.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(galleryImageLists.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.iv);

            holder.deleteFloat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sGalleryImageId = galleryImageLists.get(position).getId();
                    galleryImagePosition = position;
                    new CommonMethod(context, "Deleted Successfully");
                    galleryImageLists.remove(galleryImagePosition);
                    galleryImageAdapter.notifyDataSetChanged();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.edit().putString(ConstantUrl.GALLER_IMAGE, String.valueOf(galleryImageLists.get(position).getImage())).commit();
                    new CommonMethod(context,GalleryImageDetailActivity.class);
                }
            });

        }

        @Override
        public int getItemCount() {
            return galleryImageLists.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            ImageView iv;
            FloatingActionButton deleteFloat;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.custom_admin_gallery_image_iv);
                deleteFloat = itemView.findViewById(R.id.custom_admin_gallery_image_delete);
            }
        }
    }

}