package com.busybees.lauk_kaing_expert_services.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.MyHistoryDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.QuestionsVO;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import me.myatminsoe.mdetect.MDetect;

public class DialogRating extends BottomSheetDialogFragment {

    TextView rate;
    ArrayList<QuestionsVO> questionsList = new ArrayList<>();
    RatingBar ratingBar;
    ImageView cancel;
    MyHistoryDetailVO orderDetailsObject;

    public DialogRating(ArrayList<QuestionsVO> questionsList, MyHistoryDetailVO receiptModel) {
        this.questionsList =questionsList;
        this.orderDetailsObject=receiptModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rate, container, false);

        rate=view.findViewById(R.id.app_rating);
        cancel=view.findViewById(R.id.cancel);
        ratingBar=view.findViewById(R.id.rate_app_rating);

        if (Utility.checkLng(getActivity()).equalsIgnoreCase("it") || Utility.checkLng(getActivity()).equalsIgnoreCase("fr")){
            if ( MDetect.INSTANCE.isUnicode()){
                rate.setText(questionsList.get(1).getServiceRating());

            } else  {
                rate.setText(questionsList.get(1).getServiceRating());
            }
        } else if (Utility.checkLng(getActivity()).equalsIgnoreCase("zh")) {
            rate.setText(questionsList.get(2).getServiceRating());
        }
        else{
            rate.setText(questionsList.get(0).getServiceRating());
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                dismiss();
                /*Intent intent=new Intent(getActivity(), RatingActivity.class);
                intent.putExtra("history_data", orderDetailsObject);
                intent.putExtra("rate_question", questionsList);
                intent.putExtra("rate_count", ratingBar.getRating());
                startActivity(intent);
                getActivity().finish();*/
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
