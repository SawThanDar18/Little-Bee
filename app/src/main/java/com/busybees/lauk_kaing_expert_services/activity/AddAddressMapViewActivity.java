package com.busybees.lauk_kaing_expert_services.activity;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.busybees.data.vos.Address.LatLongObj;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
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
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

public class AddAddressMapViewActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    private static final String RED_MARKER_ICON_ID = "RED_MARKER_ICON_ID";
    private static final String COMPASS_ICON_ID = "COMPASS_ICON_ID";

    private ProgressBar mapLoader;

    private FloatingActionButton myCurrentLocationBtn;
    private Button confirmLocationBtn;

    private MapView mapView;
    private MapboxMap mapboxMap;

    private PermissionsManager permissionsManager;
    private Layer droppedMarkerLayer;
    private double add_address_latitude, add_address_longitude, current_Lat, current_Lng;
    private LatLng add_address_LatLng, current_LatLng;
    private String add_address_location_name;

    private ImageView hoveringMarker;
    private String locationName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2F3LXRoYW5kYXIiLCJhIjoiY2w1a3Fzc3A4MGNvYjNqbXBic2FzMWMydiJ9.wRp6U3_8-i1pIJTNfRi1Mw");

        setContentView(R.layout.activity_add_address_map_view);

        mapLoader = findViewById(R.id.map_loader);
        myCurrentLocationBtn = findViewById(R.id.myCurrentLocation_btn);
        confirmLocationBtn = findViewById(R.id.confirm_location_button);
        mapView = findViewById(R.id.map_view);

        mapView.getMapAsync(this);

        if (getIntent() != null) {
            add_address_latitude = getIntent().getDoubleExtra("add_address_latitude", 0);
            add_address_longitude = getIntent().getDoubleExtra("add_address_longitude", 0);
            add_address_LatLng = new LatLng(add_address_latitude, add_address_longitude);
            add_address_location_name = getIntent().getStringExtra("add_address_locationName");

            current_Lat = getIntent().getDoubleExtra("currentLat", 0);
            current_Lng = getIntent().getDoubleExtra("currentLng", 0);
            current_LatLng = new LatLng(current_Lat, current_Lng);

        }


        onClick();

    }

    private void onClick() {
        myCurrentLocationBtn.setOnClickListener(v -> {

            if (current_Lat != 0.0 && current_Lng != 0.0) {
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newLatLngZoom(new LatLng(current_LatLng), 15));
            } else {
                if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
                    mapboxMap.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 15));
                }
            }
        });
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Utility.showToast(getApplicationContext(), getString(R.string.user_location_permission_explanation));
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {

            mapLoader.setVisibility(View.VISIBLE);

            mapboxMap.getStyle(style -> {
                mapLoader.setVisibility(View.GONE);
                enableLocationComponent(style);
            });
        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.user_location_permission_not_granted));
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        mapLoader.setVisibility(View.VISIBLE);

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            mapLoader.setVisibility(View.GONE);

            if (add_address_latitude != 0.0 && add_address_longitude != 0.0) {
                moveCamera(add_address_LatLng, loadedMapStyle, mapboxMap);
            } else {

                LocationComponent locationComponent = mapboxMap.getLocationComponent();

                locationComponent.activateLocationComponent(
                        LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

                locationComponent.setLocationComponentEnabled(true);

                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.COMPASS);

            }
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
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

        mapboxMap.setStyle(Style.OUTDOORS, style -> {

            enableLocationComponent(style);

            hoveringMarker = new ImageView(getApplicationContext());
            hoveringMarker.setImageResource(R.drawable.theme_marker);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            hoveringMarker.setLayoutParams(params);
            mapView.addView(hoveringMarker);

            confirmLocationBtn.setOnClickListener(v -> {
                finish();

                final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;

                if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                    GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
                    if (source != null) {
                        source.setGeoJson(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                    }
                    droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                    if (droppedMarkerLayer != null) {
                        droppedMarkerLayer.setProperties(visibility(Property.VISIBLE));
                    }
                }

                getCompleteAddressString(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));

                LatLongObj obj = new LatLongObj();
                obj.setLatitude(mapTargetLatLng.getLatitude());
                obj.setLongitude(mapTargetLatLng.getLongitude());

                Intent putIntent = new Intent(getApplicationContext(), AddNewAddressActivity.class);
                putIntent.putExtra("add_address_latitude", mapTargetLatLng.getLatitude());
                putIntent.putExtra("add_address_longitude", mapTargetLatLng.getLongitude());
                putIntent.putExtra("add_address_locationName", locationName);
                startActivity(putIntent);
                EventBus.getDefault().postSticky(obj);
            });
        });
    }

    @SuppressLint("LongLogTag")
    private void getCompleteAddressString(final Point point) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(point.latitude(), point.longitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                locationName = strReturnedAddress.toString();

            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location address", "Cannot get Address!");
        }
    }

    private void moveCamera(LatLng latLng, Style style, MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

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
}
