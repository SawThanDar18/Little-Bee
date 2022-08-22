package com.busybees.lauk_kaing_expert_services.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.EventBusModel.AlertModel;
import com.busybees.lauk_kaing_expert_services.EventBusModel.EventBusCall;
import com.busybees.lauk_kaing_expert_services.EventBusModel.GoToCart;
import com.busybees.lauk_kaing_expert_services.EventBusModel.LCModel;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.ImageLeadFormActivity;
import com.busybees.lauk_kaing_expert_services.activity.LeadFormActivity;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.activity.ProductActivity;
import com.busybees.lauk_kaing_expert_services.activity.ServiceDetailActivity;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.MoreServiceDetailAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.ServicesTitleAndIconAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices.ExpandableListViewAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Products.ServiceDetailAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartModel;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartObj;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartDataModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartObj;
import com.busybees.lauk_kaing_expert_services.data.models.GetProductPriceModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductPriceVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.RecyclerItemClickListener;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    ArrayList<GetCartDataModel> cartDatas = new ArrayList<>();

    int posi = 0;

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
                pStepObj.setPhone(userVO.getPhone());
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
            productsCarryObject.setPhone(userVO.getPhone());
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
            productsCarryObject.setPhone(userVO.getPhone());
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
            productsCarryObject.setPhone(userVO.getPhone());
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
                            EventBus.getDefault().post("refreshdata");
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

        } else if (v.getId() == R.id.selectText) {

            int position = (int) v.getTag(R.id.position);
            int numberCount = productPriceVOArrayList.get(position).getQuantity();

            if (userVO != null) {

                if (productPriceVOArrayList.get(position).getFormStatus() == 2) {

                    dismiss();
                    Intent intent = new Intent(getContext(), LeadFormActivity.class);
                    intent.putExtra("key", 1);
                    intent.putExtra("phone", userVO.getPhone());
                    intent.putExtra("product_price_id", productPriceVOArrayList.get(position).getId());
                    intent.putExtra("position", position);
                    intent.putExtra("product_data", productsCarryObject);
                    intent.putExtra("getmorelist", serviceAvailableVOArrayList);
                    intent.putExtra("productlist", productsVOArrayList);
                    intent.putExtra("subproductlist", subProductsVOArrayList);
                    startActivity(intent);

                } else if (productPriceVOArrayList.get(position).getFormStatus() == 0) {

                    AddToCartObj addToCartObj = new AddToCartObj();
                    addToCartObj.setPhone(userVO.getPhone());
                    addToCartObj.setProductPriceId(productPriceVOArrayList.get(position).getId());
                    addToCartObj.setFormStatus(productPriceVOArrayList.get(position).getFormStatus());

                    if (numberCount == 0) {
                        addToCartObj.setQuantity(1);
                        CallAddToCart(addToCartObj);
                        productPriceVOArrayList.get(position).setQuantity(1);

                    } else {
                        addToCartObj.setQuantity(0);
                        CallAddToCart(addToCartObj);
                        productPriceVOArrayList.get(position).setQuantity(0);
                    }
                    posi = position;
                } else {
                    if (productPriceVOArrayList.get(position).getOriginalPrice() == 0) {
                        dismiss();
                        Intent intent = new Intent(getContext(), ImageLeadFormActivity.class);
                        intent.putExtra("key", 1);
                        intent.putExtra("phone", userVO.getPhone());
                        intent.putExtra("product_price_id", productPriceVOArrayList.get(position).getId());
                        intent.putExtra("position", position);
                        intent.putExtra("product_data", productsCarryObject);
                        intent.putExtra("getmorelist", serviceAvailableVOArrayList);
                        intent.putExtra("productlist", productsVOArrayList);
                        intent.putExtra("subproductlist", subProductsVOArrayList);
                        startActivity(intent);
                    } else {
                        AddToCartObj addToCartObj = new AddToCartObj();
                        addToCartObj.setPhone(userVO.getPhone());
                        addToCartObj.setProductPriceId(productPriceVOArrayList.get(position).getId());
                        addToCartObj.setFormStatus(productPriceVOArrayList.get(position).getFormStatus());

                        if (productPriceVOArrayList.get(position).getQuantity() == 0) {
                            addToCartObj.setQuantity(1);
                            CallAddToCart(addToCartObj);
                            productPriceVOArrayList.get(position).setQuantity(1);

                        } else {
                            addToCartObj.setQuantity(0);
                            CallAddToCart(addToCartObj);
                            productPriceVOArrayList.get(position).setQuantity(0);
                        }
                        posi = position;
                    }
                }
            } else {


                AppStorePreferences.putInt(getContext(), AppENUM.POSITION, position);
                AppStorePreferences.putInt(getContext(), AppENUM.NUMBER, numberCount);

                Intent intent = new Intent(getContext(), LogInActivity.class);
                startActivity(intent);

            }

        }  else if (v.getId() == R.id.cancel) {
            int position = (int) v.getTag(R.id.position);
            ProductPriceVO sdpModel = productPriceVOArrayList.get(position);
            int quantity = sdpModel.getQuantity();
            if(sdpModel.getFormStatus() == 2) {
                AddToCartObj addToCartObj = new AddToCartObj();
                addToCartObj.setPhone(userVO.getPhone());
                addToCartObj.setProductPriceId(sdpModel.getId());
                addToCartObj.setQuantity(0);
                addToCartObj.setFormStatus(2);
                CallAddToCart(addToCartObj);

            } else if (sdpModel.getFormStatus() == 1) {
                if (sdpModel.getOriginalPrice() == 0) {
                    AddToCartObj addToCartObj = new AddToCartObj();
                    addToCartObj.setPhone(userVO.getPhone());
                    addToCartObj.setProductPriceId(sdpModel.getId());
                    addToCartObj.setQuantity(0);
                    addToCartObj.setFormStatus(1);
                    CallAddToCart(addToCartObj);
                } else {
                    if (quantity > 0) {
                        quantity--;
                        if (quantity == 0) {
                            AddToCartObj addToCartObj = new AddToCartObj();
                            addToCartObj.setPhone(userVO.getPhone());
                            addToCartObj.setQuantity(0);
                            addToCartObj.setProductPriceId(sdpModel.getId());
                            addToCartObj.setFormStatus(1);
                            CallAddToCart(addToCartObj);
                            productPriceVOArrayList.get(position).setQuantity(1);
                            posi = position;
                        } else {
                            AddToCartObj addToCartObj = new AddToCartObj();
                            addToCartObj.setPhone(userVO.getPhone());
                            addToCartObj.setQuantity(0);
                            addToCartObj.setProductPriceId(sdpModel.getId());
                            addToCartObj.setFormStatus(1);
                            CallAddToCart(addToCartObj);
                            productPriceVOArrayList.get(position).setQuantity(0);
                            posi = position;

                        }
                    }
                }
            } else {
                if (quantity > 0) {
                    quantity--;
                    if (quantity == 0) {
                        AddToCartObj addToCartObj = new AddToCartObj();
                        addToCartObj.setPhone(userVO.getPhone());
                        addToCartObj.setQuantity(0);
                        addToCartObj.setProductPriceId(sdpModel.getId());
                        addToCartObj.setFormStatus(0);
                        CallAddToCart(addToCartObj);
                        productPriceVOArrayList.get(position).setQuantity(1);
                        posi = position;
                    } else {
                        AddToCartObj addToCartObj = new AddToCartObj();
                        addToCartObj.setPhone(userVO.getPhone());
                        addToCartObj.setQuantity(0);
                        addToCartObj.setProductPriceId(sdpModel.getId());
                        addToCartObj.setFormStatus(0);
                        CallAddToCart(addToCartObj);
                        productPriceVOArrayList.get(position).setQuantity(0);
                        posi = position;

                    }
                }
            }


        }

    }

    public void CallAddToCart(AddToCartObj obj) {

        if (Utility.isOnline(getContext())) {
            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.AddToCartCall(ApiConstants.BASE_URL + ApiConstants.GET_ADD_TO_CART, obj).enqueue(new Callback<AddToCartModel>() {
                @Override
                public void onResponse(Call<AddToCartModel> call, Response<AddToCartModel> response) {

                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError() == true) {

                        Utility.showToast(getContext(), response.body().getMessage());

                    } else if (response.body().getError() == false) {

                        if (userVO != null) {

                            ProductsCarryObject pStepObj = new ProductsCarryObject();
                            pStepObj.setPhone(userVO.getPhone());
                            pStepObj.setServiceId(productsCarryObject.getServiceId());
                            pStepObj.setProductId(productsCarryObject.getProductId());
                            pStepObj.setSubProductId(productsCarryObject.getSubProductId());
                            pStepObj.setProductPriceId(productsCarryObject.getProductPriceId());
                            pStepObj.setStep(productsCarryObject.getStep());
                            CallProductPriceApi(pStepObj);

                            CallGetCart();
                            EventBus.getDefault().post("refreshdata");

                        } else {
                            startActivity(new Intent(getContext(), LogInActivity.class));

                        }

                    }

                }

                @Override
                public void onFailure(Call<AddToCartModel> call, Throwable t) {
                    Utility.showToast(getContext(), t.getMessage());
                }
            });

        } else {
            Utility.showToast(getContext(), getString(R.string.no_internet));
        }
    }

    private void CallGetCart() {
        if (userVO != null) {
            GetCartObj cartObj = new GetCartObj();
            cartObj.setPhone(userVO.getPhone());

            if (Utility.isOnline(getContext())) {

                networkServiceProvider.GetCartCall(ApiConstants.BASE_URL + ApiConstants.GET_CART, cartObj).enqueue(new Callback<GetCartModel>() {
                    @Override
                    public void onResponse(Call<GetCartModel> call, Response<GetCartModel> response) {

                        progressBar.setVisibility(View.GONE);

                        //if (response.body().getError() == true) {

                            //Utility.showToast(getContext(), response.body().getMessage());

                        //} else if (response.body().getError() == false) {
                            cartDatas.clear();
                            cartDatas.addAll(response.body().getData());
                        //}
                    }

                    @Override
                    public void onFailure(Call<GetCartModel> call, Throwable t) {
                        Utility.showToast(getContext(), t.getMessage());
                    }
                });

            } else {
                Utility.showToast(getContext(), getString(R.string.no_internet));

            }
        }
    }

    public int GetPixelFromDips(float pixels) {
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

    @Override
    public void onResume() {
        CallGetCart();
        super.onResume();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getEventLcModel(LCModel lcModel) {

        int position = lcModel.getPosition();
        int numberCount = lcModel.getNumber_count();
        userVO = Utility.query_UserProfile(getContext());

        if (numberCount == 0) {

            AddToCartObj addToCartObj = new AddToCartObj();
            addToCartObj.setPhone(userVO.getPhone());
            addToCartObj.setProductPriceId(productPriceVOArrayList.get(position).getId());
            addToCartObj.setQuantity(1);
            addToCartObj.setFormStatus(productPriceVOArrayList.get(position).getFormStatus());
            CallAddToCart(addToCartObj);
            posi = position;

        } else {

            AddToCartObj addToCartObj = new AddToCartObj();
            addToCartObj.setPhone(userVO.getPhone());
            addToCartObj.setProductPriceId(productPriceVOArrayList.get(position).getId());
            addToCartObj.setQuantity(0);
            CallAddToCart(addToCartObj);
            posi = position;
        }

    }

    @Subscribe
    public void getAlert(AlertModel alertModel) {

        GoToCart goToCart = new GoToCart();
        goToCart.setText("go");
        EventBus.getDefault().postSticky(goToCart);
        startActivity(new Intent(getContext(), MainActivity.class));
        dismiss();
    }
}
