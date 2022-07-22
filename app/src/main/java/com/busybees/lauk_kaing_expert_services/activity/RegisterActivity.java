package com.busybees.lauk_kaing_expert_services.activity;

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

import com.busybees.lauk_kaing_expert_services.data.models.RegisterModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.RegisterObj;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

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
            String phone = phoneNumber.getText().toString().trim();
            String userName = name.getText().toString().trim();

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

                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                        finish();

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
