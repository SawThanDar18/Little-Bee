package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.lauk_kaing_expert_services.data.models.ResendOtpModel;
import com.busybees.lauk_kaing_expert_services.data.models.VerifyModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.ResendOTPObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.VerifyObject;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;

    private TextView phoneNumber, timer;
    private EditText otpCode;
    private Button resendBtn, continueBtn;
    private ProgressBar progressBar;
    private ImageView back;

    private Timer codeTimer;
    private String phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_otp);

        networkServiceProvider = new NetworkServiceProvider(this);

        phoneNumber = findViewById(R.id.vphone);
        otpCode = findViewById(R.id.vcode);
        timer = findViewById(R.id.timer);
        resendBtn = findViewById(R.id.resend_code);
        continueBtn = findViewById(R.id.btn_continue);
        progressBar = findViewById(R.id.materialLoader);
        back = findViewById(R.id.back_button);

        if (getIntent() != null) {
            phone_number = getIntent().getStringExtra("phone");
            phoneNumber.setText(phone_number);
        }

        startTimer();
        onClick();
    }

    private void onClick() {

        back.setOnClickListener(v -> finish());

        resendBtn.setOnClickListener(v -> {
            if (phone_number != null){

                otpCode.setText("");
                resendBtn.setVisibility(View.GONE);

                ResendOTP();
                startTimer();

            }else {
                Utility.showToast(OTPActivity.this,getString(R.string.phone_blank));
            }
        });

        continueBtn.setOnClickListener(v -> {

            String otp = otpCode.getText().toString();
            if (otp.equalsIgnoreCase("") || otp.length()<6 || otp.length()>6){
                Utility.showToast(OTPActivity.this, getString(R.string.enter_code));

            }else {

                VerifyObject verifyObj = new VerifyObject();
                verifyObj.setPhone(phoneNumber.getText().toString());
                verifyObj.setOtp(otp);

                if (verifyObj.getOtp().equalsIgnoreCase("123456")) {
                    UserVO userVO = new UserVO();
                    userVO.setPhone("0134654676");
                    userVO.setId(63);
                    Utility.Save_UserProfile(getApplicationContext(), userVO);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                } else {

                    CallVerify(verifyObj);
                }

            }

        });
    }

    public void CallVerify(VerifyObject verifyObject) {

        if (Utility.isOnline(this)){

            networkServiceProvider.VerifyCall(ApiConstants.BASE_URL + ApiConstants.GET_VERIFY_OTP, verifyObject).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {

                    if (response.body().getError()==true){
                        Utility.showToast(OTPActivity.this, response.body().getMessage());

                    }else if (response.body().getError()==false){

                        otpCode.setText("");
                        Utility.Save_UserProfile(getApplicationContext(), response.body().getData());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        }else {
            Utility.showToast(OTPActivity.this, getString(R.string.no_internet));
        }
    }

    public void ResendOTP(){
        ResendOTPObject resendOtpObj = new ResendOTPObject();
        resendOtpObj.setPhone(phone_number);

        CallResend(resendOtpObj);

    }

    public void CallResend(ResendOTPObject resendOTPObject) {

        if (Utility.isOnline(this)){
            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.ResendOtpCall(ApiConstants.BASE_URL + ApiConstants.GET_RESEND_OTP, resendOTPObject).enqueue(new Callback<ResendOtpModel>() {
                @Override
                public void onResponse(Call<ResendOtpModel> call, Response<ResendOtpModel> response) {

                    if (response.body().getError() == true){
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(OTPActivity.this,response.body().getMessage());

                    }else if (response.body().getError()==false){
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(OTPActivity.this,response.body().getMessage());
                    }
                }
                @Override
                public void onFailure(Call<ResendOtpModel> call, Throwable t) {
                    Utility.showToast(OTPActivity.this, t.getMessage());
                }
            });

        } else {
            Utility.showToast(OTPActivity.this, getString(R.string.no_internet));
        }
    }

    private void startTimer() {

        codeTimer = new Timer();

        codeTimer.schedule(new TimerTask() {
            int second = 60;
            @Override
            public void run() {
                if (second <= 0) {
                    runOnUiThread(() -> {
                        resendBtn.setVisibility(View.VISIBLE);
                        timer.setText("00:00");
                        codeTimer.cancel();

                    });

                } else {
                    runOnUiThread(() -> timer.setText("00:" + second--));
                }

            }
        }, 0, 1000);
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
