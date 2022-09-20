package com.busybees.little_bee.Dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.busybees.little_bee.MainActivity;
import com.busybees.little_bee.R;
import com.busybees.little_bee.utility.Utility;

@SuppressLint("ValidFragment")
public class DialogLogout extends DialogFragment {

    Button noBtn, yesBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_logout, container, false);
        setCancelable(true);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        noBtn = view.findViewById(R.id.logout_no_btn);
        yesBtn = view.findViewById(R.id.logout_yes_btn);

        onClick();

        return view;
    }

    private void onClick() {

        noBtn.setOnClickListener(v -> dismiss());

        yesBtn.setOnClickListener(v -> {
            getActivity().finish();
            doLogOut();
        });
    }

    private void doLogOut() {

        Utility.delete_UserProfile(getActivity());
        startActivity(new Intent(getActivity(), MainActivity.class));

    }
}


