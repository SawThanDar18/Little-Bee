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

import com.busybees.lauk_kaing_expert_services.data.models.MyHistory.MyHistoryDataModel;
import com.busybees.lauk_kaing_expert_services.data.models.MyHistory.MyHistoryModel;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.MyHistoryDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.QuestionsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.RequestPhoneObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.ExpandableReceiptAdapter;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptFragments extends Fragment {

    private RelativeLayout logInView, noLogInView;
    private ExpandableListView receiptExpandableListView;
    private ExpandableReceiptAdapter expandableListViewAdapter;

    private TextView noDataTextView;
    private ProgressBar progressBar;
    private LinearLayout reloadPage;
    private Button goToLogInBtn;

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    private ArrayList<MyHistoryDataModel> groupList = new ArrayList<>();
    private ArrayList<MyHistoryDetailVO> childList = new ArrayList<>();
    private ArrayList<QuestionsVO> questionsVOArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        networkServiceProvider = new NetworkServiceProvider(getContext());
        userVO = Utility.query_UserProfile(getContext());

        logInView = view.findViewById(R.id.loginView);
        noLogInView = view.findViewById(R.id.no_login_view);
        receiptExpandableListView = view.findViewById(R.id.receipt_expandable_list_view);
        noDataTextView = view.findViewById(R.id.no_data);
        progressBar = view.findViewById(R.id.materialLoader);
        reloadPage = view.findViewById(R.id.reload_page);
        goToLogInBtn = view.findViewById(R.id.go_to_login);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        receiptExpandableListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(10));

        receiptExpandableListView.setOnTouchListener(new View.OnTouchListener() {
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
        CallMyOrderHistory();
        onClick();

        return view;
    }

    private void onClick() {
        goToLogInBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LogInActivity.class));
        });
    }

    private void CallMyOrderHistory() {
        progressBar.setVisibility(View.VISIBLE);

        if (userVO != null) {
            RequestPhoneObject requestPhoneObject = new RequestPhoneObject();
            requestPhoneObject.setPhone(userVO.getPhone());

            if (Utility.isOnline(getContext())) {
                networkServiceProvider.GetMyOrdersHistoryCall(ApiConstants.BASE_URL + ApiConstants.GET_MY_ORDERS_HISTORY, requestPhoneObject).enqueue(new Callback<MyHistoryModel>() {
                    @Override
                    public void onResponse(Call<MyHistoryModel> call, Response<MyHistoryModel> response) {
                        if (response.body().isError() == false) {
                            progressBar.setVisibility(View.GONE);

                            groupList.clear();
                            groupList.addAll(response.body().getData());

                            for (int i = 0; i < groupList.size(); i++) {
                                childList.clear();
                                childList.addAll(groupList.get(i).getMyHistoryDetail());

                                questionsVOArrayList.clear();
                                //questionsNameVOArrayList.addAll(response.body().getQuestions());

                                showOrders();
                            }

                            if (groupList.size() == 0){
                                noDataTextView.setVisibility(View.VISIBLE);

                            }else {
                                noDataTextView.setVisibility(View.GONE);

                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Utility.showToast(getContext(), response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyHistoryModel> call, Throwable t) {
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

    private void showOrders() {
        expandableListViewAdapter = new ExpandableReceiptAdapter(getActivity(), groupList, childList, null);

        receiptExpandableListView.setAdapter(expandableListViewAdapter);

        if (expandableListViewAdapter.getGroupCount() > 1) {
            receiptExpandableListView.expandGroup(0);
            receiptExpandableListView.expandGroup(1);
        } else {
            receiptExpandableListView.expandGroup(0);
        }

        expandableListViewAdapter.notifyDataSetChanged();
    }

    private void initListeners() {

        receiptExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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

        receiptExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
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
        receiptExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        // ExpandableListView Group collapsed listener
        receiptExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
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