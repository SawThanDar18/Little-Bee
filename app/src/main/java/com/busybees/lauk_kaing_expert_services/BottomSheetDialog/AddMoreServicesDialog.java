package com.busybees.lauk_kaing_expert_services.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.ServicesTitleAndIconAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.ExpandableListViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class AddMoreServicesDialog extends BottomSheetDialogFragment {

    private RecyclerView addMoreServiceRecyclerView, servicesRecyclerView;
    private ExpandableListView expandableListView;
    private ServicesTitleAndIconAdapter servicesTitleAndIconAdapter;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private LinearLayoutManager servicesLayoutManager, addMoreServiceLayoutManager;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.add_more_service, null);
        dialog.setContentView(contentView);

        addMoreServiceRecyclerView = contentView.findViewById(R.id.recycle_add_more_service);
        servicesRecyclerView = contentView.findViewById(R.id.recycle_service);
        expandableListView = contentView.findViewById(R.id.expandableListView);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expandableListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(10));

        setUpAdapter();
        initListeners();
        ShowProduct();
    }

    private void initListeners() {

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> false);

        expandableListView.setOnGroupExpandListener(groupPosition -> {

        });

        expandableListView.setOnGroupCollapseListener(groupPosition -> {

        });

    }

    private void setUpAdapter() {
        ArrayList<Integer> servicesImageList = new ArrayList<>();
        servicesImageList.add(R.drawable.icon_cleaning__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_general_workers_hiring__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_air_con__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_plumbing__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_electrical__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_laundry__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_cctv__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_painting__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_ceiling__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_upholstery__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_stone_care__scrubbing___polishing___1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_pest_control__1200_x_1200_px_);

        ArrayList<String> serviceName = new ArrayList<>();
        serviceName.add("Cleaning");
        serviceName.add("General Worker Hiring");
        serviceName.add("Air-Con");
        serviceName.add("Plumbing");
        serviceName.add("Electrical");
        serviceName.add("Laundry");
        serviceName.add("CCTV");
        serviceName.add("Painting");
        serviceName.add("Ceiling");
        serviceName.add("Upholstery");
        serviceName.add("Stone Care (Scrubbing & Polishing)");
        serviceName.add("Pest Control");

        servicesTitleAndIconAdapter = new ServicesTitleAndIconAdapter(getActivity(), servicesImageList, serviceName);
        addMoreServiceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addMoreServiceRecyclerView.setLayoutManager(addMoreServiceLayoutManager);
        addMoreServiceRecyclerView.setAdapter(servicesTitleAndIconAdapter);
        servicesTitleAndIconAdapter.notifyDataSetChanged();
        servicesTitleAndIconAdapter.setClick(this);
    }

    public void ShowProduct() {

        expandableListViewAdapter = new ExpandableListViewAdapter(getActivity());
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListViewAdapter.notifyDataSetChanged();
    }

    public int GetPixelFromDips(float pixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }
}
