package com.busybees.lauk_kaing_expert_services.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;

import com.busybees.lauk_kaing_expert_services.data.models.PickerModel;
import com.busybees.lauk_kaing_expert_services.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;

public class DialogImagePicker extends BottomSheetDialogFragment {

    LinearLayout camera,gallery;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.dialog_edit_profile_image, null);
        dialog.setContentView(contentView);

        camera = contentView.findViewById(R.id.camera);
        gallery = contentView.findViewById(R.id.gallery);

        onClick();

    }

    private void onClick() {
        camera.setOnClickListener(v -> {

            PickerModel pickerModel=new PickerModel();
            pickerModel.setPicker(0);
            EventBus.getDefault().post(pickerModel);
            dismiss();

        });

        gallery.setOnClickListener(v -> {

            PickerModel pickerModel=new PickerModel();
            pickerModel.setPicker(1);
            EventBus.getDefault().post(pickerModel);
            dismiss();

        });
    }

}



