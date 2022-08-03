package com.busybees.lauk_kaing_expert_services.Dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.busybees.lauk_kaing_expert_services.EventBusModel.EventBusCartObj;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartDataModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;

@SuppressLint("ValidFragment")
public class DialogServiceDelete extends DialogFragment {

    Button no,yes;
    GetCartDataModel dataModel;

    UserVO userVO;

    public DialogServiceDelete(GetCartDataModel dataModel) {
        this.dataModel=dataModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_service_delete, container, false);
        setCancelable(false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        no=view.findViewById(R.id.delete_cancel_btn);
        yes=view.findViewById(R.id.delete_yes_btn);

        no.setOnClickListener(v -> dismiss());

        yes.setOnClickListener(v -> {
            userVO = Utility.query_UserProfile(getContext());

            if (dataModel != null){
                EventBusCartObj eventBusCartObj=new EventBusCartObj();
                eventBusCartObj.setPhone(userVO.getPhone());
                eventBusCartObj.setId(dataModel.getProductPriceId());
                eventBusCartObj.setQuantity(dataModel.getQuantity());
                eventBusCartObj.setFormStatus(dataModel.getFormStatus());

                EventBus.getDefault().post(eventBusCartObj);
            }
            dismiss();
        });

        return view;
    }

}
