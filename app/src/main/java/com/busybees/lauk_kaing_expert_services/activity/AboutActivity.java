package com.busybees.lauk_kaing_expert_services.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.lauk_kaing_expert_services.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_about);

        back=findViewById(R.id.back_button);

        back.setOnClickListener(v -> finish());
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
