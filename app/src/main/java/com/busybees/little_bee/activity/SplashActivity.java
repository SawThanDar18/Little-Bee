package com.busybees.little_bee.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.little_bee.MainActivity;
import com.busybees.little_bee.R;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.myatminsoe.mdetect.MDetect;

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

        setContentView(R.layout.activity_splash_new);

        skipLayout = findViewById(R.id.skip_layout);
        skipTimer = findViewById(R.id.skip_timer);

        startTimer();

        onClick();

    }

    private void LanguageChange() {

        int lang_txt = AppStorePreferences.getInt(this, AppENUM.LANG_Txt);

        Locale myLocale;

        if (lang_txt == 1) {

            if (MDetect.INSTANCE.isUnicode()) {
                myLocale = new Locale("it");
            } else {
                myLocale = new Locale("fr");
            }
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } else if (lang_txt == 0) {
            myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } else if (lang_txt == 2) {
            myLocale = new Locale("zh", "CN");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        }
    }

    void startTimer() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int second = 5;

            @Override
            public void run() {
                if (second <= 0) {
                    runOnUiThread(() -> {

                        skipTimer.setText(" ");

                        timer.cancel();
                    });

                } else {
                    //runOnUiThread(() -> skipTimer.setText(" " + second--));
                }

            }
        }, 0, 1000);

        handler.postDelayed(() -> {
            if (AppStorePreferences.getInt(SplashActivity.this, AppENUM.LNG_CON) == 1) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent=new Intent(SplashActivity.this, ChooseLanguageActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private void onClick() {
        skipLayout.setOnClickListener(view -> {
            if (AppStorePreferences.getInt(SplashActivity.this, AppENUM.LNG_CON) == 1) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
            } else {
                Intent intent=new Intent(SplashActivity.this, ChooseLanguageActivity.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
                finish();
            }
        });
    }

    void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
