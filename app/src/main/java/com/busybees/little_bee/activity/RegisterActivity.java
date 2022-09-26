package com.busybees.little_bee.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.little_bee.data.SendMessage.SendMessageModel;
import com.busybees.little_bee.data.SendMessage.SendMessageObject;
import com.busybees.little_bee.data.models.RegisterModel;
import com.busybees.little_bee.data.models.VerifyModel;
import com.busybees.little_bee.data.vos.Users.RegisterObj;
import com.busybees.little_bee.R;
import com.busybees.little_bee.network.NetworkServiceProvider;
import com.busybees.little_bee.utility.ApiConstants;
import com.busybees.little_bee.utility.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;

    private ImageView back;
    private EditText phoneNumber, name;
    private TextView register;
    private CheckBox checkBox;
    private ProgressBar progressBar;

    private String phone, userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_register);

        networkServiceProvider = new NetworkServiceProvider(this);

        back = findViewById(R.id.back_button);
        phoneNumber = findViewById(R.id.phone_number);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);
        checkBox = findViewById(R.id.terms_checkbox);
        progressBar = findViewById(R.id.materialLoader);

        onClick();

    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        register.setOnClickListener(v -> {
            phone = phoneNumber.getText().toString().trim();
            userName = name.getText().toString().trim();

            if (userName.isEmpty()) {
                Utility.showToast(this,getString(R.string.name_blank));

            } else if (phone.isEmpty()) {
                Utility.showToast(this,getString(R.string.phone_blank));

            } else if (!userName.isEmpty() && !phone.isEmpty()){

                RegisterObj registerObj = new RegisterObj();
                registerObj.setUsername(userName);
                registerObj.setPhone(phone);
                registerObj.setRegisterSource("android");


                if (checkBox.isChecked() == false) {
                    Utility.showToast(getApplicationContext(), getString(R.string.agree_to));

                } else {

                    CallRegister(registerObj);

                }
            }
        });
    }

    public void CallRegister(RegisterObj registerObj) {

        if (Utility.isOnline(this)) {
            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.RegisterCall(ApiConstants.BASE_URL + ApiConstants.GET_REGISTER, registerObj).enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError() == true) {

                        if (response.body().getStatus() == 1) {
                            CallGenerateOTP();
                        } else {
                            Utility.showToast(getApplicationContext(), response.body().getMessage());
                        }

                    } else if (response.body().getError() == false) {

                        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                        intent.putExtra("phone", phoneNumber.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<RegisterModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });
        } else {
            Utility.showToast(RegisterActivity.this, getString(R.string.no_internet));
        }
    }

    private void CallGenerateOTP() {
        String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));

        RegisterObj registerObj=new RegisterObj();
        registerObj.setUsername(userName);
        registerObj.setPhone(phone);
        registerObj.setRegisterSource("android");
        registerObj.setOtp(otp);

        CallSendOTP(registerObj);

    }

    private void CallSendOTP(RegisterObj obj) {
        if (Utility.isOnline(getApplicationContext())) {
            networkServiceProvider.CallSendOTPRegister(ApiConstants.BASE_URL + ApiConstants.get_send_otp_register, obj).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    if (response.body().getError() == false) {
                        CallSendMessage(obj);
                    } else {
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });
        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    private void CallSendMessage(RegisterObj obj) {

        String url = ApiConstants.get_send_msg_url;
        String authorization = ApiConstants.get_send_msg_header;

        List<String> phone = new ArrayList<>();
        phone.add(obj.getPhone());

        SendMessageObject sendMessageObject = new SendMessageObject();
        sendMessageObject.setReceivePhoneNumber(phone);
        sendMessageObject.setSenderAddress(ApiConstants.senderAddress);
        sendMessageObject.setMessage("Your OTP code for BusyBees : " + obj.getOtp());
        sendMessageObject.setNotifyURL("");
        sendMessageObject.setSenderName(ApiConstants.senderName);

        if (Utility.isOnline(getApplicationContext())) {
            networkServiceProvider.CallSendMessage(url, authorization, sendMessageObject).enqueue(new Callback<SendMessageModel>() {
                @Override
                public void onResponse(Call<SendMessageModel> call, Response<SendMessageModel> response) {
                    if (response.code() == 200) {
                        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                        intent.putExtra("phone", obj.getPhone());
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<SendMessageModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });
        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
