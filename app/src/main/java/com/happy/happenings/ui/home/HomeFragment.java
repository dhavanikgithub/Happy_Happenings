package com.happy.happenings.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.happy.happenings.ProductActivity;
import com.happy.happenings.R;
import com.happy.happenings.SetGet.CategoryList;
import com.happy.happenings.Utils.CommonMethod;
import com.happy.happenings.Utils.ConstantUrl;
import com.happy.happenings.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    Button addEvent;
    TextView eventName, eventCounter;

    LinearLayout eventLayout;

    RecyclerView recyclerView;
    ArrayList<CategoryList> arrayList;
    CategoryAdapter adapter;

    SharedPreferences sp;
    ProgressDialog pd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sp = getActivity().getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        recyclerView = binding.homeRecyclerview;
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        addEvent = binding.homeAddEvent;
        eventName = binding.homeEventName;
        eventCounter = binding.homeEventCountdown;
        eventLayout = binding.homeEventLayout;

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

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

        return root;
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_add_event);
        EditText name = dialog.findViewById(R.id.custom_add_event_name);
        EditText date = dialog.findViewById(R.id.custom_add_event_date);
        Button submit = (Button) dialog.findViewById(R.id.custom_add_event_submit);
        Button cancel = (Button) dialog.findViewById(R.id.custom_add_event_cancel);
        dialog.show();
        dialog.setCancelable(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                date.setText(dateFormat.format(calendar.getTime()));
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Event Name Required");
                } else if (date.getText().toString().equalsIgnoreCase("")) {
                    date.setError("Event Date Required");
                } else {
                    setCounter(name.getText().toString(), date.getText().toString());
                }
            }
        });

        setCounter("Wedding Event", "07-05-2023");

    }

    private void setCounter(String sDate, String sName) {
        eventLayout.setVisibility(View.VISIBLE);
        eventName.setText(sName);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = df.format(c);

        SimpleDateFormat formatterOld = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = null;
        if (date != null) {
            result = formatterNew.format(date);
        }

        Date cDob = null;
        Date fDob = null;
        try {
            cDob = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
            fDob = new SimpleDateFormat("yyyy-MM-dd").parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String[] separated = currentTime.split(":");
        String iHour = separated[0];
        String iMin = separated[1];
        String iSec = separated[2];

        long lHour = Long.parseLong(iHour) * (1000 * 60 * 60);
        long lMin = Long.parseLong(iMin) * (1000 * 60);
        long lSec = Long.parseLong(iSec) * (1000);

        long diff = fDob.getTime() - cDob.getTime() - lHour - lMin - lSec;
        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        int hours = (int) (diff / (1000 * 60 * 60));
        int minutes = (int) (diff / (1000 * 60));
        int seconds = (int) (diff / (1000));

        setTimer(diff);
    }

    private void setTimer(long diff) {
        int time = 0;
        long totalTimeCountInMilliseconds = 60 * diff;

        long timeBlinkInMilliseconds = 30 * 1000;
        startTimer(totalTimeCountInMilliseconds, timeBlinkInMilliseconds);
    }

    private void startTimer(long totalTimeCountInMilliseconds, long timeBlinkInMilliseconds) {
        CountDownTimer countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;

                eventCounter.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                //eventCounter.setText("Time up!");
                eventLayout.setVisibility(View.GONE);
                addEvent.setVisibility(View.VISIBLE);
                /*textViewShowTime.setVisibility(View.VISIBLE);
                buttonStartTime.setVisibility(View.VISIBLE);
                buttonStopTime.setVisibility(View.GONE);
                edtTimerValue.setVisibility(View.VISIBLE);*/
            }

        }.start();

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
                    sp.edit().putString(ConstantUrl.CATEGORY_NAME, arrayList.get(position).getName()).commit();
                    new CommonMethod(context, ProductActivity.class);
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}