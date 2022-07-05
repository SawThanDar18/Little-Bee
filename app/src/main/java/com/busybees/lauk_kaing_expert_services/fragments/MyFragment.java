package com.busybees.lauk_kaing_expert_services.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.busybees.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.Dialog.DialogChangeLanguage;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.activity.ProfileActivity;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class MyFragment extends Fragment {

    private LinearLayout changeLanguage, loginView, lineLogout;
    private CardView profileLayout;
    private ImageView profile, profileEditImageView;
    private TextView userName, userPhone;

    private UserVO userVO = new UserVO();
    private String profileUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userVO = Utility.query_UserProfile(getContext());

        View view = inflater.inflate(R.layout.fragment_my, container, false);

        changeLanguage = view.findViewById(R.id.change_language);
        profileLayout = view.findViewById(R.id.profile_layout);
        loginView = view.findViewById(R.id.loginView);
        lineLogout = view.findViewById(R.id.line_logout);
        profile = view.findViewById(R.id.profile);
        profileEditImageView = view.findViewById(R.id.profileEditImageView);
        userName = view.findViewById(R.id.name);
        userPhone = view.findViewById(R.id.phone);

        onClick();
        userProfileView();

        return  view;
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
    }

    private void userProfileView() {

        if (userVO != null){

            loginView.setVisibility(View.VISIBLE);
            lineLogout.setVisibility(View.VISIBLE);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.drawable.profile_default_image);
            requestOptions.optionalCircleCrop();
            requestOptions .diskCacheStrategy(DiskCacheStrategy.ALL);

            profileUrl = userVO.getImage();

            if (profileUrl != null && !profileUrl.isEmpty() && !profileUrl.equals("null")) {

                profileEditImageView.setVisibility(View.GONE);

                Glide.with(this)
                        .load(userVO.getImage())
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                profileEditImageView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                profileEditImageView.setVisibility(View.VISIBLE);
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
        }
    }
}