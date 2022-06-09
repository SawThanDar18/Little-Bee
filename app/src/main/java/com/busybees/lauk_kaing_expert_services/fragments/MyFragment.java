package com.busybees.lauk_kaing_expert_services.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.busybees.lauk_kaing_expert_services.Dialog.DialogChangeLanguage;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.ChangeLanguageActivity;

public class MyFragment extends Fragment {

    private LinearLayout changeLanguage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        changeLanguage = view.findViewById(R.id.change_language);

        onClick();

        return  view;
    }

    private void onClick() {
        changeLanguage.setOnClickListener(view -> {
            DialogChangeLanguage dialogChangeLanguage=new DialogChangeLanguage();
            dialogChangeLanguage.show(getFragmentManager(),"");
        });
    }
}