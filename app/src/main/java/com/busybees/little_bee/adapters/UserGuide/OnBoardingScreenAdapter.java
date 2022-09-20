package com.busybees.little_bee.adapters.UserGuide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.little_bee.R;
import com.busybees.little_bee.activity.UserGuideActivity;
import com.jsibbold.zoomage.ZoomageView;

import java.util.List;

public class OnBoardingScreenAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ZoomageView imageViewIcon;
    private List<String> list;
    private ProgressBar progressBar;

    public OnBoardingScreenAdapter(UserGuideActivity userGuideActivity, List<String> list) {
        this.context=userGuideActivity;
        this.list=list;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.onboarding_slide_layout,container,false);

        imageViewIcon = view.findViewById(R.id.onboarding_icon_id);
        progressBar=view.findViewById(R.id.materialLoader);

        String imglink=list.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        Glide.with(context)
                .load(imglink)
                .apply(requestOptions)
                .into(imageViewIcon);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
