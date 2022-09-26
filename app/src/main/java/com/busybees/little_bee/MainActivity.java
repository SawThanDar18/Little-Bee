package com.busybees.little_bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.busybees.little_bee.Dialog.DialogForceUpdate;
import com.busybees.little_bee.EventBus.EventBusChangeLanguage;
import com.busybees.little_bee.EventBusModel.EventBusCall;
import com.busybees.little_bee.EventBusModel.EventBusCartObj;
import com.busybees.little_bee.EventBusModel.GoToCart;
import com.busybees.little_bee.activity.NotificationActivity;
import com.busybees.little_bee.adapters.Home.ViewPagerAdapter;
import com.busybees.little_bee.data.models.ForceUpdate.ForceUpdateObj;
import com.busybees.little_bee.data.models.ForceUpdate.ForeUpdateModel;
import com.busybees.little_bee.fragments.CartsFragment;
import com.busybees.little_bee.fragments.HomeFragment;
import com.busybees.little_bee.fragments.MoreFragment;
import com.busybees.little_bee.fragments.OrdersFragment;
import com.busybees.little_bee.fragments.ReceiptFragments;
import com.busybees.little_bee.network.NetworkServiceProvider;
import com.busybees.little_bee.utility.ApiConstants;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.CustomViewPager;
import com.busybees.little_bee.utility.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NetworkServiceProvider networkServiceProvider;

    private BottomNavigationView bottomNavigationView;
    private CustomViewPager viewPager;

    private MenuItem prevMenuItem;

    private HomeFragment homeFragment;
    private CartsFragment cartsFragment;
    private OrdersFragment ordersFragment;
    private ReceiptFragments receiptFragments;
    private MoreFragment moreFragment;

    private ViewPagerAdapter viewPagerAdapter;

    private boolean doubleBackToExitPressedOnce = false;
    private static int tabCondition = 0;

    private PackageInfo packageInfo;
    private int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_main);

        MDetect.INSTANCE.init(this);
        LanguageChange();

        networkServiceProvider = new NetworkServiceProvider(this);

        bottomNavigationView = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.viewpager_container);

        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);

        if (tabCondition != 0) {
            bottomNavigationView.setSelectedItemId(R.id.moreTab);
            tabCondition = 0;
        }

        if (getIntent() != null) {
            int tabPosition = getIntent().getIntExtra("tabPosition", 0);
            Log.e("tab>>>", String.valueOf(tabPosition));
            if (tabPosition == 1) {
                Carts();
            } else if (tabPosition == 4) {
                My();
            }
        }

        CalculateVersionCode();

        intiUI();
        onClick();
    }

    private void CalculateVersionCode() {
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionCode;

            ForceUpdateObj forceUpdateObj = new ForceUpdateObj();
            forceUpdateObj.setVersion(String.valueOf(version));
            CallForceUpdate(forceUpdateObj);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void CallForceUpdate(ForceUpdateObj forceUpdateObj) {
        if (Utility.isOnline(getApplicationContext())) {
            networkServiceProvider.ForceUpdate(ApiConstants.BASE_URL + ApiConstants.GET_FORCE_UPDATE, forceUpdateObj).enqueue(new Callback<ForeUpdateModel>() {
                @Override
                public void onResponse(Call<ForeUpdateModel> call, Response<ForeUpdateModel> response) {
                    if (response.body().getError() == false) {

                        DialogForceUpdate dialogForceUpdate = new DialogForceUpdate();
                        dialogForceUpdate.show(getSupportFragmentManager(), "");
                        Utility.delete_UserProfile(getApplicationContext());

                    } else {
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ForeUpdateModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());

                }
            });
        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    private void intiUI() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                if (position == 0) {
                    Home();
                } else if (position == 1) {
                    Carts();
                } else if (position == 2) {
                    Orders();
                } else if (position == 3) {
                    Receipt();
                } else if (position == 4) {
                    My();
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        viewPager.disableScroll(true);

        setupViewPager(viewPager);

    }

    private void onClick() {

    }

    private void Home() {
        EventBus.getDefault().post("home");
        viewPager.setCurrentItem(0);
    }

    public void Carts() {
        EventBus.getDefault().post("carts");
        viewPager.setCurrentItem(1);

    }

    private void Orders() {
        EventBus.getDefault().post("orders");
        viewPager.setCurrentItem(2);

    }

    private void Receipt() {
        EventBus.getDefault().post("receipt");
        viewPager.setCurrentItem(3);

    }

    private void My() {
        EventBus.getDefault().post("my");
        viewPager.setCurrentItem(4);

    }

    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        cartsFragment = new CartsFragment();
        ordersFragment = new OrdersFragment();
        receiptFragments = new ReceiptFragments();
        moreFragment = new MoreFragment();

        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(cartsFragment);
        viewPagerAdapter.addFragment(ordersFragment);
        viewPagerAdapter.addFragment(receiptFragments);
        viewPagerAdapter.addFragment(moreFragment);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            MainActivity.this.finishAffinity();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utility.showToast(this, getResources().getString(R.string.exit_app));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeTab:

                Home();
                break;

            case R.id.cartTab:

                Carts();
                break;

            case R.id.orderTab:

                Orders();
                break;

            case R.id.receiptTab:

                Receipt();
                break;

            case R.id.moreTab:

                My();
                break;
        }
        return false;
    }

    private void LanguageChange() {

        int lang_txt = AppStorePreferences.getInt(this, AppENUM.LANG_Txt);

        Locale myLocale;

        if (lang_txt == 1) {

            if (MDetect.INSTANCE.isUnicode()) {
                myLocale = new Locale("it");
            } else {
                myLocale = new Locale("fr");
            }
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } else if (lang_txt == 0) {
            myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } else if (lang_txt == 2) {
            myLocale = new Locale("zh", "CN");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getEventBusService(EventBusCall service) {


        bottomNavigationView.setSelectedItemId(R.id.homeTab);

    }

    @Subscribe
    public void getEventBusChlng(EventBusChangeLanguage chLng) {
        tabCondition = chLng.getLanguage();
    }

    @Subscribe
    public void getCart(EventBusCartObj obj) {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getGoToGetCart(GoToCart goToCart) {

        bottomNavigationView.setSelectedItemId(R.id.cartTab);
        EventBus.getDefault().removeStickyEvent(goToCart);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Log.e("extra>", extras.getString("notification_condition"));

            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(extras.get("notification_condition")));
                Log.e("all>>>", String.valueOf(String.valueOf(jsonObject.get("status")).equals("All")));

                if (String.valueOf(jsonObject.get("status")).equals("All")) {

                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
        }

    }
}