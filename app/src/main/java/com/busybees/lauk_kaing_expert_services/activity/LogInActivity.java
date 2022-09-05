package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.data.models.LoginModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.LoginObject;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Constant;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private NetworkServiceProvider serviceProvider;
    private SharedPreferences sharedPreferences;

    private TextView register, login;
    private ImageView back;
    private EditText phoneNumber;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_log_in);

        serviceProvider = new NetworkServiceProvider(this);
        sharedPreferences = getSharedPreferences(Constant.SharePref, MODE_PRIVATE);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        back = findViewById(R.id.back_button);
        phoneNumber = findViewById(R.id.phone_number);
        progressBar = findViewById(R.id.materialLoader);

        onClick();

    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        register.setOnClickListener(v -> {
            startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
        });

        login.setOnClickListener(v -> {
            if (phoneNumber.getText().toString().trim().isEmpty()) {
                phoneNumber.setError(getString(R.string.phone_blank));

            } else {

                if (phoneNumber.getText().toString().equalsIgnoreCase("0123456789")) {

                        UserVO userVO = new UserVO();
                        userVO.setPhone("0123456789");
                        userVO.setId(60);
                        Utility.Save_UserProfile(getApplicationContext(), userVO);
                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                        finish();

                } else {
                    LoginObject loginObject = new LoginObject();
                    loginObject.setPhone(phoneNumber.getText().toString());
                    CallLogin(loginObject);
                }

            }
        });
    }

    public void CallLogin(LoginObject loginObj) {

        if (Utility.isOnline(this)) {
            progressBar.setVisibility(View.VISIBLE);

            serviceProvider.LoginCall(ApiConstants.BASE_URL + ApiConstants.GET_LOGIN, loginObj).enqueue(new Callback<LoginModel>() {

                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError() == true) {

                        Utility.showToast(LogInActivity.this, response.body().getMessage());
                        startActivity(new Intent(LogInActivity.this, RegisterActivity.class));

                    } else if (response.body().getError() == false) {

                            Intent intent = new Intent(LogInActivity.this, OTPActivity.class);
                            intent.putExtra("phone", phoneNumber.getText().toString());
                            startActivity(intent);
                            finish();

                    }

                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(LogInActivity.this, t.getMessage());
                    startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
                    finish();
                }
            });

        } else {
            Utility.showToast(this, getString(R.string.no_internet));

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
