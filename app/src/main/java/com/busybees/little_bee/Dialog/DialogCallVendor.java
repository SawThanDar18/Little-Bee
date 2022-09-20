package com.busybees.little_bee.Dialog;

import android.annotation.SuppressLint;
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
import com.busybees.little_bee.data.vos.MyOrders.VendorInfoVO;

public class DialogCallVendor extends DialogFragment {

    TextView textView;
    Button no,yes;
    VendorInfoVO obj;

    @SuppressLint("ValidFragment")
    public DialogCallVendor(VendorInfoVO obj) {
        this.obj=obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_call, container, false);
        setCancelable(false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        textView=view.findViewById(R.id.phonetxt);
        no=view.findViewById(R.id.vendor_no_btn);
        yes=view.findViewById(R.id.vendor_yes_btn);

        textView.setText(obj.getVendorName());
        yes.setText(R.string.call_now);
        no.setText(R.string.cancel);

        no.setOnClickListener(v -> dismiss());

        yes.setOnClickListener(v -> {
            dialContactPhone(obj.getVendorPhone());
            dismiss();
        });

        return view;
    }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}

