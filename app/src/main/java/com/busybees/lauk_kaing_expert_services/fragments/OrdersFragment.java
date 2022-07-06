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

import com.busybees.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.ExpandableOrderAdapter;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class OrdersFragment extends Fragment {

    private RelativeLayout logInView, noLogInView;
    private ExpandableListView orderExpandableListView;
    private ExpandableOrderAdapter expandableListViewAdapter;
    private LinearLayout reloadPage;
    private Button goToLogInBtn;

    private TextView noDataTextView;
    private ProgressBar progressBar;

    private UserVO userVO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

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
        showOrders();
        onClick();
        return view;
    }

    private void onClick() {
        goToLogInBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LogInActivity.class));
        });
    }

    private void showOrders() {
        expandableListViewAdapter = new ExpandableOrderAdapter(getActivity());

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
}