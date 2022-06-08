package com.busybees.lauk_kaing_expert_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.busybees.lauk_kaing_expert_services.adapters.Home.ViewPagerAdapter;
import com.busybees.lauk_kaing_expert_services.fragments.CartsFragment;
import com.busybees.lauk_kaing_expert_services.fragments.HomeFragment;
import com.busybees.lauk_kaing_expert_services.fragments.MyFragment;
import com.busybees.lauk_kaing_expert_services.fragments.OrdersFragment;
import com.busybees.lauk_kaing_expert_services.fragments.ReceiptFragments;
import com.busybees.lauk_kaing_expert_services.utility.CustomViewPager;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private ConstraintLayout searchToolBar;
    private BottomNavigationView bottomNavigationView;
    private CustomViewPager viewPager;

    private MenuItem prevMenuItem;

    private HomeFragment homeFragment;
    private CartsFragment cartsFragment;
    private OrdersFragment ordersFragment;
    private ReceiptFragments receiptFragments;
    private MyFragment myFragment;

    private ViewPagerAdapter viewPagerAdapter;

    private boolean doubleBackToExitPressedOnce = false;
    private static int tabCondition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_main);

        searchToolBar = findViewById(R.id.search_tool_bar);
        bottomNavigationView = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.viewpager_container);

        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);

        if (tabCondition != 0) {
            bottomNavigationView.setSelectedItemId(R.id.moreTab);
            tabCondition = 0;
        }

        intiUI();
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
                    HomeFragment();
                } else if (position == 1) {
                    CartsFragment();
                } else if (position == 2) {
                    OrdersFragment();
                } else if (position == 3) {
                    ReceiptFragments();
                } else if (position == 4) {
                    MyFragment();
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

    private void HomeFragment() {
        viewPager.setCurrentItem(0);
        searchToolBar.setVisibility(View.VISIBLE);
    }

    public void CartsFragment() {
        viewPager.setCurrentItem(1);
        searchToolBar.setVisibility(View.GONE);

    }

    private void OrdersFragment() {
        viewPager.setCurrentItem(2);
        searchToolBar.setVisibility(View.GONE);

    }

    private void ReceiptFragments() {
        viewPager.setCurrentItem(3);
        searchToolBar.setVisibility(View.GONE);

    }

    private void MyFragment() {
        viewPager.setCurrentItem(4);
        searchToolBar.setVisibility(View.GONE);

    }

    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        cartsFragment = new CartsFragment();
        ordersFragment = new OrdersFragment();
        receiptFragments = new ReceiptFragments();
        myFragment = new MyFragment();

        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(cartsFragment);
        viewPagerAdapter.addFragment(ordersFragment);
        viewPagerAdapter.addFragment(receiptFragments);
        viewPagerAdapter.addFragment(myFragment);

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

                HomeFragment();
                break;

            case R.id.cartTab:

                CartsFragment();
                break;

            case R.id.orderTab:

                OrdersFragment();
                break;

            case R.id.receiptTab:

                ReceiptFragments();
                break;

            case R.id.moreTab:

                MyFragment();
                break;
        }
        return false;
    }
}