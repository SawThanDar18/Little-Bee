package com.busybees.little_bee.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busybees.little_bee.Dialog.DialogCall;
import com.busybees.little_bee.R;

public class ContactUsActivity extends AppCompatActivity {

    private LinearLayout phone, mobile, mapIntent;
    private ImageView backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_contact_us);

        phone = findViewById(R.id.phone_one);
        mobile = findViewById(R.id.mobile_one);
        backButton = findViewById(R.id.back_button);
        mapIntent = findViewById(R.id.map_intent);

        onClick();
    }

    private void onClick() {
        phone.setOnClickListener(v-> {
            String phone = "09689082742";
            DialogCall dialogCall1=new DialogCall(phone);
            dialogCall1.show(getSupportFragmentManager(),"");
        });

        mobile.setOnClickListener(v-> {
            String phone = "09684096773";
            DialogCall dialogCall1=new DialogCall(phone);
            dialogCall1.show(getSupportFragmentManager(),"");
        });

        mapIntent.setOnClickListener(v-> {
            /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=16.829756,96.129758"));
            startActivity(intent);*/
        });

        backButton.setOnClickListener(v-> finish());
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
