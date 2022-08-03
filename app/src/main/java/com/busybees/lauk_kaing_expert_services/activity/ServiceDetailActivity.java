package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.EventBusModel.AlertModel;
import com.busybees.lauk_kaing_expert_services.EventBusModel.GoToCart;
import com.busybees.lauk_kaing_expert_services.EventBusModel.LCModel;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartModel;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartObj;
import com.busybees.lauk_kaing_expert_services.data.models.GetProductPriceModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductPriceVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Products.ServiceDetailAdapter;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailActivity extends AppCompatActivity {

    private NetworkServiceProvider serviceProvider;
    private UserVO userVO;

    private TextView serviceDetailName;
    private ImageView back;

    private VideoView videoView;

    private LinearLayout continueLayout;

    private ProgressBar progressBar;

    private RecyclerView serviceDetailRecyclerView;
    private ServiceDetailAdapter serviceDetailAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ProductPriceVO> productPriceVOArrayList = new ArrayList<>();

    // Intent Data
    private String productName;
    private ProductsCarryObject productsCarryObject;
    int posi = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_service_detail);

        serviceProvider = new NetworkServiceProvider(this);
        userVO = Utility.query_UserProfile(this);

        videoView = findViewById(R.id.video_view);
        back = findViewById(R.id.back_button);
        serviceDetailRecyclerView = findViewById(R.id.service_detail_recyclerview);
        continueLayout = findViewById(R.id.continue_layout);
        serviceDetailName = findViewById(R.id.service_detail_name);
        progressBar = findViewById(R.id.materialLoader);

        setUpRecyclerView();
        onClick();

        if (getIntent() != null) {
            productName = getIntent().getStringExtra("product_title");
            productsCarryObject = (ProductsCarryObject) getIntent().getSerializableExtra("product_detail_data");
            serviceDetailName.setText(productName);
            CallProductPriceApi(productsCarryObject);
        }

    }

    private void CallProductPriceApi(ProductsCarryObject productsCarryObject) {
        if (Utility.isOnline(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);

            serviceProvider.GetProductPriceCall(ApiConstants.BASE_URL + ApiConstants.GET_PRODUCT_PRICE_LISTS, productsCarryObject).enqueue(new Callback<GetProductPriceModel>() {
                @Override
                public void onResponse(Call<GetProductPriceModel> call, Response<GetProductPriceModel> response) {

                    progressBar.setVisibility(View.VISIBLE);
                    
                    if (response.body().getError() == true) {

                        videoView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        Utility.showToast(ServiceDetailActivity.this, response.body().getMessage());
                        finish();

                    } else if (response.body().getError() == false) {

                        progressBar.setVisibility(View.GONE);
                        //videoView.setVisibility(View.VISIBLE);
                        //PlayVideo("https://busybeesexpertservice.com//assets/video/CCTV_Services.mp4");

                        productPriceVOArrayList.clear();
                        productPriceVOArrayList.addAll(response.body().getData());

                        if (!productPriceVOArrayList.isEmpty()) {
                            serviceDetailAdapter = new ServiceDetailAdapter(ServiceDetailActivity.this, productPriceVOArrayList);
                            serviceDetailRecyclerView.setAdapter(serviceDetailAdapter);
                            serviceDetailAdapter.notifyDataSetChanged();
                            serviceDetailRecyclerView.smoothScrollToPosition(posi);

                            serviceDetailAdapter.setClick(v -> AdapterCLick(v));
                        } else {
                            finish();
                            Utility.showToast(getApplicationContext(), "Coming Soon");
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProductPriceModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        } else {
            progressBar.setVisibility(View.VISIBLE);
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
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
                    Utility.showToast(getApplicationContext(), "form 2");

                    /*finish();
                    Intent intent = new Intent(this, LeadFormActivity.class);
                    intent.putExtra("phone", userObj.getPhone());
                    intent.putExtra("product_price_id", String.valueOf(datalist.get(position).getProductPriceId()));
                    intent.putExtra("position", position);
                    intent.putExtra("product_data", pStepCarryObj);
                    startActivity(intent);*/

                } else if (productPriceVOArrayList.get(position).getFormStatus() == 0){

                    Utility.showToast(getApplicationContext(), "form 0");
                    AddToCartObj addToCartObj = new AddToCartObj();
                    addToCartObj.setPhone(userVO.getPhone());
                    addToCartObj.setQuantity(productPriceVOArrayList.get(position).getQuantity());
                    addToCartObj.setProductPriceId(String.valueOf(productPriceVOArrayList.get(position).getId()));
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
                    Utility.showToast(getApplicationContext(), "form 1");

                }
            } else {


                AppStorePreferences.putInt(this, AppENUM.POSITION, position);
                AppStorePreferences.putInt(this, AppENUM.NUMBER, numberCount);

                Intent intent = new Intent(ServiceDetailActivity.this, LogInActivity.class);
                startActivity(intent);

            }

        }

    }

    public void CallAddToCart(AddToCartObj obj) {

        if (Utility.isOnline(this)) {
            progressBar.setVisibility(View.VISIBLE);

            serviceProvider.AddToCartCall(ApiConstants.BASE_URL + ApiConstants.GET_ADD_TO_CART, obj).enqueue(new Callback<AddToCartModel>() {
                @Override
                public void onResponse(Call<AddToCartModel> call, Response<AddToCartModel> response) {

                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError() == true) {

                        Utility.showToast(ServiceDetailActivity.this, response.body().getMessage());

                    } else if (response.body().getError() == false) {

                        if (userVO != null) {

                            ProductsCarryObject pStepObj = new ProductsCarryObject();
                            pStepObj.setServiceId(productsCarryObject.getServiceId());
                            pStepObj.setProductId(productsCarryObject.getProductId());
                            pStepObj.setSubProductId(productsCarryObject.getSubProductId());
                            pStepObj.setProductPriceId(productsCarryObject.getProductPriceId());
                            pStepObj.setStep(productsCarryObject.getStep());
                            CallProductPriceApi(pStepObj);

                            //GetCart();

                        } else {
                            startActivity(new Intent(ServiceDetailActivity.this, LogInActivity.class));

                        }

                    }

                }

                @Override
                public void onFailure(Call<AddToCartModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        } else {
            Utility.showToast(ServiceDetailActivity.this, getString(R.string.no_internet));
        }
    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        continueLayout.setOnClickListener(v -> startActivity(new Intent(ServiceDetailActivity.this, AddressActivity.class)));
    }

    private void setUpRecyclerView() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        serviceDetailRecyclerView.setLayoutManager(layoutManager);
    }

    private void PlayVideo(String video_url) {
        /*videoView = findViewById(R.id.video_view);
        Uri video = Uri.parse(video_url);
        //player = ExoPlayerFactory.newSimpleInstance(this);
        videoView.setPlayer(player);
        videoView.setUseController(false);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"));
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(video));
        LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
        player.prepare(loopingSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(player.REPEAT_MODE_ALL);*/

        /*MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);*/
        Uri uri = Uri.parse(video_url);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnCompletionListener(mp -> {
            videoView.start();
        });

        videoView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 24);
            }
        });

        videoView.setClipToOutline(true);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getEventLcModel(LCModel lcModel) {

        int position = lcModel.getPosition();
        int numberCount = lcModel.getNumber_count();
        userVO = Utility.query_UserProfile(this);

        if (numberCount == 0) {

            AddToCartObj addToCartObj = new AddToCartObj();
            addToCartObj.setPhone(userVO.getPhone());
            addToCartObj.setProductPriceId(String.valueOf(productPriceVOArrayList.get(position).getId()));
            addToCartObj.setQuantity(1);
            addToCartObj.setFormStatus(productPriceVOArrayList.get(position).getFormStatus());
            CallAddToCart(addToCartObj);
            posi = position;

        } else {

            AddToCartObj addToCartObj = new AddToCartObj();
            addToCartObj.setPhone(userVO.getPhone());
            addToCartObj.setProductPriceId(String.valueOf(productPriceVOArrayList.get(position).getId()));
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
        startActivity(new Intent(ServiceDetailActivity.this, MainActivity.class));
        finish();
    }
}
