package com.busybees.lauk_kaing_expert_services.Dialog;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.DialogFragment;

import com.busybees.lauk_kaing_expert_services.EventBus.EventBusChangeLanguage;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

public class DialogChangeLanguage extends DialogFragment {

    RadioButton myanmarLanguage, englishLanguage, chineseLanguage;
    RadioGroup radioGroup;
    Button confirmBtn;
    int languageIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_language, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        myanmarLanguage = view.findViewById(R.id.myanmar_language);
        englishLanguage = view.findViewById(R.id.english_language);
        chineseLanguage = view.findViewById(R.id.chinese_language);
        radioGroup = view.findViewById(R.id.radio_group);

        confirmBtn = view.findViewById(R.id.confirm_btn);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (myanmarLanguage.isChecked()){

                myanmarLanguage.setChecked(true);
                AppStorePreferences.putInt(getContext(), AppENUM.LANG_Txt,1);

            }else if (englishLanguage.isChecked()){

                englishLanguage.setChecked(true);
                AppStorePreferences.putInt(getContext(), AppENUM.LANG_Txt, 0);

            } else if (chineseLanguage.isChecked()) {

                chineseLanguage.setChecked(true);
                AppStorePreferences.putInt(getContext(), AppENUM.LANG_Txt, 2);
            }
        });

        onClick();

        return view;
    }

    private void onClick() {
        confirmBtn.setOnClickListener((View.OnClickListener) v -> {

            if (AppStorePreferences.getInt(getContext(), AppENUM.LANG_Txt)==0){

                AppStorePreferences.putString(getActivity(), AppENUM.LANG, Locale.ENGLISH.getLanguage());
                updateResources(Locale.ENGLISH.getLanguage());
                AppStorePreferences.putInt(getContext(), AppENUM.LNG_CON,1);
                dismiss();

            }else  if (AppStorePreferences.getInt(getContext(), AppENUM.LANG_Txt)==1){
                /*if (MDetect.INSTANCE.isUnicode()){
                    AppStorePreferences.putString(getActivity(), AppENUM.LANG, Locale.ITALY.getLanguage());
                    updateResources(Locale.ITALY.getLanguage());
                } else  {
                    AppStorePreferences.putString(getActivity(), AppENUM.LANG, Locale.FRANCE.getLanguage());
                    updateResources(Locale.FRANCE.getLanguage());
                }*/
                AppStorePreferences.putString(getActivity(), AppENUM.LANG, Locale.ITALY.getLanguage());
                updateResources(Locale.ITALY.getLanguage());
                AppStorePreferences.putInt(getContext(), AppENUM.LNG_CON,1);

                dismiss();

            } else {

                AppStorePreferences.putString(getActivity(), AppENUM.LANG, Locale.SIMPLIFIED_CHINESE.getLanguage());
                updateResources(Locale.SIMPLIFIED_CHINESE.getLanguage());
                AppStorePreferences.putInt(getContext(), AppENUM.LNG_CON,1);
                dismiss();

            }

        });
    }

    public  void updateResources(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getActivity().getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        EventBusChangeLanguage eventBusChangeLanguage = new EventBusChangeLanguage();
        eventBusChangeLanguage.setLanguage(1);
        EventBus.getDefault().post(eventBusChangeLanguage);

        startActivity(new Intent(getActivity(), MainActivity.class));

    }

}


