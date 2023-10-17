package com.happy.happenings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import com.happy.happenings.SetGet.UserList;
import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;

public class UserListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<UserList> arrayList;
    UserAdapter adapter;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().setTitle("User List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        recyclerView = findViewById(R.id.user_list_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            UserList list = new UserList();
            list.setId(String.valueOf(i));
            list.setType("User");
            list.setName("Sony");
            list.setEmail("sony@gmail.com");
            list.setContact("9876123450");
            list.setGender("Male");
            list.setCity("Ahmedabad");
            list.setAddress("Ashramroad");
            arrayList.add(list);
        }
        adapter = new UserAdapter(UserListActivity.this, arrayList);
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

    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> {
        Context context;
        ArrayList<UserList> arrayList;
        String sId;
        int iPosition;
        ProgressDialog pd;

        public UserAdapter(UserListActivity adminActivity, ArrayList<UserList> arrayList) {
            this.context = adminActivity;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_user, parent, false);
            return new MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView name, email, contact, gender, city, delete;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.custom_user_name);
                email = itemView.findViewById(R.id.custom_user_email);
                contact = itemView.findViewById(R.id.custom_user_contact);
                gender = itemView.findViewById(R.id.custom_user_gender);
                city = itemView.findViewById(R.id.custom_user_city);
                delete = itemView.findViewById(R.id.custom_user_delete);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            holder.email.setText(arrayList.get(position).getEmail());
            holder.contact.setText(arrayList.get(position).getContact());
            holder.gender.setText(arrayList.get(position).getGender());

            if(sp.getString(ConstantUrl.TYPE,"").equalsIgnoreCase("Admin")){
                holder.delete.setVisibility(View.VISIBLE);
            }
            else{
                holder.delete.setVisibility(View.GONE);
            }

            if (arrayList.get(position).getAddress().equalsIgnoreCase("")) {
                holder.city.setText(arrayList.get(position).getCity());
            } else {
                holder.city.setText(arrayList.get(position).getAddress() + "," + arrayList.get(position).getCity());
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sId = arrayList.get(position).getId();
                    iPosition = position;
                    new CommonMethod(context, "User Deleted Successfully");
                    arrayList.remove(iPosition);
                    adapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }
}