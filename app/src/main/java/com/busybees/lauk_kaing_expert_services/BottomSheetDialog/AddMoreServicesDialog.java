package com.busybees.lauk_kaing_expert_services.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.data.models.GetProductPriceModel;
import com.busybees.data.vos.Home.ServiceAvailableVO;
import com.busybees.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.data.vos.ServiceDetail.ProductPriceVO;
import com.busybees.data.vos.ServiceDetail.ProductsVO;
import com.busybees.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.activity.ProductActivity;
import com.busybees.lauk_kaing_expert_services.activity.ServiceDetailActivity;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.MoreServiceDetailAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.ServicesTitleAndIconAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.ExpandableListViewAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Products.ServiceDetailAdapter;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.RecyclerItemClickListener;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMoreServicesDialog extends BottomSheetDialogFragment {

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    private RelativeLayout serviceDetailLayout, selectServiceLayout, comingSoonLayout, servicesLayout;
    private RecyclerView addMoreServiceRecyclerView, servicesDetailRecyclerView;
    private ExpandableListView expandableListView;
    private ServicesTitleAndIconAdapter servicesTitleAndIconAdapter;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private LinearLayoutManager servicesLayoutManager, addMoreServiceLayoutManager;

    private ImageView closeDialog, back;
    private ProgressBar progressBar;

    private ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList = new ArrayList<>();
    private ArrayList<ProductsVO> productsVOArrayList = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

    private ArrayList<ProductsVO> productsVOS = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOS = new ArrayList<>();

    private MoreServiceDetailAdapter serviceDetailAdapter;
    private ArrayList<ProductPriceVO> productPriceVOArrayList = new ArrayList<>();

    private ProductsCarryObject productsCarryObject = new ProductsCarryObject();

    public AddMoreServicesDialog(ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList, ArrayList<ProductsVO> productsVOArrayList, ArrayList<SubProductsVO> subProductsVOArrayList) {
        this.serviceAvailableVOArrayList = serviceAvailableVOArrayList;
        this.productsVOArrayList = productsVOArrayList;
        this.subProductsVOArrayList = subProductsVOArrayList;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.add_more_service, null);
        dialog.setContentView(contentView);

        networkServiceProvider = new NetworkServiceProvider(getContext());
        userVO = Utility.query_UserProfile(getContext());

        comingSoonLayout = contentView.findViewById(R.id.coming_soon_layout);
        servicesLayout = contentView.findViewById(R.id.services_layout);
        serviceDetailLayout = contentView.findViewById(R.id.service_detail_layout);
        selectServiceLayout = contentView.findViewById(R.id.select_layout);
        addMoreServiceRecyclerView = contentView.findViewById(R.id.recycle_add_more_service);
        servicesDetailRecyclerView = contentView.findViewById(R.id.recycle_service_detail);
        expandableListView = contentView.findViewById(R.id.expandableListView);
        closeDialog = contentView.findViewById(R.id.cancel_more_service);
        back = contentView.findViewById(R.id.back_button);
        progressBar = contentView.findViewById(R.id.materialLoader);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expandableListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(10));

        if (getArguments() != null) {
            productsCarryObject = (ProductsCarryObject) getArguments().getSerializable("product_data");

            if (productsCarryObject != null) {
                progressBar.setVisibility(View.VISIBLE);
                ProductsCarryObject pStepObj = new ProductsCarryObject();
                pStepObj.setServiceId(productsCarryObject.getServiceId());
                pStepObj.setProductId(productsCarryObject.getProductId());
                pStepObj.setSubProductId(productsCarryObject.getSubProductId());
                pStepObj.setProductPriceId(productsCarryObject.getProductPriceId());
                pStepObj.setStep(productsCarryObject.getStep());
                CallProductPriceApi(pStepObj);

            }
        }

        setUpAdapter();
        initListeners();
        ShowProduct(serviceAvailableVOArrayList.get(0));
        onClick();
    }

    private void onClick() {
        closeDialog.setOnClickListener(v -> dismiss());

        back.setOnClickListener(v -> {
            serviceDetailLayout.setVisibility(View.GONE);
            selectServiceLayout.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        });

        expandableListView.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            v.onTouchEvent(event);
            return true;
        });
    }

    private void initListeners() {

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            productsCarryObject.setServiceId(productsVOS.get(groupPosition).getSubProducts().get(childPosition).getServiceId());
            productsCarryObject.setProductId(productsVOS.get(groupPosition).getSubProducts().get(childPosition).getProductId());
            productsCarryObject.setSubProductId(productsVOS.get(groupPosition).getSubProducts().get(childPosition).getSubProductId());
            productsCarryObject.setStep(productsVOS.get(groupPosition).getSubProducts().get(childPosition).getStep());

            if (productsCarryObject != null) {

                progressBar.setVisibility(View.VISIBLE);
                CallProductPriceApi(productsCarryObject);
            }
            return false;
        });

        expandableListView.setOnGroupExpandListener(groupPosition -> {
            productsCarryObject.setServiceId(productsVOS.get(groupPosition).getServiceId());
            productsCarryObject.setProductId(productsVOS.get(groupPosition).getProductId());
            productsCarryObject.setStep(productsVOS.get(groupPosition).getStep());
            if (productsCarryObject != null) {
                if (productsCarryObject.getStep() == 2) {
                    progressBar.setVisibility(View.VISIBLE);
                    CallProductPriceApi(productsCarryObject);
                }
            }
        });

        expandableListView.setOnGroupCollapseListener(groupPosition -> {

        });

    }

    private void setUpAdapter() {

        addMoreServiceLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addMoreServiceRecyclerView.setLayoutManager(addMoreServiceLayoutManager);
        servicesTitleAndIconAdapter = new ServicesTitleAndIconAdapter(getActivity(), serviceAvailableVOArrayList);
        addMoreServiceRecyclerView.setAdapter(servicesTitleAndIconAdapter);
        servicesTitleAndIconAdapter.notifyDataSetChanged();
        servicesTitleAndIconAdapter.setClick(this);

        servicesLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        servicesDetailRecyclerView.setLayoutManager(servicesLayoutManager);
    }

    public void ShowProduct(ServiceAvailableVO serviceAvailableVO) {

        productsVOS.clear();
        subProductsVOS.clear();

        if (serviceAvailableVO.getStep() == 1) {
            ProductsCarryObject productsCarryObject = new ProductsCarryObject();
            productsCarryObject.setServiceId(serviceAvailableVO.getServiceId());
            productsCarryObject.setStep(serviceAvailableVO.getStep());

            CallProductPriceApi(productsCarryObject);
        }

        for (int i = 0; i < productsVOArrayList.size(); i++) {
            if (productsVOArrayList.get(i).getServiceId().equals(serviceAvailableVO.getServiceId())) {
                ProductsVO product = new ProductsVO();
                product.setServiceId(productsVOArrayList.get(i).getServiceId());
                product.setProductId(productsVOArrayList.get(i).getProductId());
                product.setStep(productsVOArrayList.get(i).getStep());
                product.setName(productsVOArrayList.get(i).getName());
                product.setNameMm(productsVOArrayList.get(i).getNameMm());
                product.setNameCh(productsVOArrayList.get(i).getNameCh());
                product.setProductImage(productsVOArrayList.get(i).getProductImage());
                product.setSubProducts(productsVOArrayList.get(i).getSubProducts());
                productsVOS.add(product);
            }
        }
        for (int j = 0; j < productsVOS.size(); j++) {
            subProductsVOS.addAll(productsVOS.get(j).getSubProducts());
        }

        expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), productsVOS, subProductsVOS);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListViewAdapter.notifyDataSetChanged();
    }

    private void CallProductPriceApi(ProductsCarryObject productsCarryObject) {
        if (Utility.isOnline(getContext())) {
            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.GetProductPriceCall(ApiConstants.BASE_URL + ApiConstants.GET_PRODUCT_PRICE_LISTS, productsCarryObject).enqueue(new Callback<GetProductPriceModel>() {
                @Override
                public void onResponse(Call<GetProductPriceModel> call, Response<GetProductPriceModel> response) {

                    progressBar.setVisibility(View.VISIBLE);

                    if (response.body().getError() == true) {

                        //videoView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        Utility.showToast(getContext(), response.body().getMessage());
                        dismiss();

                    } else if (response.body().getError() == false) {

                        progressBar.setVisibility(View.GONE);
                        back.setVisibility(View.VISIBLE);
                        //videoView.setVisibility(View.VISIBLE);
                        //PlayVideo("https://busybeesexpertservice.com//assets/video/CCTV_Services.mp4");

                        selectServiceLayout.setVisibility(View.GONE);
                        serviceDetailLayout.setVisibility(View.VISIBLE);

                        productPriceVOArrayList.clear();
                        productPriceVOArrayList.addAll(response.body().getData());

                        if (!productPriceVOArrayList.isEmpty()) {
                            serviceDetailAdapter = new MoreServiceDetailAdapter(getActivity(), productPriceVOArrayList);
                            servicesDetailRecyclerView.setAdapter(serviceDetailAdapter);
                            serviceDetailAdapter.notifyDataSetChanged();

                            serviceDetailAdapter.setClick(v -> AdapterCLick(v));
                        } else {
                            servicesLayout.setVisibility(View.GONE);
                            comingSoonLayout.setVisibility(View.VISIBLE);
                            back.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProductPriceModel> call, Throwable t) {
                    Utility.showToast(getContext(), t.getMessage());
                }
            });

        } else {
            progressBar.setVisibility(View.VISIBLE);
            Utility.showToast(getContext(), getString(R.string.no_internet));
        }
    }

    private void AdapterCLick(View v) {

        if (v.getId() == R.id.hideShowDetail) {
            int position = (int) v.getTag(R.id.position);
            ProductPriceVO productPriceVO = productPriceVOArrayList.get(position);
            productPriceVO.setShowDetail(!productPriceVO.isShowDetail());
            serviceDetailAdapter.notifyItemChanged(position);

        }

    }

    public int GetPixelFromDips(float pixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }
}
