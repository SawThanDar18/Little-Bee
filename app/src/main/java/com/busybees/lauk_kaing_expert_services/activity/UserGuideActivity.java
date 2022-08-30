package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.UserGuide.OnBoardingScreenAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.UserGuide.UserGuideModel;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserGuideActivity extends AppCompatActivity  implements Button.OnClickListener  {

    ImageView back;
    private TextView headerText;
    private ViewPager viewPager;
    private LinearLayout dotsLinearLayout;
    private OnBoardingScreenAdapter adapter;
    private TextView[] dots;
    private int noOfScreens;
    private Button backButton;
    private Button nextButton;
    private int currentScreenIndex;
    private RelativeLayout layout;
    private int langtxt;
    private ImageView video;
    private String youTubeId = "";
    private ProgressBar progressBar;

    NetworkServiceProvider serviceProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_user_guide);

        serviceProvider = new NetworkServiceProvider(this);

        back = findViewById(R.id.back_button);
        headerText = (TextView) findViewById(R.id.toolbar_common);
        layout = (RelativeLayout)findViewById(R.id.bottom_layout);
        video = (ImageView)findViewById(R.id.video_click);
        progressBar = (ProgressBar)findViewById(R.id.materialLoader);

        headerText.setText(getString(R.string.user_guide));

        langtxt = AppStorePreferences.getInt(this, AppENUM.LANG_Txt);

        viewPager = findViewById(R.id.onboarding_viewpager_id);
        dotsLinearLayout = findViewById(R.id.onboarding_dots_container_id);
        nextButton = findViewById(R.id.next_button_id);
        backButton = findViewById(R.id.back_button_id);

        nextButton.setText(getString(R.string.next));
        backButton.setEnabled(false);

        CallUserGuide();

        back.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        video.setOnClickListener(this);

    }


    private void addDotsToArray(int noOfSlides,int currentPositon){
        dots = new TextView[noOfSlides];

        // Adds dots to the textview array in translucent white (unselected color)
        for (int i = 0; i < noOfSlides; i++){
            dots[i] = new TextView(this);
            //Min api is 24
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].setText(Html.fromHtml("&#8226",noOfSlides));
            }
            dots[i].setTextSize(35);
            dots[i].setTextColor(ContextCompat.getColor(this,R.color.dark_grey));
        }

        // Set the dot for the current screen to white (selected color)
        dots[currentPositon].setTextColor(ContextCompat.getColor(this,R.color.theme_color));

        displayDots(dots);
    }

    private void displayDots(TextView[] dots){
        // Add dots to the layout
        for(int i = 0; i < dots.length; i++){
            dotsLinearLayout.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            currentScreenIndex = i;
            // Remove all the dots
            dotsLinearLayout.removeAllViews();
            //Pass the current position of the screen to addDotsToArray method
            addDotsToArray(noOfScreens,i);

            // if onBoardingScreen is at the first screen
            if (i == 0){
                nextButton.setText(getString(R.string.next));
                backButton.setText("");
                backButton.setEnabled(false);
                nextButton.setEnabled(true);
            }
            //if onBoardingScreen is at the last screen
            else if (i == noOfScreens-1){
                nextButton.setText(getString(R.string.finish));
                backButton.setText(getString(R.string.back));
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
            }
            // if onBoardingScreen is at the other screens
            else {
                nextButton.setText(getString(R.string.next));
                nextButton.setEnabled(true);
                backButton.setText(getString(R.string.back));
                backButton.setEnabled(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_button_id:

                String buttonText = nextButton.getText().toString();
                if (buttonText.equals(this.getResources().getString(R.string.next))){
                    viewPager.setCurrentItem(currentScreenIndex + 1,true);
                }
                else if (buttonText.equals(this.getResources().getString(R.string.finish))){
                    finish();
                }
                break;

            case R.id.back_button_id:

                viewPager.setCurrentItem(currentScreenIndex-1,true);
                break;

            case R.id.back_button:

                finish();
                break;

           /* case R.id.video_click:

                if (youTubeId !=null){
                    Intent intent=new Intent(UserGuideActivity.this,YouTubePlayActivity.class);
                    intent.putExtra("you_tube_id", youTubeId);
                    startActivity(intent);
                }else {
                    Utility.showToast(this,"Please wait");
                    if (String.valueOf(AppStorePreferences.getString(UserGuideActivity.this, AppENUM.DIVISION_MOBILE)).equalsIgnoreCase("ygn")){
                        CallUserGuide(APIURL.DomainName);
                    }else if (String.valueOf(AppStorePreferences.getString(UserGuideActivity.this, AppENUM.DIVISION_MOBILE)).equalsIgnoreCase("mdy")){
                        CallUserGuide(APIURL.MdyDomain);
                    }
                }


                break;*/
        }
    }

    public void CallUserGuide() {

        if (Utility.isOnline(this)){
            progressBar.setVisibility(View.VISIBLE);
            serviceProvider.GetUserGuideCall(ApiConstants.BASE_URL + ApiConstants.GET_USER_GUIDE).enqueue(new Callback<UserGuideModel>() {

                @Override
                public void onResponse(Call<UserGuideModel> call, Response<UserGuideModel> response) {

                    if (response.body().getError()==false){
                        progressBar.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        if (langtxt == 1) {

                            ImageSlide(response.body().getData().getMm());

                        } else if (langtxt == 2){
                            ImageSlide(response.body().getData().getEn());

                        } else {

                            ImageSlide(response.body().getData().getEn());
                        }
                        //youTubeId =response.body().getData().getVideoId();

                    }else if (response.body().getError()==true){
                        progressBar.setVisibility(View.GONE);
                        layout.setVisibility(View.GONE);
                        Utility.showToast(UserGuideActivity.this,response.body().getMessage());
                    }


                }
                @Override
                public void onFailure(Call<UserGuideModel> call, Throwable t) {

                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(getApplicationContext(), t.getMessage());

                }
            });

        }else {
            Utility.showToast(this,getString(R.string.no_internet));
        }
    }


    private void ImageSlide(List<String> list){
        currentScreenIndex = 0;
        adapter = new OnBoardingScreenAdapter(this,list);
        viewPager.setAdapter(adapter);
        noOfScreens = adapter.getCount();
        addDotsToArray(noOfScreens,currentScreenIndex);
        viewPager.addOnPageChangeListener(listener);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

}

