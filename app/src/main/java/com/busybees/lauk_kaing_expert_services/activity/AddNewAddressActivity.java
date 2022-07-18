package com.busybees.lauk_kaing_expert_services.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.busybees.data.models.AddAddressModel;
import com.busybees.data.vos.Address.AddAddresssObject;
import com.busybees.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewAddressActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    private ImageView back;

    private Spinner type;
    private EditText phoneNumber;
    private EditText address;

    private ProgressBar progress_loader, progressBar;
    private ConstraintLayout mapLayout, goMapBtn;
    private LinearLayout addNewAddressConfirmBtn;

    private MapView mapView;
    private MapboxMap mapboxMap;

    private PermissionsManager permissionsManager;
    private double add_address_latitude, add_address_longitude, current_latitude, current_longitude, latitude, longitude;
    private LatLng add_address_LatLng;
    private String add_address_location_name, location_name;

    private static final String RED_MARKER_ICON_ID = "RED_MARKER_ICON_ID";
    private static final String COMPASS_ICON_ID = "COMPASS_ICON_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2F3LXRoYW5kYXIiLCJhIjoiY2w1a3Fzc3A4MGNvYjNqbXBic2FzMWMydiJ9.wRp6U3_8-i1pIJTNfRi1Mw");

        setContentView(R.layout.activity_add_new_address);

        networkServiceProvider = new NetworkServiceProvider(this);
        userVO = Utility.query_UserProfile(this);

        back = findViewById(R.id.back_button);
        mapView = findViewById(R.id.map_view_add);
        progress_loader = findViewById(R.id.loader);
        progressBar = findViewById(R.id.materialLoader);
        mapLayout = findViewById(R.id.map_layout);
        goMapBtn = findViewById(R.id.goMap_btn);
        addNewAddressConfirmBtn = findViewById(R.id.continue_layout);
        type = findViewById(R.id.type);
        phoneNumber = findViewById(R.id.phone);
        address = findViewById(R.id.flat);

        if (getIntent() != null) {
            add_address_latitude = getIntent().getDoubleExtra("add_address_latitude", 0);
            add_address_longitude = getIntent().getDoubleExtra("add_address_longitude", 0);
            add_address_LatLng = new LatLng(add_address_latitude, add_address_longitude);
            add_address_location_name = getIntent().getStringExtra("add_address_locationName");
        }

        mapView.getMapAsync(this);

        onClick();
    }

    private void onClick() {
        back.setOnClickListener(v -> finish());

        addNewAddressConfirmBtn.setOnClickListener(v -> {

            if(!address.getText().toString().trim().isEmpty() && !phoneNumber.getText().toString().trim().isEmpty()) {

                if (userVO != null){
                    AddAddresssObject addAddressObject = new AddAddresssObject();
                    addAddressObject.setPhone(phoneNumber.getText().toString());
                    addAddressObject.setUserId(String.valueOf(userVO.getId()));
                    addAddressObject.setAddress(address.getText().toString());
                    addAddressObject.setType(type.getSelectedItem().toString());
                    addAddressObject.setLatitude(Double.toString(latitude));
                    addAddressObject.setLongitude(Double.toString(longitude));
                    addAddressObject.setLocationName(location_name);
                    CallAddAddress(addAddressObject);

                }
            } else {
                Utility.showToast(getApplicationContext(),getString(R.string.fill_all_detail));
            }

        });
    }

    public void CallAddAddress(AddAddresssObject obj) {

        if (Utility.isOnline(this)){

            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.AddAddressCall(ApiConstants.BASE_URL + ApiConstants.GET_ADD_ADDRESS, obj).enqueue(new Callback<AddAddressModel>() {
                @Override
                public void onResponse(Call<AddAddressModel> call, Response<AddAddressModel> response) {

                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError()==false){

                        Utility.showToast(getApplicationContext(),response.body().getMessage());
                        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                        startActivity(intent);
                        finish();

                    }else if (response.body().getError()==true){

                        Utility.showToast(getApplicationContext(),response.body().getMessage());

                    }
                }
                @Override
                public void onFailure(Call<AddAddressModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        }else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        progress_loader.setVisibility(View.VISIBLE);

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            progress_loader.setVisibility(View.GONE);
            mapboxMap.getUiSettings().setAllGesturesEnabled(false);

            mapLayout.setVisibility(View.VISIBLE);

            if (add_address_latitude != 0.0 && add_address_longitude != 0.0) {

                moveCamera(add_address_LatLng, loadedMapStyle, mapboxMap);

                latitude = add_address_latitude;
                longitude = add_address_longitude;
                location_name = add_address_location_name;

                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(AddNewAddressActivity.this, mapboxMap.getStyle()).build());
                locationComponent.setLocationComponentEnabled(true);

                locationComponent.getLocationEngine().getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
                    @Override
                    public void onSuccess(LocationEngineResult result) {
                        if (result.getLastLocation() != null) {

                            current_latitude = result.getLastLocation().getLatitude();
                            current_longitude = result.getLastLocation().getLongitude();

                            goMapBtn.setOnClickListener(v -> {
                                finish();
                                Intent intent = new Intent(getApplicationContext(), AddAddressMapViewActivity.class);
                                intent.putExtra("currentLat", current_latitude);
                                intent.putExtra("currentLng", current_longitude);
                                intent.putExtra("add_address_latitude", add_address_latitude);
                                intent.putExtra("add_address_longitude", add_address_longitude);
                                startActivity(intent);
                            });

                        } else {
                            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                buildAlertMessageNoGps();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

            } else {
                mapLayout.setVisibility(View.GONE);

                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(AddNewAddressActivity.this, mapboxMap.getStyle()).build());
                locationComponent.setLocationComponentEnabled(true);

                locationComponent.getLocationEngine().getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(LocationEngineResult result) {
                        if (result.getLastLocation() != null) {

                            mapLayout.setVisibility(View.VISIBLE);

                            current_latitude = result.getLastLocation().getLatitude();
                            current_longitude = result.getLastLocation().getLongitude();

                            latitude = current_latitude;
                            longitude = current_longitude;

                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(current_latitude, current_longitude, 1);
                                if (addresses != null) {
                                    Address returnedAddress = addresses.get(0);
                                    StringBuilder strReturnedAddress = new StringBuilder("");

                                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                                    }
                                    location_name = strReturnedAddress.toString();

                                } else {
                                    Log.w("My Current location address", "No Address returned!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.w("My Current location address", "Cannot get Address!");
                            }

                            goMapBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), AddAddressMapViewActivity.class);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                buildAlertMessageNoGps();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.COMPASS);

            }
        } else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void moveCamera(LatLng latLng, Style style, MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.getUiSettings().setAllGesturesEnabled(false);
        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);

        style.addImage(RED_MARKER_ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(com.mapbox.mapboxsdk.places.R.drawable.mapbox_marker_icon_default)));

        final SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setIconIgnorePlacement(true);

        final Symbol symbolStart = symbolManager.create(new SymbolOptions()
                .withLatLng(latLng)
                .withIconImage(RED_MARKER_ICON_ID)
                .withSymbolSortKey(5.0f)
                .withIconSize(1.5f));

        mapboxMap.addOnCameraMoveListener(() -> {

            LatLng latlng = mapboxMap.getCameraPosition().target;

            symbolStart.setIconImage(COMPASS_ICON_ID);
            symbolStart.setLatLng(latlng);

            symbolManager.update(symbolStart);
        });
        mapboxMap.addOnCameraIdleListener(() -> {

            symbolStart.setIconImage(RED_MARKER_ICON_ID);
            symbolManager.update(symbolStart);

        });

        CircleManager circleManager = new CircleManager(mapView, mapboxMap, style);
        CircleOptions circleOptions = new CircleOptions()
                .withLatLng(latLng)
                .withCircleColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                .withCircleRadius(5f)
                .withDraggable(true);
        circleManager.create(circleOptions);

        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .tilt(10)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 6000);
    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.gps_alert))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.gps_enable_yes), (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(getString(R.string.gps_enable_no), (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            progress_loader.setVisibility(View.VISIBLE);
            mapboxMap.getStyle(style -> {
                progress_loader.setVisibility(View.GONE);
                enableLocationComponent(style);
            });
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", getPackageName(), null)));
            Utility.showToast(getApplicationContext(), getString(R.string.user_location_permission_not_granted));
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);

        checkData(mapboxMap);
    }

    private void checkData(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        progress_loader.setVisibility(View.VISIBLE);
        mapLayout.setVisibility(View.GONE);

        mapboxMap.setStyle(Style.OUTDOORS, style -> enableLocationComponent(style));
    }
}
