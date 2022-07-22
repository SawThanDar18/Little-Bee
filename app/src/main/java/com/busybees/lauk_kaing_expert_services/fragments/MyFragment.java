package com.busybees.lauk_kaing_expert_services.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.busybees.lauk_kaing_expert_services.data.models.GetUserProfileModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.GetUserProfileObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.Dialog.DialogChangeLanguage;
import com.busybees.lauk_kaing_expert_services.Dialog.DialogLogout;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.activity.ProfileActivity;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.busybees.lauk_kaing_expert_services.EventBusModel.EventBusProfile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFragment extends Fragment {

    private NetworkServiceProvider networkServiceProvider;

    private LinearLayout changeLanguage, loginView, lineLogout, logOut;
    private CardView profileLayout;
    private ImageView profile, profileEditImageView;
    private TextView userName, userPhone;
    private ProgressBar progressBar;

    private UserVO userVO = new UserVO();
    private String profileUrl;
    Uri uri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        networkServiceProvider = new NetworkServiceProvider(getContext());
        userVO = Utility.query_UserProfile(getContext());

        View view = inflater.inflate(R.layout.fragment_my, container, false);

        changeLanguage = view.findViewById(R.id.change_language);
        profileLayout = view.findViewById(R.id.profile_layout);
        loginView = view.findViewById(R.id.loginView);
        lineLogout = view.findViewById(R.id.line_logout);
        logOut = view.findViewById(R.id.logout);
        profile = view.findViewById(R.id.profile);
        profileEditImageView = view.findViewById(R.id.profileEditImageView);
        userName = view.findViewById(R.id.name);
        userPhone = view.findViewById(R.id.phone);
        progressBar = view.findViewById(R.id.materialLoader);

        onClick();
        userProfileView();

        /*if (userVO != null) {
            GetUserProfileObject userProfileObject = new GetUserProfileObject();
            userProfileObject.setPhone(userVO.getPhone());
            CallUserProfile(userProfileObject);
        }*/

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void EventBusProfile(EventBusProfile eventBusProfile){

        userProfileView();
    }

    @Subscribe
    public void getEventBusCart(String home) {

        userVO = Utility.query_UserProfile(getActivity());

        if (profileUrl != null) {
            userProfileView();
        }


    }

    private void onClick() {
        changeLanguage.setOnClickListener(view -> {
            DialogChangeLanguage dialogChangeLanguage=new DialogChangeLanguage();
            dialogChangeLanguage.show(getFragmentManager(),"");
        });

        profileLayout.setOnClickListener(v -> {
            if (userVO != null) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            } else {
                startActivity(new Intent(getContext(), LogInActivity.class));
            }
        });

        logOut.setOnClickListener(v -> {
            DialogLogout dialogCall=new DialogLogout();
            dialogCall.show(getFragmentManager(),"");
        });
    }

    private void CallUserProfile(GetUserProfileObject getUserProfileObject) {
        if (Utility.isOnline(getContext())) {
            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.UserProfileCall(ApiConstants.BASE_URL + ApiConstants.GET_USER_PROFILE, getUserProfileObject).enqueue(new Callback<GetUserProfileModel>() {
                @Override
                public void onResponse(Call<GetUserProfileModel> call, Response<GetUserProfileModel> response) {

                    if (response.body().getError() == true) {
                        Utility.showToast(getContext(), response.body().getMessage());
                    } else if (response.body().getError() == false) {
                        progressBar.setVisibility(View.GONE);

                        userPhone.setText(response.body().getData().getPhone());
                        userName.setText(response.body().getData().getUsername());

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.loader_circle_shape);
                        requestOptions.error(R.drawable.loader_circle_shape);

                        profileUrl = response.body().getData().getImage();

                        if (profileUrl != null && !profileUrl.isEmpty() && !profileUrl.equals("null")) {

                            Glide.with(getContext())
                                    .load(profileUrl)
                                    .apply(requestOptions)
                                    .listener(new RequestListener<Drawable>() {

                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    }).into(profile);
                        }else{

                            Glide.with(getContext())
                                    .load(R.drawable.profile_default_image)
                                    .apply(requestOptions)
                                    .into(profile);

                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUserProfileModel> call, Throwable t) {
                    Utility.showToast(getContext(), t.getMessage());

                }
            });
        } else {
            Utility.showToast(getContext(), getString(R.string.no_internet));
        }
    }

    private void userProfileView() {

        if (userVO != null){

            loginView.setVisibility(View.VISIBLE);
            lineLogout.setVisibility(View.VISIBLE);
            profileEditImageView.setVisibility(View.VISIBLE);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.loader_circle_shape);
            requestOptions.error(R.drawable.loader_circle_shape);

            profileUrl = userVO.getImage();

            if (profileUrl != null && !profileUrl.isEmpty() && !profileUrl.equals("null")) {

                Glide.with(this)
                        .load(userVO.getImage())
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(profile);
            }else{

                Glide.with(getContext())
                        .load(R.drawable.profile_default_image)
                        .apply(requestOptions)
                        .into(profile);

            }

            userName.setText(userVO.getUsername());
            userPhone.setText(userVO.getPhone());

        } else {
            loginView.setVisibility(View.GONE);
            lineLogout.setVisibility(View.GONE);
            profileEditImageView.setVisibility(View.GONE);
        }
    }

}