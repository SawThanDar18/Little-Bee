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

import com.busybees.data.vos.Address.AddressVO;
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

public class MapBoxActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    public static final String GAME_PREFERENCES = null;
    private static final String RED_MARKER_ICON_ID = "RED_MARKER_ICON_ID";
    private static final String COMPASS_ICON_ID = "COMPASS_ICON_ID";

    private MapView mapView;
    private FloatingActionButton myCurrentLocation_btn;
    private Button select_location_button;
    private Button confirm_location_button;
    private ImageView hoveringMarker;
    private String locationName;
    private double userLat, userLng;
    private LatLng userLatLng;
    private ProgressBar map_loader;

    AddressVO addressLists = new AddressVO();

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Layer droppedMarkerLayer;
    private int key, position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2F3LXRoYW5kYXIiLCJhIjoiY2w1a3Fzc3A4MGNvYjNqbXBic2FzMWMydiJ9.wRp6U3_8-i1pIJTNfRi1Mw");

        setContentView(R.layout.activity_map_box);

        mapView = findViewById(R.id.map_view);
        myCurrentLocation_btn = findViewById(R.id.myCurrentLocation_btn);
        confirm_location_button = findViewById(R.id.confirm_location_button);
        map_loader = findViewById(R.id.map_loader);

        mapView.getMapAsync(this);

        myCurrentLocation_btn.setOnClickListener(v -> {
            enableLocationComponent(mapboxMap.getStyle());
        });

        Intent intent = getIntent();
        if (intent != null) {
            key = intent.getIntExtra("key", 0);
            position = intent.getIntExtra("position", 0);
            addressLists = (AddressVO) intent.getSerializableExtra("obj");
            userLat = Double.parseDouble(addressLists.getLatitude());
            userLng = Double.parseDouble(addressLists.getLongitude());
            userLatLng = new LatLng(userLat, userLng);
            locationName = addressLists.getLocationName();
        }
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);

        mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                if (userLat != 0.0 && userLng != 0.0) {
                    moveCamera(userLatLng, style, mapboxMap);
                } else {
                    enableLocationComponent(style);
                    hoveringMarker = new ImageView(getApplicationContext());
                    hoveringMarker.setImageResource(R.drawable.theme_marker);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                    hoveringMarker.setLayoutParams(params);
                    mapView.addView(hoveringMarker);
                }

                confirm_location_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                        Intent putIntent = new Intent(getApplicationContext(), EditAddressActivity.class);

                        addressLists.setLatitude(String.valueOf(mapTargetLatLng.getLatitude()));
                        addressLists.setLongitude(String.valueOf(mapTargetLatLng.getLongitude()));
                        addressLists.setLocationName(locationName);
                        putIntent.putExtra("obj", addressLists);
                        putIntent.putExtra("position", position);
                        startActivity(putIntent);
                        EventBus.getDefault().postSticky(obj);
                        finish();
                    }
                });
            }
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

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        map_loader.setVisibility(View.VISIBLE);

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            map_loader.setVisibility(View.GONE);

            if (userLat != 0.0 && userLng != 0.0) {
                moveCamera(userLatLng, loadedMapStyle, mapboxMap);
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
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Utility.showToast(getApplicationContext(), getString(R.string.user_location_permission_explanation));
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {
            map_loader.setVisibility(View.VISIBLE);
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {

                    map_loader.setVisibility(View.GONE);
                    enableLocationComponent(style);
                }
            });
        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.user_location_permission_not_granted));
        }
    }
}

/* private void reverseGeocode(final Point point) {

        MapboxGeocoding client = MapboxGeocoding.builder()
                .accessToken("pk.eyJ1Ijoic2F3LXRoYW5kYXIiLCJhIjoiY2t3Y2drN29iMGFhYTJ1dDN3bnM3YnN2NCJ9.745ozxcJanfhUvneFX_4dQ")
                .query(Point.fromLngLat(point.longitude(), point.latitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();

        client.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                if (response.body() != null) {
                    List<CarmenFeature> results = response.body().features();
                    if (results.size() > 0) {
                        CarmenFeature feature = results.get(0);
                        String address = feature.placeName();

                        mapboxMap.getStyle(new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                    Utility.showToast(getApplicationContext(),
                                            address, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Utility.showToast(getApplicationContext(),
                                getString(R.string.location_picker_dropped_marker_snippet_no_results), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                Timber.e("Geocoding Failure: %s", throwable.getMessage());
            }
        });
    }*/
 /* mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                            Utility.showToast(getApplicationContext(),
                                    strAdd, Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
/*Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap mBitmap = Bitmap.createBitmap(300, 550, conf);

        Paint color = new Paint();
        color.setColor(Color.BLUE);

        Canvas canvas = new Canvas(mBitmap);
        canvas.drawBitmap(BitmapUtils.getBitmapFromDrawable(drawable), 0,0, color);*/