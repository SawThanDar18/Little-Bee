package com.busybees.lauk_kaing_expert_services.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.ExpandableOrderAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.ExpandableReceiptAdapter;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class ReceiptFragments extends Fragment {

    private RelativeLayout logInView, noLogInView;
    private ExpandableListView receiptExpandableListView;
    private ExpandableReceiptAdapter expandableListViewAdapter;

    private TextView noDataTextView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        logInView = view.findViewById(R.id.loginView);
        noLogInView = view.findViewById(R.id.no_login_view);
        receiptExpandableListView = view.findViewById(R.id.receipt_expandable_list_view);
        noDataTextView = view.findViewById(R.id.no_data);
        progressBar = view.findViewById(R.id.materialLoader);

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

        initListeners();
        showOrders();

        return view;
    }

    private void showOrders() {
        expandableListViewAdapter = new ExpandableReceiptAdapter(getActivity());

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
}