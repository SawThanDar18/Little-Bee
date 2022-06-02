package com.busybees.lauk_kaing_expert_services.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private LinearLayout skipLayout;
    private TextView skipTimer;

    Handler handler = new Handler();
    Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();

        setContentView(R.layout.activity_splash);

        skipLayout = findViewById(R.id.skip_layout);
        skipTimer = findViewById(R.id.skip_timer);

        startTimer();

        onClick();

    }

    void startTimer() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int second = 5;

            @Override
            public void run() {
                if (second <= 0) {
                    runOnUiThread(() -> {

                        skipTimer.setText("0");

                        timer.cancel();
                    });

                } else {
                    runOnUiThread(() -> skipTimer.setText(" " + second--));
                }

            }
        }, 0, 1000);

        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 5000);
    }

    private void onClick() {
        skipLayout.setOnClickListener(view -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            handler.removeCallbacksAndMessages(null);
            finish();
        });
    }

    void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
