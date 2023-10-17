package com.happy.happenings;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.happenings.SetGet.CategoryList;
import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<CategoryList> arrayList;
    CategoryAdapter adapter;

    SharedPreferences sp;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery2, container, false);
        sp = getActivity().getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.gallery_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CategoryList list = new CategoryList();
            list.setId(String.valueOf(i));
            list.setName("Cloth");
            list.setImage(R.mipmap.ic_launcher);
            arrayList.add(list);
        }
        adapter = new CategoryAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {
        Context context;
        ArrayList<CategoryList> arrayList;
        ProgressDialog pd;

        public CategoryAdapter(FragmentActivity adminActivity, ArrayList<CategoryList> arrayList) {
            this.context = adminActivity;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public CategoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category, parent, false);
            return new CategoryAdapter.MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView name, edit, delete;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.custom_category_iv);
                name = itemView.findViewById(R.id.custom_category_name);
                delete = itemView.findViewById(R.id.custom_category_delete);
                edit = itemView.findViewById(R.id.custom_category_edit);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.MyHolder holder, int position) {
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.name.setText(arrayList.get(position).getName());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.edit().putString(ConstantUrl.CATEGORY_ID, arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.CATEGORY_NAME, arrayList.get(position).getName()).commit();
                    new CommonMethod(context, GalleryActivity.class);
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}