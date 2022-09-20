package com.busybees.little_bee.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.little_bee.R;
import com.jsibbold.zoomage.ZoomageView;

public class ImageViewActivity extends AppCompatActivity {

    String imageUrl;
    ZoomageView imageZoom;

    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_imageview);

        back = findViewById(R.id.back_button);
        imageZoom = findViewById(R.id.myZoomageView);

        Intent condition=getIntent();
        imageUrl = (String) condition.getSerializableExtra("image");

        if (imageUrl !=null){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.loader_circle_shape);
            requestOptions.error(R.drawable.loader_circle_shape);
            Glide.with(this).load(imageUrl).apply(requestOptions).into(imageZoom);
        }

       back.setOnClickListener(v -> finish());

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
