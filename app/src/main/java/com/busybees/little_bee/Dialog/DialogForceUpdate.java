package com.busybees.little_bee.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.busybees.little_bee.R;


public class DialogForceUpdate extends DialogFragment {

    private Button updateBtn;
    private TextView updateAlertText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_force_update, container, false);

        setCancelable(false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        updateAlertText =view.findViewById(R.id.txtview);
        updateBtn =view.findViewById(R.id.btnupdate);

        updateAlertText.setText(R.string.update_msg);

        updateBtn.setOnClickListener(v -> {
            Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("https://play.google.com/store/apps/details?id=com.busybees.little_bee") );
            if(getView()!=null){
                startActivity( browse );
            }
        });


        return view;
    }
}
