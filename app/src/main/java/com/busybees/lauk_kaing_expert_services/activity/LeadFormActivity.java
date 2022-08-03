package com.busybees.lauk_kaing_expert_services.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.Dialog.DialogLimitPhoto;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.PhotoItemViewAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.network.RetrofitFactory;
import com.busybees.lauk_kaing_expert_services.network.sync.AddToCartSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetCartSync;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.karumi.dexter.listener.PermissionGrantedResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LeadFormActivity extends AppCompatActivity {

    private EditText title_et, budget_et, first_square_feet_et, second_square_feet_et, detail_et;
    private RecyclerView photo_recyclerView;
    private Button continue_btn;
    private ImageButton upload_photo_btn;
    private ProgressBar progressBar;
    private String phone = "";
    private int key, product_price_id;
    TextView toolbar;
    ImageView back;
    File file;

    List<File> home_photos = new ArrayList<File>();

    private PhotoItemViewAdapter photoItemViewAdapter;

    NetworkServiceProvider serviceProvider;
    UserVO userVO = new UserVO();
    ProductsCarryObject pStepCarryObj = new ProductsCarryObject();

    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.lead_form_activity);

        title_et = findViewById(R.id.title_et);
        budget_et = findViewById(R.id.budget_et);
        first_square_feet_et = findViewById(R.id.first_square_feet_et);
        second_square_feet_et = findViewById(R.id.second_square_feet_et);
        detail_et = findViewById(R.id.detail_et);
        photo_recyclerView = findViewById(R.id.upload_photo_recyclerView);
        upload_photo_btn = findViewById(R.id.upload_photo_btn);
        continue_btn = findViewById(R.id.continue_btn);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar_common);
        back = findViewById(R.id.back_button);

        photoItemViewAdapter = new PhotoItemViewAdapter(this);

        serviceProvider = new NetworkServiceProvider(this);
        userVO = Utility.query_UserProfile(this);

        if (getIntent() != null) {

            key = getIntent().getIntExtra("key", 0);
            position = getIntent().getIntExtra("position", 0);
            phone = getIntent().getStringExtra("phone");
            product_price_id = getIntent().getIntExtra("product_price_id", 0);
            pStepCarryObj = (ProductsCarryObject) getIntent().getSerializableExtra("product_data");

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photo_recyclerView.setLayoutManager(layoutManager);
        photo_recyclerView.setAdapter(photoItemViewAdapter);

        onClick();
    }

    private void onClick() {
        upload_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title_et.getText().toString().equalsIgnoreCase("") || title_et.getText().toString().startsWith("0") ) {
                    title_et.setError(getString(R.string.title_et_error));
                } else if (budget_et.getText().toString().equalsIgnoreCase("") || budget_et.getText().toString().startsWith("0")) {
                    budget_et.setError(getString(R.string.budget_et_error));
                }  else if (first_square_feet_et.getText().toString().equalsIgnoreCase("") || first_square_feet_et.getText().toString().startsWith("0")) {
                    first_square_feet_et.setError(getString(R.string.first_square_et_error));
                } else if (second_square_feet_et.getText().toString().equalsIgnoreCase("") || second_square_feet_et.getText().toString().startsWith("0")) {
                    second_square_feet_et.setError(getString(R.string.second_square_feet_et_error));
                } else if (home_photos.size() == 0) {
                    Utility.showToast(getApplicationContext(), getString(R.string.home_photos_error));
                } else {
                    CallAddToCart();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void CallAddToCart() {
        if (Utility.isOnline(this)) {
            progressBar.setVisibility(View.VISIBLE);

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("phone", phone);
            builder.addFormDataPart("title", title_et.getText().toString());
            builder.addFormDataPart("budget", budget_et.getText().toString());
            builder.addFormDataPart("square_feet", first_square_feet_et.getText().toString() + "x" + second_square_feet_et.getText().toString());
            builder.addFormDataPart("detail_text", detail_et.getText().toString());
            builder.addFormDataPart("quantity", "1");
            builder.addFormDataPart("product_price_id", String.valueOf(product_price_id));
            builder.addFormDataPart("form_status", String.valueOf(2));

            for (int i = 0; i < home_photos.size(); i++) {
                builder.addFormDataPart("photos[]", home_photos.get(i).getName(), RequestBody.create(MediaType.parse("multipart/form-data"), home_photos.get(i)));
            }

            MultipartBody requestBody = builder.build();
            RetrofitFactory factory = new RetrofitFactory();
            Retrofit retrofit = factory.connector();
            AddToCartSync sync = retrofit.create(AddToCartSync.class);
            Call<AddToCartModel> call = sync.uploadMultiFile(ApiConstants.BASE_URL + ApiConstants.GET_ADD_TO_CART, requestBody);
            call.enqueue(new Callback<AddToCartModel>() {
                @Override
                public void onResponse(Call<AddToCartModel> call, Response<AddToCartModel> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError() == false) {
                        finish();

                            Intent intent = new Intent(getApplicationContext(), ServiceDetailActivity.class);
                            intent.putExtra("product_data", pStepCarryObj);
                            startActivity(intent);


                    } else if (response.body().getError() == true) {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }

                }

                @Override
                public void onFailure(Call<AddToCartModel> call, Throwable t) {
                    finish();
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    private void requestPermission() {
        PermissionListener permissionListener = new PermissionListener() {


            @Override
            public void onPermissionGranted() {
                selectImagesFromGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(this).setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject the permission, you cannot use this service.\n\nPlease Turn on permission at [Setting] > [Permission].")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    private void selectImagesFromGallery() {
        TedBottomPicker.with(this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showCameraTile(true)
                .showGalleryTile(true)
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        if (uriList != null && !uriList.isEmpty()) {
                            if (uriList.size() > 5) {
                                uriList.clear();
                                DialogLimitPhoto dialogLimitPhoto = new DialogLimitPhoto(getString(R.string.photo_limit_txt));
                                dialogLimitPhoto.show(getSupportFragmentManager(), "");
                            } else {
                                photoItemViewAdapter.setData(uriList);
                            }
                            Log.e("photoSize", String.valueOf(uriList.size()));
                            for (int i = 0; i < uriList.size(); i++) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriList.get(i));
                                    home_photos.add(SaveHomePhoto(bitmap, uriList));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });
    }

    public File SaveHomePhoto(Bitmap myBitmap, List<Uri> uriList) {
        String IMAGE_DIRECTORY = "/LKK";

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File wallpaperDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            for (int i = 0; i < uriList.size(); i++) {
                file = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                //fo.write(imgBytesData);
                MediaScannerConnection.scanFile(this,
                        new String[]{uriList.get(i).getPath()},
                        new String[]{"image/jpeg"}, null);
                fo.close();

                return file;

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new File("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
