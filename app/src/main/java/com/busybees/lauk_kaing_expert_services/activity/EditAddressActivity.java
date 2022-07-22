package com.busybees.lauk_kaing_expert_services.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.data.models.AddressModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Address.AddressVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Address.EditAddressObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
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

public class EditAddressActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    private ImageView back;

    private Spinner type;
    private EditText phoneNumber;
    private EditText address;

    private ProgressBar progress_loader, progressBar;
    private ConstraintLayout mapLayout, goMapBtn;
    private LinearLayout editAddressConfirmBtn;

    private MapView mapView;
    private MapboxMap mapboxMap;

    private PermissionsManager permissionsManager;
    private double userLat, userLng, latitude, longitude;
    private String locationName, location_name;
    private LatLng userLatLng;

    private static final String RED_MARKER_ICON_ID = "RED_MARKER_ICON_ID";
    private static final String COMPASS_ICON_ID = "COMPASS_ICON_ID";

    private int position;
    private AddressVO obj = new AddressVO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2F3LXRoYW5kYXIiLCJhIjoiY2w1a3Fzc3A4MGNvYjNqbXBic2FzMWMydiJ9.wRp6U3_8-i1pIJTNfRi1Mw");

        setContentView(R.layout.activity_edit_address);

        networkServiceProvider = new NetworkServiceProvider(this);
        userVO = Utility.query_UserProfile(this);

        back = findViewById(R.id.back_button);
        mapView = findViewById(R.id.map_view_edit);
        progress_loader = findViewById(R.id.loader);
        progressBar = findViewById(R.id.materialLoader);
        mapLayout = findViewById(R.id.map_layout);
        goMapBtn = findViewById(R.id.goMap_btn);
        editAddressConfirmBtn = findViewById(R.id.continue_layout);
        type = findViewById(R.id.type);
        phoneNumber = findViewById(R.id.phone);
        address = findViewById(R.id.flat);

        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 1);
            obj = (AddressVO) getIntent().getSerializableExtra("obj");

            if (getIntent().getDoubleExtra("latitude", 0.0) != 0.0) {
                userLat = getIntent().getDoubleExtra("latitude", 0.0);
                userLng = getIntent().getDoubleExtra("longitude", 0.0);
                locationName = getIntent().getStringExtra("locationName");
            } else {
                userLat = Double.parseDouble(obj.getLatitude());
                userLng = Double.parseDouble(obj.getLongitude());
                Log.e("lat&lng", userLat + "," + userLng);
                locationName = obj.getLocationName();
            }
            userLatLng = new LatLng(userLat, userLng);

        }

        onClick();
        setAddressData();

        mapView.getMapAsync(this);
    }

    public void onClick() {

        back.setOnClickListener(v -> finish());

        editAddressConfirmBtn.setOnClickListener(v -> {

            if(!address.getText().toString().trim().isEmpty() && !phoneNumber.getText().toString().trim().isEmpty()) {

                if (userVO != null){
                    EditAddressObject editAddressObject = new EditAddressObject();
                    editAddressObject.setAddressId(String.valueOf(obj.getId()));
                    editAddressObject.setPhone(phoneNumber.getText().toString());
                    editAddressObject.setAddress(address.getText().toString());
                    editAddressObject.setType(type.getSelectedItem().toString());
                    editAddressObject.setLatitude(Double.toString(latitude));
                    editAddressObject.setLongitude(Double.toString(longitude));
                    editAddressObject.setLocationName(location_name);
                    CallEditAddress(editAddressObject);

                }
            } else {
                Utility.showToast(getApplicationContext(),getString(R.string.fill_all_detail));
            }

        });
    }

    public void CallEditAddress(EditAddressObject obj) {

        if (Utility.isOnline(this)) {

            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.EditAddressCall(ApiConstants.BASE_URL + ApiConstants.GET_EDIT_ADDRESS, obj).enqueue(new Callback<AddressModel>() {
                @Override
                public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {

                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError() == false) {

                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (response.body().getError() == true) {

                        Utility.showToast(getApplicationContext(), response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(Call<AddressModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });
        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    public void setAddressData() {
        if (obj != null) {

            phoneNumber.setText(obj.getPhone());
            address.setText(obj.getAddress());
            type.setSelection(getTypePosition(obj.getType()));

            if (position == 0) {
                phoneNumber.setTextColor(getResources().getColor(R.color.home_service_name_color));
                phoneNumber.setEnabled(false);
            } else {
                phoneNumber.setEnabled(true);

            }
        }
    }

    private int getTypePosition(String type) {

        String[] typeArreng = this.getResources().getStringArray(R.array.items);

        int j = 0;
        for (int i = 0; i < typeArreng.length; i++) {
            if (type != null && type.equalsIgnoreCase(typeArreng[i])) {
                j = i;
                break;
            }
        }
        return j;

    }


    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            progress_loader.setVisibility(View.VISIBLE);
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    progress_loader.setVisibility(View.GONE);
                    enableLocationComponent(style);
                }
            });
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null)));
            Utility.showToast(getApplicationContext(), getString(R.string.user_location_permission_not_granted));
            finish();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        progress_loader.setVisibility(View.VISIBLE);

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            progress_loader.setVisibility(View.GONE);
            mapLayout.setVisibility(View.VISIBLE);

            mapboxMap.getUiSettings().setAllGesturesEnabled(false);

            if (userLat != 0.0 && userLng != 0.0) {

                latitude = userLat;
                longitude = userLng;
                location_name = locationName;

                moveCamera(userLatLng, loadedMapStyle, mapboxMap);

                goMapBtn.setOnClickListener(v -> {

                    finish();
                    Intent intent = new Intent(getApplicationContext(), MapBoxActivity.class);
                    intent.putExtra("key", 1);
                    intent.putExtra("position", position);
                    intent.putExtra("obj", obj);
                    startActivity(intent);
                });

            } else {
                mapLayout.setVisibility(View.GONE);

                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
                locationComponent.setLocationComponentEnabled(true);

                locationComponent.getLocationEngine().getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(LocationEngineResult result) {
                        if (result.getLastLocation() != null) {
                            Log.e("current>>", String.valueOf(result.getLastLocation()));

                            mapLayout.setVisibility(View.VISIBLE);

                            latitude = result.getLastLocation().getLatitude();
                            longitude = result.getLastLocation().getLongitude();

                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null) {
                                    Address returnedAddress = addresses.get(0);
                                    StringBuilder strReturnedAddress = new StringBuilder("");

                                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                                    }
                                    location_name = strReturnedAddress.toString();

                                } else {
                                    Log.w("My Current loction address", "No Address returned!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.w("My Current location address", "Cannot get Address!");
                            }

                            goMapBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MapBoxActivity.class);
                                    intent.putExtra("key", 1);
                                    intent.putExtra("position", position);
                                    intent.putExtra("obj", obj);
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
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.gps_alert))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.gps_enable_yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.gps_enable_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void moveCamera(LatLng latLng, Style style, MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.getUiSettings().setAllGesturesEnabled(false);
        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);

        style.addImage(RED_MARKER_ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(com.mapbox.mapboxsdk.R.drawable.mapbox_marker_icon_default)));

        final SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setIconIgnorePlacement(true);

        final Symbol symbolStart = symbolManager.create(new SymbolOptions()
                .withLatLng(latLng)
                .withIconImage(RED_MARKER_ICON_ID)
                .withSymbolSortKey(5.0f)
                .withIconSize(1.5f));

        mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                LatLng latlng = mapboxMap.getCameraPosition().target;

                symbolStart.setIconImage(COMPASS_ICON_ID);
                symbolStart.setLatLng(latlng);

                symbolManager.update(symbolStart);
            }
        });
        mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                symbolStart.setIconImage(RED_MARKER_ICON_ID);
                symbolManager.update(symbolStart);

            }
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
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });
    }

    @SuppressWarnings("MissingPermission")
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
