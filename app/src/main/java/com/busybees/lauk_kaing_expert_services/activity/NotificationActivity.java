package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Notifications.NotificationAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.Notifications.NotificationsModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.RequestPhoneObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ImageView backButton;
    private RelativeLayout noLogInView, logInView;
    private LinearLayout reloadPage;
    private Button goToLogin, reloadBtn;
    private TextView noData;
    private ProgressBar progressBar;

    private ArrayList<NotificationsModel> notificationsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NotificationAdapter adapter;

    NetworkServiceProvider serviceProvider;
    UserVO userObj = new UserVO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_notification);

        serviceProvider = new NetworkServiceProvider(this);
        userObj = Utility.query_UserProfile(this);

        noData = (TextView) findViewById(R.id.no_data);
        goToLogin = (Button) findViewById(R.id.go_to_login);
        backButton = (ImageView) findViewById(R.id.back_button);
        logInView = (RelativeLayout)findViewById(R.id.loginView);
        noLogInView = (RelativeLayout)findViewById(R.id.no_login_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_noti);
        progressBar = (ProgressBar)findViewById(R.id.materialLoader);
        reloadPage = findViewById(R.id.reload_page);
        reloadBtn = findViewById(R.id.btn_reload_page);

        if (Utility.isOnline(getApplicationContext())) {
            reloadPage.setVisibility(View.GONE);
            logInView.setVisibility(View.VISIBLE);

            if (userObj != null) {
                noLogInView.setVisibility(View.GONE);
                logInView.setVisibility(View.VISIBLE);

                RequestPhoneObject requestPhoneObject = new RequestPhoneObject();
                requestPhoneObject.setPhone(userObj.getPhone());
                //CallNotification(requestPhoneObject);

            } else {
                noLogInView.setVisibility(View.VISIBLE);
                logInView.setVisibility(View.GONE);
            }

        } else {
            reloadPage.setVisibility(View.VISIBLE);
            logInView.setVisibility(View.GONE);
        }

        setUpAdapter();
        onClick();
    }

    public void CallNotification(RequestPhoneObject obj) {

        progressBar.setVisibility(View.VISIBLE);

        if (Utility.isOnline(this)){
            serviceProvider.Notification(ApiConstants.BASE_URL + ApiConstants.GET_NOTIFICATION, obj).enqueue(new Callback<NotificationsModel>() {

                @Override
                public void onResponse(Call<NotificationsModel> call, Response<NotificationsModel> response) {

                    if (response.body().getError() == false) {
                        progressBar.setVisibility(View.GONE);

                        if (response.body().getData().size() != 0) {
                            noData.setVisibility(View.GONE);
                            adapter = new NotificationAdapter(NotificationActivity.this, response.body().getData());
                            layoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } else {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }

                }
                @Override
                public void onFailure(Call<NotificationsModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(getApplicationContext(), t.getMessage());

                }
            });

        }else {
            progressBar.setVisibility(View.GONE);
            Utility.showToast(this, getString(R.string.no_internet));

        }

    }

    private void onClick() {
        backButton.setOnClickListener(v -> finish());

        goToLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LogInActivity.class)));

        reloadBtn.setOnClickListener(v -> {
            if (Utility.isOnline(getApplicationContext())) {
                if (userObj != null) {
                    RequestPhoneObject requestPhoneObject = new RequestPhoneObject();
                    requestPhoneObject.setPhone(userObj.getPhone());
                    //CallNotification(requestPhoneObject);
                }
            } else {
                Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
            }
        });
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
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
