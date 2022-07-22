package com.busybees.lauk_kaing_expert_services.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.busybees.lauk_kaing_expert_services.data.models.PickerModel;
import com.busybees.lauk_kaing_expert_services.data.models.ProfileUpdateModel;
import com.busybees.lauk_kaing_expert_services.data.models.ProfileImageModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.ProfileImageObj;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.ProfileUpdateObj;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;

    public static final int REQUEST_IMAGE = 100;
    private static final String IMAGE_DIRECTORY = "/LKK";
    private Uri uri;
    private String uploadImage;

    private ImageView back, editIcon;
    private CircleImageView profile;
    private TextView profileName, update;
    private EditText phoneNumber, userName, userEmail;
    private ProgressBar progressBar;

    private UserVO userObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_profile);

        networkServiceProvider = new NetworkServiceProvider(this);

        userObj = Utility.query_UserProfile(this);

        back = findViewById(R.id.back_button);
        profile = findViewById(R.id.profile);
        profileName = findViewById(R.id.profileName);
        phoneNumber = findViewById(R.id.phone);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.email_et);
        update = findViewById(R.id.update_btn);
        progressBar = findViewById(R.id.materialLoader);
        editIcon = findViewById(R.id.edit_icon);

        if (userObj != null) {
            profileName.setText(userObj.getUsername());
            phoneNumber.setText(userObj.getPhone());
            userName.setText(userObj.getUsername());
            userEmail.setText(userObj.getEmail());
            loadProfile(userObj.getImage());
        }

        onClick();

    }

    private void loadProfile(String url) {

        if (url != null && !url.isEmpty() && !url.equals("null")) {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.loader_circle_shape);
            requestOptions.error(R.drawable.loader_circle_shape);

            Glide.with(this)
                    .load(url)
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            editIcon.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            editIcon.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(profile);

        }else{

            Glide.with(this)
                    .load(R.drawable.profile_default_image)
                    .into(profile);

        }
    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        profile.setOnClickListener(v -> showPictureViewDialog());

        editIcon.setOnClickListener( v -> Dexter.withActivity(ProfileActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());

        update.setOnClickListener(v -> {

            String phone = phoneNumber.getText().toString().trim();
            String name = userName.getText().toString().trim();

            if(phone.isEmpty()) {
                Utility.showToast(getApplicationContext(), getString(R.string.phone_blank));

            } else if(name.isEmpty()) {
                Utility.showToast(getApplicationContext(), getString(R.string.name_blank));

            } else {

                ProfileUpdateObj updateObj=new ProfileUpdateObj();
                updateObj.setPhone(phone);
                updateObj.setUsername(name);
                updateObj.setEmail(userEmail.getText().toString());

                CallProfileUpdate(updateObj);

            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showImagePickerOptions() {

        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        final View imagePickerDialogView = factory.inflate(R.layout.dialog_edit_profile_image, null);
        final androidx.appcompat.app.AlertDialog imagePicker = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this).create();
        imagePicker.setView(imagePickerDialogView);

        imagePicker.setCancelable(true);
        imagePicker.setCanceledOnTouchOutside(false);

        LinearLayout camera = imagePickerDialogView.findViewById(R.id.camera);
        LinearLayout gallery = imagePickerDialogView.findViewById(R.id.gallery);

        camera.setOnClickListener(v -> {
            launchCameraIntent();
            imagePicker.dismiss();
        });

        gallery.setOnClickListener(v -> {
            launchGalleryIntent();
            imagePicker.dismiss();
        });

        imagePicker.show();

    }

    private void showPictureViewDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
        pictureDialog.setTitle(this.getResources().getString(R.string.edit_image_dialog_title));

        String[] pictureDialogItems;

        if (userObj.getImage() != null) {
            pictureDialogItems = new String[] {
                    this.getResources().getString(R.string.edit_photo),
                    this.getResources().getString(R.string.viewphoto)};
        } else {
            pictureDialogItems = new String[] {
                    this.getResources().getString(R.string.edit_photo)};
        }

        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            EditProfile();
                            break;
                        case 1:
                            seeProfile();
                            break;

                    }
                });
        pictureDialog.show();
    }

    private void EditProfile(){
        if (userObj!=null){

            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private  void seeProfile(){

        if (userObj.getImage() != null){
            String url = userObj.getImage();
            Intent intent=new Intent(ProfileActivity.this, ImageViewActivity.class);
            intent.putExtra("image_url", url);
            startActivity(intent);
        } else {

        }

    }

    private void CallProfileUpdate(ProfileUpdateObj profileUpdateObj) {

        if (Utility.isOnline(this)){

            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.ProfileUpdateCall(ApiConstants.BASE_URL + ApiConstants.GET_EDIT_PROFILE, profileUpdateObj).enqueue(new Callback<ProfileUpdateModel>() {
                @Override
                public void onResponse(Call<ProfileUpdateModel> call, Response<ProfileUpdateModel> response) {

                    if (response.body().getError()==false){
                        progressBar.setVisibility(View.GONE);

                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                        Utility.delete_UserProfile(ProfileActivity.this);
                        profileName.setText(response.body().getData().getUsername());
                        userName.setText(response.body().getData().getUsername());
                        userEmail.setText(response.body().getData().getEmail());
                        Utility.showToast(ProfileActivity.this, response.body().getMessage());
                        Utility.Save_UserProfile(ProfileActivity.this, response.body().getData());

                    }else {

                        Utility.showToast(ProfileActivity.this,response.body().getMessage());
                    }

                }
                @Override
                public void onFailure(Call<ProfileUpdateModel> call, Throwable t) {
                    Utility.showToast(ProfileActivity.this, t.getMessage());

                }
            });
        }else {
            Utility.showToast(ProfileActivity.this, getString(R.string.no_internet));

        }
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    UploadImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void UploadImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String convertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        ProfileImageObj profileImageObj=new ProfileImageObj();
        profileImageObj.setImage(convertImage);
        profileImageObj.setPhone(userObj.getPhone());
        CallSaveImage(profileImageObj);

    }

    public void CallSaveImage(ProfileImageObj profileImageObj) {
        if (Utility.isOnline(this)){

            editIcon.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.ProfileImageCall(ApiConstants.BASE_URL + ApiConstants.GET_SAVE_USER_IMAGE, profileImageObj).enqueue(new Callback<ProfileImageModel>() {
                @Override
                public void onResponse(Call<ProfileImageModel> call, Response<ProfileImageModel> response) {

                    if (response.body().getError()==false){
                        progressBar.setVisibility(View.GONE);
                        editIcon.setVisibility(View.VISIBLE);

                        Utility.Save_UserProfile(ProfileActivity.this,response.body().getData());

                        Utility.showToast(ProfileActivity.this,response.body().getMessage());

                        loadProfile(uri.toString());
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(ProfileActivity.this,response.body().getMessage());
                    }

                }
                @Override
                public void onFailure(Call<ProfileImageModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    editIcon.setVisibility(View.VISIBLE);
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });
        }else {
            Utility.showToast(ProfileActivity.this, getString(R.string.no_internet));
        }
    }

    @Subscribe
    public void getEventBusPicker(PickerModel pickerModel){
        if (pickerModel.getPicker()==0){

            launchCameraIntent();

        }else if (pickerModel.getPicker()==1){

            launchGalleryIntent();

        }
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void launchGalleryIntent() {
        Intent intent = new Intent(ProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
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
}
