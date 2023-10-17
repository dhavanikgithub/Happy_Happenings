package com.happy.happenings;

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

import com.happy.happenings.SetGet.OrderList;
import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyOrderFragment extends Fragment {

    RecyclerView recyclerView;
    public static ArrayList<OrderList> arrayList;
    OrderAdapter adapter;
    SharedPreferences sp;
    
    public MyOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        sp = getActivity().getSharedPreferences(ConstantUrl.PREF, Context.MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.my_order_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            OrderList list = new OrderList();
            list.setId(String.valueOf(i));
            list.setUserName("Sony");
            list.setUserContact("9876123450");
            list.setVendorName("Vendor Name");
            list.setVendorContact("9090901234");
            list.setPrductName("Wedding Cloth");
            list.setProductPrice("7000");
            list.setProductDesc("Available Wedding Cloth");
            list.setProductImage(R.mipmap.ic_launcher);
            list.setQty("3");
            list.setFunctionDate("07-05-2023");
            list.setAddress("Ashramroad");
            list.setRemark("");
            list.setTotalAmount("21000");
            list.setAdvanceAmount("10500");
            list.setTransactionId("TRN12345");
            list.setCreated_date("03-03-2023 10:00AM");
            arrayList.add(list);
        }
        adapter = new OrderAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);

        return view;
    }
    
    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {
        Context context;
        ArrayList<OrderList> arrayList;
        String sId;
        int iPosition;
        ProgressDialog pd;

        public OrderAdapter(FragmentActivity adminActivity, ArrayList<OrderList> arrayList) {
            this.context = adminActivity;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public OrderAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order, parent, false);
            return new OrderAdapter.MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView orderNo,name,vendorName, userName,totalPrice,advance,remaining,date,functionDate,orderView;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.custom_order_iv);
                orderNo = itemView.findViewById(R.id.custom_order_order_no);
                name = itemView.findViewById(R.id.custom_order_name);
                vendorName = itemView.findViewById(R.id.custom_order_vendor_name);
                userName = itemView.findViewById(R.id.custom_order_user_name);
                totalPrice = itemView.findViewById(R.id.custom_order_totalprice);
                advance = itemView.findViewById(R.id.custom_order_advance);
                remaining = itemView.findViewById(R.id.custom_order_remain);
                date = itemView.findViewById(R.id.custom_order_date);
                orderView = itemView.findViewById(R.id.custom_order_view);
                functionDate = itemView.findViewById(R.id.custom_order_function_date);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull OrderAdapter.MyHolder holder, int position) {
            holder.orderNo.setText("Order No : "+arrayList.get(position).getId());
            holder.name.setText("Product : "+arrayList.get(position).getPrductName()+" ("+arrayList.get(position).getQty()+" Qty)");
            holder.vendorName.setText("Vendor Name : "+arrayList.get(position).getVendorName());
            holder.userName.setText("Customer Name : "+arrayList.get(position).getUserName());
            holder.totalPrice.setText("Total :\n"+context.getResources().getString(R.string.price_symbol) + arrayList.get(position).getTotalAmount());
            holder.advance.setText("Advance :\n"+context.getResources().getString(R.string.price_symbol) + arrayList.get(position).getAdvanceAmount());
            int iRemain = Integer.parseInt(arrayList.get(position).getTotalAmount())-Integer.parseInt(arrayList.get(position).getAdvanceAmount());
            holder.remaining.setText("Remaining :\n"+context.getResources().getString(R.string.price_symbol) + iRemain);
            Picasso.get().load(arrayList.get(position).getProductImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

            holder.date.setText(arrayList.get(position).getCreated_date());
            holder.functionDate.setText("Function Date : "+arrayList.get(position).getFunctionDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp.edit().putString(ConstantUrl.ORDER_ID, arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.ORDER_POSITION, String.valueOf(position)).commit();
                    sp.edit().putString(ConstantUrl.ORDER_PAGE, "Yes").commit();
                    new CommonMethod(context, OrderDetailActivity.class);
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }
}