package com.busybees.lauk_kaing_expert_services.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class SearchActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    private ImageView back, searchImage;

    private EditText searchText;
    private ProgressBar progressBar;
    private RecyclerView searchRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_search);

        networkServiceProvider = new NetworkServiceProvider(this);
        userVO = Utility.query_UserProfile(this);

        back = findViewById(R.id.back_button);
        searchImage = findViewById(R.id.search_img);
        searchText = findViewById(R.id.txt_search);
        progressBar = findViewById(R.id.materialLoader);
        searchRecyclerView = findViewById(R.id.recycle_search);

        onClick();
    }

    private void onClick() {
        back.setOnClickListener(v -> {
          finish();
        });

        searchImage.setOnClickListener(v -> {
            String searchName = searchText.getText().toString();
            if (!searchName.isEmpty()){
                //SearchApi(searchName);
            }
        });

        searchText.setText("");
        searchText.requestFocus();

        searchText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:

                        progressBar.setVisibility(View.VISIBLE);

                        String searchName = searchText.getText().toString();
                        if (!searchName.isEmpty()){
                           // SearchApi(searchName);
                        }

                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
