package com.busybees.lauk_kaing_expert_services.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.busybees.lauk_kaing_expert_services.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddMoreServicesDialog extends BottomSheetDialogFragment {

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.add_more_service, null);
        dialog.setContentView(contentView);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
    }
}
