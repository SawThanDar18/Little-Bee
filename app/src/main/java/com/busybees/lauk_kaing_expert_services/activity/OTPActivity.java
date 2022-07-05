package com.busybees.lauk_kaing_expert_services.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.Timer;
import java.util.TimerTask;

public class OTPActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;

    private TextView phoneNumber, timer;
    private EditText otpCode;
    private Button resendBtn, continueBtn;

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

        if (getIntent() != null) {
            phone_number = getIntent().getStringExtra("phone");
            phoneNumber.setText(phone_number);
        }

        startTimer();
        onClick();
    }

    private void onClick() {

        resendBtn.setOnClickListener(v -> {
            if (phone_number != null){

                otpCode.setText("");
                resendBtn.setVisibility(View.GONE);

                ResendOTP();
                codeTimer.cancel();
                startTimer();

            }else {
                Utility.showToast(OTPActivity.this,getString(R.string.phone_blank));
            }
        });
    }

    private void startTimer() {

        resendBtn.setVisibility(View.GONE);

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
}
