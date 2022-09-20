package com.busybees.little_bee.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.little_bee.EventBusModel.GoToCart;
import com.busybees.little_bee.MainActivity;
import com.busybees.little_bee.R;
import com.busybees.little_bee.adapters.Address.AddressListAdapter;
import com.busybees.little_bee.data.models.AddressModel;
import com.busybees.little_bee.data.models.GetAddressModel;
import com.busybees.little_bee.data.models.GetDateTimeModel;
import com.busybees.little_bee.data.vos.Address.AddressVO;
import com.busybees.little_bee.data.vos.Address.DeleteAddressObject;
import com.busybees.little_bee.data.vos.Users.RequestPhoneObject;
import com.busybees.little_bee.data.vos.Users.UserVO;
import com.busybees.little_bee.network.NetworkServiceProvider;
import com.busybees.little_bee.utility.ApiConstants;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    private RecyclerView addressListRecyclerView;
    private AddressListAdapter addressListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView addNewAddress, selectedDate;
    private ImageView back, homePageView, cartPageView;
    private ProgressBar progressBar;

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

    ArrayList<AddressVO> addressVOArrayList = new ArrayList<>();

    private boolean isSelected = false;
    private int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_address);

        networkServiceProvider = new NetworkServiceProvider(this);
        userVO = Utility.query_UserProfile(this);

        progressBar = findViewById(R.id.materialLoader);
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

        if (userVO != null){
            RequestPhoneObject phoneObj = new RequestPhoneObject();
            phoneObj.setPhone(userVO.getPhone());
            CallGetAddress(phoneObj);
            CallGetDateTime();
        }

        setUpAdapter();
        onClick();
    }

    public void EditAddress(AddressVO obj,int position){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            Intent intent = new Intent(this, EditAddressActivity.class);
            intent.putExtra("key", 1);
            intent.putExtra("position", position);
            intent.putExtra("obj", obj);
            startActivity(intent);
        }
    }

    public void DeleteAddress(AddressVO obj){
        DeleteAddressObject deleteAddressObj=new DeleteAddressObject();
        deleteAddressObj.setPhone(userVO.getPhone());
        deleteAddressObj.setAddressId(obj.getId());
        CallDeleteAddress(deleteAddressObj);

    }

    public void CallDeleteAddress(DeleteAddressObject obj) {

        if (Utility.isOnline(this)){

            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.DeleteAddressCall(ApiConstants.BASE_URL + ApiConstants.GET_DELETE_ADDRESS, obj).enqueue(new Callback<AddressModel>() {
                @Override
                public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                    if (response.body().getError()==false){

                        if (userVO != null){

                            RequestPhoneObject phoneObj=new RequestPhoneObject();
                            phoneObj.setPhone(userVO.getPhone());
                            CallGetAddress(phoneObj);
                        }

                    }else if (response.body().getError()==true){
                        Utility.showToast(AddressActivity.this,response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AddressModel> call, Throwable t) {
                    Utility.showToast(AddressActivity.this, t.getMessage());
                }
            });

        }else {
            Utility.showToast(this, getString(R.string.no_internet));
        }
    }

    public void CallGetAddress(RequestPhoneObject phoneObject) {
        if (Utility.isOnline(this)){

            progressBar.setVisibility(View.VISIBLE);
            networkServiceProvider.GetAddressCall(ApiConstants.BASE_URL + ApiConstants.GET_ADDRESS, phoneObject).enqueue(new Callback<GetAddressModel>() {
                @Override
                public void onResponse(Call<GetAddressModel> call, Response<GetAddressModel> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError()==false){

                        addressListAdapter= new AddressListAdapter(AddressActivity.this, response.body().getResult());
                        addressListRecyclerView.setAdapter(addressListAdapter);
                        addressListAdapter.notifyDataSetChanged();

                        addressVOArrayList.clear();
                        addressVOArrayList.addAll(response.body().getResult());

                        addressListAdapter.setCLick(AddressActivity.this);
                        isSelected = true;

                    }else if (response.body().getError()==true){
                        Utility.showToast(AddressActivity.this,response.body().getMessage());
                    }
                }
                @Override
                public void onFailure(Call<GetAddressModel> call, Throwable t) {
                    Utility.showToast(AddressActivity.this, t.getMessage());
                }
            });
        }else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    public void CallGetDateTime() {
        if (Utility.isOnline(this)){

            networkServiceProvider.DateTimeCall(ApiConstants.BASE_URL + ApiConstants.GET_DATE_TIME).enqueue(new Callback<GetDateTimeModel>() {
                @Override
                public void onResponse(Call<GetDateTimeModel> call, Response<GetDateTimeModel> response) {

                    String timeResponse = response.body().getMessage();
                    String[] dateArray = timeResponse.split("-");
                    String[] dateArray1 = dateArray[0].split(" ");

                    dateData = dateArray1[0] + " " + dateArray1[1] + " " + dateArray1[2] + " " + dateArray1[3];
                    timeData = dateArray[1];

                    Utility.setDate1(selectedDate, dateData);
                    String[] showtimeArr1 = getResources().getStringArray(R.array.times);

                    checkTimings(timeArray[0], timeArray[4],showtimeArr1[0]);
                    checkTimings(timeArray[1], timeArray[4],showtimeArr1[1]);
                    checkTimings(timeArray[2], timeArray[4],showtimeArr1[2]);
                    checkTimings(timeArray[3], timeArray[4],showtimeArr1[3]);

                    if (numlist.size() > 0) {
                        showtimeArray = numlist.toArray(new String[numlist.size()]);
                        dataAdapter = new ArrayAdapter(AddressActivity.this, R.layout.spinner_item_theme, showtimeArray);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_item_theme);
                        timeSpinnerArray.setAdapter(dataAdapter);

                    } else {
                        numlist = new ArrayList<String>();
                        String pattern = "hh:mm a";
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
                        try {
                            Date currentDate = sdf.parse(String.valueOf(timeData));

                            dateData = Utility.setDate2(selectedDate, String.valueOf(dateData));
                            changeTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<GetDateTimeModel> call, Throwable t) {
                    Log.e("Error>>",t.getLocalizedMessage());
                }
            });

        }else {
            Utility.showToast(this, getString(R.string.no_internet));
        }
    }

    public void GetPosition(int posi){
        position = posi;
    }

    private boolean checkTimings(String time1, String endtime, String index) {

        String pattern = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date endDate;
        Date currentDate;
        Date arrDate;

        try {
            endDate = sdf.parse(endtime);

            currentDate = sdf.parse(String.valueOf(timeData));
            arrDate = sdf.parse(time1);

            if (currentDate.getTime() <= endDate.getTime() && currentDate.getTime() <= arrDate.getTime()) {

                numlist.add(index);
            } else {

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public  void changeTime() {

        SimpleDateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy", Locale.ENGLISH);

        if (!selectedDate.getText().toString().isEmpty()) {
            String new_date_data1 = selectedDate.getText().toString();
            String[] dateArray1 = new_date_data1.split(" ");

            String new_date_data = dateArray1[0] + " " + dateArray1[2] + " " + dateArray1[1] + " " + dateArray1[3];

            try {
                Date strDate = df6.parse(new_date_data);
                Date strDate1 = df6.parse(dateData);

                if (strDate.equals(strDate1)) {
                    dataAdapter = new ArrayAdapter(AddressActivity.this, R.layout.spinner_item_popup, showtimeArray);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item_popup);
                    timeSpinnerArray.setAdapter(dataAdapter);
                    timeSpinnerArray.setSelection(0);

                } else {
                    String[] showtimeArr1 = AddressActivity.this.getResources().getStringArray(R.array.times);
                    if (numlist.size() > 0) {
                        dataAdapter = new ArrayAdapter(AddressActivity.this, R.layout.spinner_item_popup, showtimeArr1);
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

    private void setUpAdapter() {
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        addressListRecyclerView.setLayoutManager(layoutManager);
    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
            OrderData();
            //startActivity(new Intent(AddressActivity.this, FinalOrderActivity.class));
        });

        dateLayout.setOnClickListener(v -> {
            if(!dateData.isEmpty()){
                getDate(this, selectedDate, dateData);
            }
        });

        timeLayout.setOnClickListener(v -> {

        });

        timeSpinnerArray.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dataAdapter.setDropDownViewResource(R.layout.spinner_item_popup);
                changeTime();
            }
            return false;
        });

        addNewAddress.setOnClickListener(v -> {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                startActivity(new Intent(AddressActivity.this, AddNewAddressActivity.class));
            }
        });

        back.setOnClickListener(v -> finish());

        homePageView.setOnClickListener(v -> {
            startActivity(new Intent(AddressActivity.this, MainActivity.class));
            finish();
        });

        cartPageView.setOnClickListener(v -> {
            GoToCart goToCart = new GoToCart();
            goToCart.setText("go");
            EventBus.getDefault().postSticky(goToCart);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

    }

    private void OrderData(){

        if (isSelected==true) {
            try {
                JSONObject jsonObject = new JSONObject();

                if (addressVOArrayList.isEmpty()) {
                    Utility.showToast(getApplicationContext(), getString(R.string.add_new_address_));
                } else {
                    jsonObject.put("full_address",addressVOArrayList.get(position).getAddress());
                }

                jsonObject.put(AppENUM.KeyName.PHONE_USERID, userVO.getPhone());
                jsonObject.put(AppENUM.KeyName.DATE, selectedDate.getTag().toString());
                jsonObject.put(AppENUM.KeyName.TIME, timeSpinnerArray.getSelectedItem().toString());

                AddressVO addressVO = new AddressVO();
                addressVO.setId(addressVOArrayList.get(position).getId());
                addressVO.setUserId(userVO.getId());
                addressVO.setAddress(addressVOArrayList.get(position).getAddress());
                addressVO.setPhone(userVO.getPhone());
                addressVO.setType(addressVOArrayList.get(position).getType());
                addressVO.setLatitude(addressVOArrayList.get(position).getLatitude());
                addressVO.setLongitude(addressVOArrayList.get(position).getLongitude());
                addressVO.setLocationName(addressVOArrayList.get(position).getLocationName());

                Bundle bundle = new Bundle();
                bundle.putString(AppENUM.KeyName.PRODUCT_DETAIL, jsonObject.toString());

                Intent intent=new Intent(AddressActivity.this, FinalOrderActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("address", addressVO);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Utility.showToast(this, getString(R.string.select_address_first));
        }
    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.gps_alert))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.gps_enable_yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.gps_enable_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
