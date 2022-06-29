package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.lauk_kaing_expert_services.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ImageView back;
    private CircleImageView profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.back_button);
        profile = findViewById(R.id.profile);

        onClick();

    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        profile.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, LogInActivity.class)));
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
