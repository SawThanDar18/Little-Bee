package com.busybees.lauk_kaing_expert_services.activity;

import android.graphics.Color;
import android.graphics.Outline;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Products.ServiceDetailAdapter;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ServiceDetailActivity extends AppCompatActivity {

    private TextView serviceDetailName;

    private VideoView videoView;

    private LinearLayout continueLayout;

    private RecyclerView serviceDetailRecyclerView;
    private ServiceDetailAdapter serviceDetailAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_service_detail);

        videoView = findViewById(R.id.video_view);
        serviceDetailRecyclerView = findViewById(R.id.service_detail_recyclerview);

        setUpRecyclerView();
        onClick();

        PlayVideo("https://busybeesexpertservice.com//assets/video/CCTV_Services.mp4");

    }

    private void onClick() {

    }

    private void setUpRecyclerView() {
        serviceDetailAdapter = new ServiceDetailAdapter(ServiceDetailActivity.this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        serviceDetailRecyclerView.setLayoutManager(layoutManager);
        serviceDetailRecyclerView.setAdapter(serviceDetailAdapter);
        serviceDetailAdapter.notifyDataSetChanged();
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
