package com.busybees.lauk_kaing_expert_services.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busybees.lauk_kaing_expert_services.data.models.MyOrders.MyOrderDataModel;
import com.busybees.lauk_kaing_expert_services.data.models.MyOrders.MyOrderModel;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.MyOrdersDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.RequestPhoneObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.ExpandableOrderAdapter;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    private RelativeLayout logInView, noLogInView;
    private ExpandableListView orderExpandableListView;
    private ExpandableOrderAdapter expandableListViewAdapter;
    private LinearLayout reloadPage;
    private Button goToLogInBtn;

    private TextView noDataTextView;
    private ProgressBar progressBar;

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    ArrayList<MyOrderDataModel> groupList = new ArrayList<>();
    ArrayList<MyOrdersDetailVO> childList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        networkServiceProvider = new NetworkServiceProvider(getContext());
        userVO = Utility.query_UserProfile(getContext());

        logInView = view.findViewById(R.id.loginView);
        noLogInView = view.findViewById(R.id.no_login_view);
        orderExpandableListView = view.findViewById(R.id.order_expandable_list_view);
        noDataTextView = view.findViewById(R.id.no_data);
        progressBar = view.findViewById(R.id.materialLoader);
        reloadPage = view.findViewById(R.id.reload_page);
        goToLogInBtn = view.findViewById(R.id.go_to_login);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        orderExpandableListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(10));

        orderExpandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            }
        });

        if (Utility.isOnline(getContext())) {
            reloadPage.setVisibility(View.GONE);
            logInView.setVisibility(View.VISIBLE);

            if (userVO != null) {
                noLogInView.setVisibility(View.GONE);
                logInView.setVisibility(View.VISIBLE);
            } else {
                noLogInView.setVisibility(View.VISIBLE);
                logInView.setVisibility(View.GONE);
            }

        } else {
            reloadPage.setVisibility(View.VISIBLE);
            logInView.setVisibility(View.GONE);
        }

        initListeners();
        CallGetMyOrders();
        onClick();
        return view;
    }

    private void CallGetMyOrders() {

        progressBar.setVisibility(View.VISIBLE);

        if (userVO != null){
            RequestPhoneObject phoneObj=new RequestPhoneObject();
            phoneObj.setPhone(userVO.getPhone());

            if (Utility.isOnline(getActivity())) {
                networkServiceProvider.GetMyOrdersCall(ApiConstants.BASE_URL + ApiConstants.GET_MY_ORDERS, phoneObj).enqueue(new Callback<MyOrderModel>() {
                    @Override
                    public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {

                        if (response.body().getError() == false) {
                            progressBar.setVisibility(View.GONE);

                            groupList.clear();
                            groupList.addAll(response.body().getData());

                            for (int i = 0; i < groupList.size(); i++) {
                                childList.clear();
                                childList.addAll(groupList.get(i).getOrdersDetail());
                                showOrders();
                            }

                            if (groupList.size() == 0){
                                noDataTextView.setVisibility(View.VISIBLE);

                            }else {
                                noDataTextView.setVisibility(View.GONE);

                            }

                        } else {
                            noDataTextView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Utility.showToast(getContext(), response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyOrderModel> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getContext(), t.getMessage());
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Utility.showToast(getContext(), getString(R.string.no_internet));
            }
        }
    }

    private void onClick() {
        goToLogInBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LogInActivity.class));
        });
    }

    private void showOrders() {
        expandableListViewAdapter = new ExpandableOrderAdapter(getActivity(), groupList, childList);

        orderExpandableListView.setAdapter(expandableListViewAdapter);

        if (expandableListViewAdapter.getGroupCount() > 1) {
            orderExpandableListView.expandGroup(0);
            orderExpandableListView.expandGroup(1);
        } else {
            orderExpandableListView.expandGroup(0);
        }

        expandableListViewAdapter.notifyDataSetChanged();
    }

    private void initListeners() {

        orderExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(parent.isGroupExpanded(groupPosition))
                {

                }
                else{

                    // Expanded ,Do your Staff

                }
                return false;
            }
        });

        orderExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Utility.showToast(getActivity(), groupPosition
                        + " : "
                        +
                        childPosition);
                return false;
            }
        });
        // ExpandableListView Group expanded listener
        orderExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        // ExpandableListView Group collapsed listener
        orderExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }

    private int GetPixelFromDips(float pixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
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
    public void getEventBusCart(String home) {

        userVO = Utility.query_UserProfile(getActivity());

    }
}