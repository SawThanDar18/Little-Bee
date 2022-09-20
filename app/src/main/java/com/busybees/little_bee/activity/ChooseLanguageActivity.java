package com.busybees.little_bee.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.little_bee.EventBus.EventBusChangeLanguage;
import com.busybees.little_bee.MainActivity;
import com.busybees.little_bee.R;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.Utility;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;

public class ChooseLanguageActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioButton myanmarLanguage, englishLanguage, chineseLanguage;
    private RadioGroup languageRadioGroup;
    private Button confirmBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_choose_language);

        languageRadioGroup = findViewById(R.id.radGroup);
        myanmarLanguage = findViewById(R.id.mm_radio);
        englishLanguage = findViewById(R.id.en_radio);
        chineseLanguage = findViewById(R.id.china_radio);
        confirmBtn = findViewById(R.id.btn_confirm);

        languageRadioGroup.setOnCheckedChangeListener(this);

        MDetect.INSTANCE.init(this);
        LanguageChange();

        confirmBtn.setOnClickListener(v -> {

            if (englishLanguage.isChecked()!=false){
                AppStorePreferences.putInt(this, AppENUM.LANG_Txt, 0);
                AppStorePreferences.putString(this, AppENUM.LANG, Locale.ENGLISH.getLanguage());
                AppStorePreferences.putInt(getApplicationContext(), AppENUM.LNG_CON,1);
                updateResources(Locale.ENGLISH.getLanguage());
                finish();
                //startActivity(new Intent(ChooseLanguageActivity.this, MainActivity.class));

            } else if (myanmarLanguage.isChecked()!=false) {

                AppStorePreferences.putInt(this, AppENUM.LANG_Txt,1);
                if ( MDetect.INSTANCE.isUnicode()){
                    AppStorePreferences.putString(this, AppENUM.LANG, Locale.ITALY.getLanguage());
                    updateResources(Locale.ITALY.getLanguage());
                } else  {
                    AppStorePreferences.putString(this, AppENUM.LANG, Locale.FRANCE.getLanguage());
                    updateResources(Locale.FRANCE.getLanguage());
                }

                AppStorePreferences.putInt(getApplicationContext(), AppENUM.LNG_CON,1);
                finish();
                //startActivity(new Intent(ChooseLanguageActivity.this, MainActivity.class));

            } else if (chineseLanguage.isChecked()!=false) {

                AppStorePreferences.putInt(this, AppENUM.LANG_Txt, 2);
                AppStorePreferences.putString(this, AppENUM.LANG, Locale.SIMPLIFIED_CHINESE.getLanguage());
                AppStorePreferences.putInt(getApplicationContext(), AppENUM.LNG_CON,1);
                updateResources(Locale.SIMPLIFIED_CHINESE.getLanguage());
                finish();
                //startActivity(new Intent(ChooseLanguageActivity.this, MainActivity.class));

            } else {
                Utility.showToast(ChooseLanguageActivity.this,"Please select Language!");
            }


        });
    }

    private void LanguageChange(){
        int langIndex = AppStorePreferences.getInt(this, AppENUM.LANG_Txt);

        Locale myLocale;

        if (langIndex == 0) {

            myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            chineseLanguage.setChecked(false);
            myanmarLanguage.setChecked(false);
            englishLanguage.setChecked(true);

        } else if (langIndex == 1) {

            if ( MDetect.INSTANCE.isUnicode()){
                myLocale = new Locale("it");
            } else  {
                myLocale = new Locale("fr");
            }
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            chineseLanguage.setChecked(false);
            myanmarLanguage.setChecked(true);
            englishLanguage.setChecked(false);


        } else if (langIndex == 2) {

            myLocale = new Locale("zh");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            chineseLanguage.setChecked(true);
            myanmarLanguage.setChecked(false);
            englishLanguage.setChecked(false);
        }
    }

    public  void updateResources(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        EventBusChangeLanguage eventBusChangeLanguage = new EventBusChangeLanguage();
        eventBusChangeLanguage.setLanguage(1);
        EventBus.getDefault().post(eventBusChangeLanguage);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (myanmarLanguage.isChecked()){

            AppStorePreferences.putInt(this, AppENUM.LANG_Txt,1);
            if ( MDetect.INSTANCE.isUnicode()){
                AppStorePreferences.putString(this, AppENUM.LANG, Locale.ITALY.getLanguage());
            } else  {
                AppStorePreferences.putString(this, AppENUM.LANG, Locale.FRANCE.getLanguage());
            }

        }else if (englishLanguage.isChecked()){

            AppStorePreferences.putInt(this, AppENUM.LANG_Txt, 0);
            AppStorePreferences.putString(this, AppENUM.LANG, Locale.ENGLISH.getLanguage());

        } else if (chineseLanguage.isChecked()){
            AppStorePreferences.putInt(this, AppENUM.LANG_Txt, 2);
            AppStorePreferences.putString(this, AppENUM.LANG, Locale.SIMPLIFIED_CHINESE.getLanguage());
        }
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

    void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
