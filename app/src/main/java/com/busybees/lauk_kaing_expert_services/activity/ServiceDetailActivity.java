package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.data.models.GetProductPriceModel;
import com.busybees.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.data.vos.ServiceDetail.ProductPriceVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Products.ServiceDetailAdapter;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailActivity extends AppCompatActivity {

    private NetworkServiceProvider serviceProvider;

    private TextView serviceDetailName;
    private ImageView back;

    private VideoView videoView;

    private LinearLayout continueLayout;

    private ProgressBar progressBar;

    private RecyclerView serviceDetailRecyclerView;
    private ServiceDetailAdapter serviceDetailAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ProductPriceVO> productPriceVOArrayList = new ArrayList<>();

    // Intent Data
    private String productName;
    private ProductsCarryObject productsCarryObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_service_detail);

        serviceProvider = new NetworkServiceProvider(this);

        videoView = findViewById(R.id.video_view);
        back = findViewById(R.id.back_button);
        serviceDetailRecyclerView = findViewById(R.id.service_detail_recyclerview);
        continueLayout = findViewById(R.id.continue_layout);
        serviceDetailName = findViewById(R.id.service_detail_name);
        progressBar = findViewById(R.id.materialLoader);

        setUpRecyclerView();
        onClick();

        if (getIntent() != null) {
            productName = getIntent().getStringExtra("product_title");
            productsCarryObject = (ProductsCarryObject) getIntent().getSerializableExtra("product_detail_data");
            serviceDetailName.setText(productName);
            CallProductPriceApi(productsCarryObject);
        }

    }

    private void CallProductPriceApi(ProductsCarryObject productsCarryObject) {
        if (Utility.isOnline(getApplicationContext())) {
            progressBar.setVisibility(View.GONE);

            serviceProvider.GetProductPriceCall(ApiConstants.BASE_URL + ApiConstants.GET_PRODUCT_PRICE_LISTS, productsCarryObject).enqueue(new Callback<GetProductPriceModel>() {
                @Override
                public void onResponse(Call<GetProductPriceModel> call, Response<GetProductPriceModel> response) {

                    progressBar.setVisibility(View.VISIBLE);
                    
                    if (response.body().getError() == true) {

                        videoView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        Utility.showToast(ServiceDetailActivity.this, response.body().getMessage());
                        finish();

                    } else if (response.body().getError() == false) {

                        progressBar.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        PlayVideo("https://busybeesexpertservice.com//assets/video/CCTV_Services.mp4");

                        productPriceVOArrayList.clear();
                        productPriceVOArrayList.addAll(response.body().getData());

                        serviceDetailAdapter = new ServiceDetailAdapter(ServiceDetailActivity.this, productPriceVOArrayList);
                        serviceDetailRecyclerView.setAdapter(serviceDetailAdapter);
                        serviceDetailAdapter.notifyDataSetChanged();

                        serviceDetailAdapter.setClick(v -> AdapterCLick(v));
                    }
                }

                @Override
                public void onFailure(Call<GetProductPriceModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        } else {
            progressBar.setVisibility(View.VISIBLE);
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    private void AdapterCLick(View v) {

        if (v.getId() == R.id.hideShowDetail) {
            int position = (int) v.getTag(R.id.position);
            ProductPriceVO productPriceVO = productPriceVOArrayList.get(position);
            productPriceVO.setShowDetail(!productPriceVO.isShowDetail());
            serviceDetailAdapter.notifyItemChanged(position);

        }

    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        continueLayout.setOnClickListener(v -> startActivity(new Intent(ServiceDetailActivity.this, AddressActivity.class)));
    }

    private void setUpRecyclerView() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        serviceDetailRecyclerView.setLayoutManager(layoutManager);
    }

    private void PlayVideo(String video_url) {
        /*videoView = findViewById(R.id.video_view);
        Uri video = Uri.parse(video_url);
        //player = ExoPlayerFactory.newSimpleInstance(this);
        videoView.setPlayer(player);
        videoView.setUseController(false);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"));
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(video));
        LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
        player.prepare(loopingSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(player.REPEAT_MODE_ALL);*/

        /*MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);*/
        Uri uri = Uri.parse(video_url);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnCompletionListener(mp -> {
            videoView.start();
        });

        videoView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 24);
            }
        });

        videoView.setClipToOutline(true);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
