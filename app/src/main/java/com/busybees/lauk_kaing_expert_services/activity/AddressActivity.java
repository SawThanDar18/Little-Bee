package com.busybees.lauk_kaing_expert_services.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Address.AddressListAdapter;
import com.busybees.lauk_kaing_expert_services.fragments.CartsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity {

    private RecyclerView addressListRecyclerView;
    private AddressListAdapter addressListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView addNewAddress, selectedDate;
    private ImageView back, homePageView, cartPageView;

    private LinearLayout continueLayout, dateLayout, timeLayout;

    Spinner timeSpinnerArray;

    String dateData = "";
    String timeData = "";
    String[] showtimeArray = new String[]{};
    String[] timeArray = new String[]{};
    private static String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "November", "Dec"};

    List<String> numlist = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    private static SimpleDateFormat newDate = new SimpleDateFormat("EEE, d MMM yyyy",  Locale.ENGLISH);
    private static SimpleDateFormat newDate1 = new SimpleDateFormat("yyyy-MM-dd",  Locale.ENGLISH);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_address);

        addressListRecyclerView = findViewById(R.id.address_list_recyclerview);
        addNewAddress = findViewById(R.id.addNewAddress);
        continueLayout = findViewById(R.id.continue_layout);
        dateLayout = findViewById(R.id.layout_date);
        timeLayout = findViewById(R.id.layout_time);
        timeSpinnerArray = findViewById(R.id.timeSpinnerArray);
        selectedDate = findViewById(R.id.selectDate);
        back = findViewById(R.id.back_button);
        homePageView = findViewById(R.id.home_page_btn);
        cartPageView = findViewById(R.id.cart_page_btn);

        timeArray = getResources().getStringArray(R.array.caltimes);
        showtimeArray = getResources().getStringArray(R.array.times);

        setUpAdapter();
        onClick();
    }

    private void setUpAdapter() {
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        addressListRecyclerView.setLayoutManager(layoutManager);

        addressListAdapter= new AddressListAdapter(AddressActivity.this);
        addressListRecyclerView.setAdapter(addressListAdapter);
        addressListAdapter.notifyDataSetChanged();
    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
            startActivity(new Intent(AddressActivity.this, FinalOrderActivity.class));
        });

        dateLayout.setOnClickListener(v -> {
            /*if(!dateData.isEmpty()){
                getDate(this, selectedDate, dateData);
            }*/
        });

        timeLayout.setOnClickListener(v -> {

        });

        timeSpinnerArray.setOnTouchListener((v, event) -> {
           /* if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dataAdapter.setDropDownViewResource(R.layout.spinner_item_popup);
                changeTime();
            }
            return false;*/
            return false;
        });

        addNewAddress.setOnClickListener(v -> {
            startActivity(new Intent(AddressActivity.this, AddNewAddressActivity.class));
        });

        back.setOnClickListener(v -> finish());

        homePageView.setOnClickListener(v -> {
            startActivity(new Intent(AddressActivity.this, MainActivity.class));
            finish();
        });

        cartPageView.setOnClickListener(v -> {
            Intent intent = new Intent(AddressActivity.this, MainActivity.class);
            intent.putExtra("tabPosition", 1);
            startActivity(intent);
            finish();
        });

    }

    private void getDate(Context context, final TextView text, String dateString) {

        String[] dateYear = text.getText().toString().split(",")[1].trim().trim().split(" ");

        int toyear = Integer.parseInt(dateYear[2].trim());
        int tomonth = getMonthByCode(dateYear[1].trim());
        int today = Integer.parseInt(dateYear[0].trim());
        Calendar newdate = Calendar.getInstance();

        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        context.createConfigurationContext(configuration);

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateOfBirth1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                newdate.set(year, monthOfYear, dayOfMonth);

                text.setTag(newDate1.format(newdate.getTime()));
                text.setText(newDate.format(newdate.getTime()));

                changeTime();

            }
        },toyear, tomonth, today);

        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", fromDatePickerDialog);
        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", fromDatePickerDialog);
        SimpleDateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy",Locale.ENGLISH);

        Date d6 = null;
        try {
            d6 = df6.parse(dateString);
            fromDatePickerDialog.getDatePicker().setMinDate(d6.getTime());
            fromDatePickerDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static int getMonthByCode(String month) {
        if (month.equalsIgnoreCase(months[0])) {
            return 0;
        } else if (month.equalsIgnoreCase(months[1])) {
            return 1;
        } else if (month.equalsIgnoreCase(months[2])) {
            return 2;
        } else if (month.equalsIgnoreCase(months[3])) {
            return 3;
        } else if (month.equalsIgnoreCase(months[4])) {
            return 4;
        } else if (month.equalsIgnoreCase(months[5])) {
            return 5;
        } else if (month.equalsIgnoreCase(months[6])) {
            return 6;
        } else if (month.equalsIgnoreCase(months[7])) {
            return 7;
        } else if (month.equalsIgnoreCase(months[8])) {
            return 8;
        } else if (month.equalsIgnoreCase(months[9])) {
            return 9;
        } else if (month.equalsIgnoreCase(months[10])) {
            return 10;
        } else if (month.equalsIgnoreCase(months[11])) {
            return 11;
        } else {
            return -1;
        }
    }

    private void changeTime() {

        SimpleDateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy", Locale.ENGLISH);

        if (!selectedDate.getText().toString().isEmpty()) {
            String new_date_data1 = selectedDate.getText().toString();
            String[] dateArray1 = new_date_data1.split(" ");

            String new_date_data = dateArray1[0] + " " + dateArray1[2] + " " + dateArray1[1] + " " + dateArray1[3];

            try {
                Date strDate = df6.parse(new_date_data);
                Date strDate1 = df6.parse(dateData);

                if (strDate.equals(strDate1)) {
                    dataAdapter = new ArrayAdapter(AddressActivity.this, R.layout.spinner_item_theme, showtimeArray);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item_popup);
                    timeSpinnerArray.setAdapter(dataAdapter);
                    timeSpinnerArray.setSelection(0);

                } else {
                    String[] showtimeArr1 = AddressActivity.this.getResources().getStringArray(R.array.times);
                    if (numlist.size() > 0) {
                        dataAdapter = new ArrayAdapter(AddressActivity.this, R.layout.spinner_item_theme, showtimeArr1);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_item_popup);
                        timeSpinnerArray.setAdapter(dataAdapter);
                        timeSpinnerArray.setSelection(0);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
