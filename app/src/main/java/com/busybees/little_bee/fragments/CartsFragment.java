package com.busybees.little_bee.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busybees.little_bee.Dialog.DialogServiceDelete;
import com.busybees.little_bee.EventBusModel.EventBusCall;
import com.busybees.little_bee.EventBusModel.EventBusCartObj;
import com.busybees.little_bee.data.models.AddToCart.AddToCartModel;
import com.busybees.little_bee.data.models.AddToCart.AddToCartObj;
import com.busybees.little_bee.data.models.GetCart.GetCartDataModel;
import com.busybees.little_bee.data.models.GetCart.GetCartModel;
import com.busybees.little_bee.data.models.GetCart.GetCartObj;
import com.busybees.little_bee.data.vos.Users.UserVO;
import com.busybees.little_bee.MainActivity;
import com.busybees.little_bee.R;
import com.busybees.little_bee.activity.AddressActivity;
import com.busybees.little_bee.activity.LogInActivity;
import com.busybees.little_bee.adapters.Carts.CartsListAdapter;
import com.busybees.little_bee.network.NetworkServiceProvider;
import com.busybees.little_bee.utility.ApiConstants;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import me.myatminsoe.mdetect.MDetect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartsFragment extends Fragment {

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RelativeLayout cartTimeline, logInView, noLogInView, noServicesView;
    private LinearLayout continueLayout, reloadPage;

    private RecyclerView cartsRecyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private CartsListAdapter cartsListAdapter;

    private ImageView homePageView;
    private TextView cartCountText, amountText;
    private Button goToLogInBtn, goToServicesBtn, reloadBtn;

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    ArrayList<GetCartDataModel> cartDatas = new ArrayList<>();

    private boolean isCartItemAvailable;
    private boolean continue_error;
    private String continue_error_msg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carts, container, false);

        networkServiceProvider = new NetworkServiceProvider(getContext());
        userVO = Utility.query_UserProfile(getContext());

        progressBar = view.findViewById(R.id.materialLoader);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        cartTimeline = view.findViewById(R.id.cart_timeline);
        logInView = view.findViewById(R.id.loginView);
        noLogInView = view.findViewById(R.id.no_login_view);
        noServicesView = view.findViewById(R.id.no_service_view);
        continueLayout = view.findViewById(R.id.continue_layout);
        reloadPage = view.findViewById(R.id.reload_page);
        homePageView = view.findViewById(R.id.home_page_btn);
        cartsRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartCountText = view.findViewById(R.id.cartCountText);
        amountText = view.findViewById(R.id.amountText);
        goToLogInBtn = view.findViewById(R.id.go_to_login);
        goToServicesBtn = view.findViewById(R.id.go_to_service);
        reloadBtn = view.findViewById(R.id.btn_reload_page);

        if (Utility.isOnline(getContext())) {
            reloadPage.setVisibility(View.GONE);
            cartTimeline.setVisibility(View.VISIBLE);
            logInView.setVisibility(View.VISIBLE);

            if (userVO != null) {
                noLogInView.setVisibility(View.GONE);
                logInView.setVisibility(View.VISIBLE);
                cartTimeline.setVisibility(View.VISIBLE);
                CallGetCart();
            } else {
                noLogInView.setVisibility(View.VISIBLE);
                logInView.setVisibility(View.GONE);
                cartTimeline.setVisibility(View.GONE);
            }

        } else {
            reloadPage.setVisibility(View.VISIBLE);
            cartTimeline.setVisibility(View.GONE);
            logInView.setVisibility(View.GONE);
        }

        setUpAdapter();
        onClick();
        CallGetCart();

        return view;
    }

    private void CallGetCart() {
        if (userVO != null) {
            GetCartObj cartObj = new GetCartObj();
            cartObj.setPhone(userVO.getPhone());

            progressBar.setVisibility(View.VISIBLE);

            if (Utility.isOnline(getContext())) {

                networkServiceProvider.GetCartCall(ApiConstants.BASE_URL + ApiConstants.GET_CART, cartObj).enqueue(new Callback<GetCartModel>() {
                    @Override
                    public void onResponse(Call<GetCartModel> call, Response<GetCartModel> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.body().getError() == false) {
                            reloadPage.setVisibility(View.GONE);

                            if (response.body().getContinueError() != null) {
                                continue_error = response.body().getContinueError();
                                continue_error_msg = response.body().getContinueAlertMsg();
                            }

                            cartDatas.clear();
                            cartDatas.addAll(response.body().getData());
                            cartsListAdapter = new CartsListAdapter(getActivity(), cartDatas);
                            cartsRecyclerView.setAdapter(cartsListAdapter);
                            cartsListAdapter.notifyDataSetChanged();

                            cartsListAdapter.setCLick(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AdapterClick(v);
                                }
                            });

                            if (!cartDatas.isEmpty() && cartDatas != null) {
                                continueLayout.setVisibility(View.VISIBLE);
                                isCartItemAvailable = true;
                                cartTimeline.setVisibility(View.VISIBLE);
                                noServicesView.setVisibility(View.GONE);

                                cartCountText.setText(String.valueOf(response.body().getTotalQuantity()));
                                if (response.body().getTotal() != 0) {
                                    amountText.setText(getString(R.string.currency) + " " + response.body().getTotal());
                                } else {
                                    amountText.setText("Survey");
                                }

                            } else {
                                continueLayout.setVisibility(View.GONE);
                                isCartItemAvailable = false;
                                cartTimeline.setVisibility(View.GONE);
                                noServicesView.setVisibility(View.VISIBLE);

                                cartCountText.setText("0");
                                amountText.setText(" ");
                            }
                        } else {
                            Utility.showToast(getContext(), response.body().getMessage());
                        }

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

    private void AdapterClick(View v) {

        if (v.getId() == R.id.decreseBtn) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel dataModel = cartDatas.get(position);
            int quantity = dataModel.getQuantity();

            if (quantity == 1) {
                Utility.showToast(getActivity(), getString(R.string.qty_zero));
            } else {

                if (quantity > 0) {
                    quantity--;
                    if (quantity == 0) {
                        if (cartDatas != null) {
                            if (cartDatas.isEmpty()) {
                                Utility.showToast(getActivity(), getString(R.string.no_item_cart));
                            }
                        }
                        dataModel.setQuantity(Integer.valueOf(quantity + ""));

                        AddToCartObj addToCartObj = new AddToCartObj();
                        addToCartObj.setPhone(userVO.getPhone());
                        addToCartObj.setQuantity(dataModel.getQuantity());
                        addToCartObj.setProductPriceId(dataModel.getProductPriceId());
                        addToCartObj.setFormStatus(dataModel.getFormStatus());

                        CallAddToCart(addToCartObj);
                    } else {
                        dataModel.setQuantity(Integer.valueOf(quantity + ""));

                        AddToCartObj addToCartObj = new AddToCartObj();
                        addToCartObj.setPhone(userVO.getPhone());
                        addToCartObj.setQuantity(dataModel.getQuantity());
                        addToCartObj.setProductPriceId(dataModel.getProductPriceId());
                        addToCartObj.setFormStatus(dataModel.getFormStatus());

                        CallAddToCart(addToCartObj);
                    }
                }

            }

        } else if (v.getId() == R.id.increaseBtn) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel dataModel = cartDatas.get(position);
            int quantity = dataModel.getQuantity();
            if (quantity < 10) {
                dataModel.setQuantity(Integer.valueOf((quantity + 1) + ""));

                AddToCartObj addToCartObj = new AddToCartObj();
                addToCartObj.setPhone(userVO.getPhone());
                addToCartObj.setQuantity(dataModel.getQuantity());
                addToCartObj.setProductPriceId(dataModel.getProductPriceId());
                addToCartObj.setFormStatus(dataModel.getFormStatus());

                CallAddToCart(addToCartObj);

            } else {
                Utility.showToast(getActivity(), getString(R.string.cant_update_more_ten));
            }
        } else if (v.getId() == R.id.delete_cart) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel dataModel = cartDatas.get(position);
            dataModel.setQuantity(0);
            DialogServiceDelete dialogServiceDelete = new DialogServiceDelete(dataModel);
            dialogServiceDelete.show(getFragmentManager(), "");

        } else if (v.getId() == R.id.cancel) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel sdpModel = cartDatas.get(position);
            int quantity = Integer.parseInt(String.valueOf(sdpModel.getQuantity()));
            if(Double.parseDouble(String.valueOf(cartDatas.get(position).getFormStatus())) == 2) {
                AddToCartObj addToCartObj = new AddToCartObj();
                addToCartObj.setPhone(userVO.getPhone());
                addToCartObj.setProductPriceId(cartDatas.get(position).getProductPriceId());
                addToCartObj.setQuantity(0);
                addToCartObj.setFormStatus(2);
                CallAddToCart(addToCartObj);

            }else {
                if (quantity > 0) {
                    quantity--;
                    if (quantity == 0) {
                        if (cartDatas != null) {
                            if (cartDatas.isEmpty()) {
                                Utility.showToast(getActivity(), getString(R.string.no_item_cart));
                            }
                        }
                        sdpModel.setQuantity(0);
                        DialogServiceDelete serviceDelete = new DialogServiceDelete(sdpModel);
                        serviceDelete.show(getFragmentManager(), "");
                    } else {
                        sdpModel.setQuantity(0);
                        DialogServiceDelete serviceDelete = new DialogServiceDelete(sdpModel);
                        serviceDelete.show(getFragmentManager(), "");

                    }
                } else {
                    sdpModel.setQuantity(0);
                    DialogServiceDelete serviceDelete = new DialogServiceDelete(sdpModel);
                    serviceDelete.show(getFragmentManager(), "");
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

                        CallGetCart();

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

    private void setUpAdapter() {
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        cartsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
    }

    private void onClick() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CallGetCart();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        continueLayout.setOnClickListener(v -> {
            if (continue_error == true) {
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View priceZeroDialogView = factory.inflate(R.layout.dialog_alert_price_zero, null);
                final AlertDialog priceZeroDialog = new AlertDialog.Builder(getContext()).create();
                priceZeroDialog.setView(priceZeroDialogView);

                priceZeroDialog.setCancelable(false);
                priceZeroDialog.setCanceledOnTouchOutside(false);

                if (priceZeroDialog != null && priceZeroDialog.getWindow() != null) {
                    priceZeroDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    priceZeroDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                }

                TextView alertMsg = priceZeroDialogView.findViewById(R.id.price_zero_alert_txt);

                if (checkLng(getContext()).equalsIgnoreCase("it") || checkLng(getContext()).equalsIgnoreCase("fr")) {

                    if (MDetect.INSTANCE.isUnicode()) {
                        alertMsg.setText(getString(R.string.continue_error_msg));
                    } else {
                        alertMsg.setText(getString(R.string.continue_error_msg));
                    }

                } else {
                    alertMsg.setText(continue_error_msg);
                }

                priceZeroDialogView.findViewById(R.id.price_zero_ok_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        priceZeroDialog.dismiss();
                    }
                });

                priceZeroDialog.show();

            } else {
                startActivity(new Intent(getContext(), AddressActivity.class));
            }
        });

        homePageView.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        goToLogInBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LogInActivity.class));
        });

        goToServicesBtn.setOnClickListener(v -> {
            EventBusCall service = new EventBusCall();
            service.setService("1");
            EventBus.getDefault().post(service);
        });

        reloadBtn.setOnClickListener(v -> {
            if (Utility.isOnline(getActivity())) {
                CallGetCart();
            } else {
                Utility.showToast(getActivity(), getString(R.string.no_internet));
            }
        });
    }

    public static String checkLng(Context activity) {
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if (lang == null) {
            lang = "en";
        }
        return lang;
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
    public void getServiceDeleteID(EventBusCartObj obj) {
        AddToCartObj addToCartObj = new AddToCartObj();
        addToCartObj.setQuantity(obj.getQuantity());
        addToCartObj.setPhone(obj.getPhone());
        addToCartObj.setProductPriceId(obj.getId());
        addToCartObj.setFormStatus(obj.getFormStatus());

        CallAddToCart(addToCartObj);

    }

    @Subscribe
    public void getEventBusCart(String home) {

        userVO = Utility.query_UserProfile(getActivity());
    }
}