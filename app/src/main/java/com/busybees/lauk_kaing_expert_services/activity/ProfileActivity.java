package com.busybees.lauk_kaing_expert_services.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.busybees.data.models.ProfileUpdateModel;
import com.busybees.data.vos.Users.ProfileUpdateObj;
import com.busybees.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;

    private ImageView back;
    private CircleImageView profile;
    private TextView profileName, update;
    private EditText phoneNumber, userName, userEmail;
    private ProgressBar progressBar;

    private UserVO userObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_profile);

        networkServiceProvider = new NetworkServiceProvider(this);

        userObj = Utility.query_UserProfile(this);

        back = findViewById(R.id.back_button);
        profile = findViewById(R.id.profile);
        profileName = findViewById(R.id.profileName);
        phoneNumber = findViewById(R.id.phone);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.email_et);
        update = findViewById(R.id.update_btn);
        progressBar = findViewById(R.id.materialLoader);

        if (userObj != null) {
            profileName.setText(userObj.getUsername());
            phoneNumber.setText(userObj.getPhone());
            userName.setText(userObj.getUsername());
            userEmail.setText(userObj.getEmail());
            loadProfile(userObj.getImage());
        }

        onClick();

    }

    private void loadProfile(String url) {

        if (url != null && !url.isEmpty() && !url.equals("null")){

            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(profile);

        }else{

            Glide.with(this)
                    .load(R.drawable.profile_default_image)
                    .into(profile);

        }
    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        profile.setOnClickListener(v -> {

        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = phoneNumber.getText().toString().trim();
                String name = userName.getText().toString().trim();

                if(phone.isEmpty()) {
                    Utility.showToast(getApplicationContext(), getString(R.string.phone_blank));

                } else if(name.isEmpty()) {
                    Utility.showToast(getApplicationContext(), getString(R.string.name_blank));

                } else {

                    ProfileUpdateObj updateObj=new ProfileUpdateObj();
                    updateObj.setPhone(phone);
                    updateObj.setUsername(name);
                    updateObj.setEmail(userEmail.getText().toString());

                    CallProfileUpdate(updateObj);

                }
            }
        });
    }

    private void CallProfileUpdate(ProfileUpdateObj profileUpdateObj) {

        if (Utility.isOnline(this)){

            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.ProfileUpdateCall(ApiConstants.BASE_URL + ApiConstants.GET_EDIT_PROFILE, profileUpdateObj).enqueue(new Callback<ProfileUpdateModel>() {
                @Override
                public void onResponse(Call<ProfileUpdateModel> call, Response<ProfileUpdateModel> response) {

                    if (response.body().getError()==false){
                        progressBar.setVisibility(View.GONE);

                        //finish();
                        Utility.delete_UserProfile(ProfileActivity.this);
                        userName.setText(response.body().getData().getUsername());
                        userEmail.setText(response.body().getData().getEmail());
                        Utility.showToast(ProfileActivity.this,response.body().getMessage());
                        Utility.Save_UserProfile(ProfileActivity.this,response.body().getData());

                    }else {

                        Utility.showToast(ProfileActivity.this,response.body().getMessage());
                    }

                }
                @Override
                public void onFailure(Call<ProfileUpdateModel> call, Throwable t) {
                    Utility.showToast(ProfileActivity.this, t.getMessage());

                }
            });
        }else {
            Utility.showToast(ProfileActivity.this, getString(R.string.no_internet));

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
