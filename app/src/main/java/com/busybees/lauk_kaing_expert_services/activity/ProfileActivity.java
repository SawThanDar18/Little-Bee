package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ImageView back;
    private CircleImageView profile;
    private TextView profileName;
    private EditText phoneNumber, userName, userEmail;

    private UserVO userObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_profile);

        userObj = Utility.query_UserProfile(this);

        back = findViewById(R.id.back_button);
        profile = findViewById(R.id.profile);
        profileName = findViewById(R.id.profileName);
        phoneNumber = findViewById(R.id.phone);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.email_et);

        if (userObj != null) {
            profileName.setText(userObj.getUsername());
            phoneNumber.setText(userObj.getPhone());
            userName.setText(userObj.getUsername());
            userEmail.setText(userObj.getEmail());
        }

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
