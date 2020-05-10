package com.example.cyctrack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapTestActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, View.OnClickListener {

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private Button startButton;
    private MapboxMap map;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;

    private Location lastlocation;
    private Point originPosition;
    private Point destinationPosition;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActivity";
    private String TEST = "NAVI_TEST";
    private EditText edt_search;
    private Button btn_submit, btn_locateme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map_test);

        mapView = findViewById(R.id.mapView);
        startButton = findViewById(R.id.startbutton);
        edt_search = findViewById(R.id.edt_address);
        btn_submit = findViewById(R.id.btn_submit);
        btn_locateme = findViewById(R.id.btn_locateme);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        startButton.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_locateme.setOnClickListener(this);
    }


    private void show() {
        String destination = edt_search.getText().toString();
        Geocoder mGeocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> mResultLocation = mGeocoder.getFromLocationName(destination, 1);
            double latitude = mResultLocation.get(0).getLatitude();
            double longitude = mResultLocation.get(0).getLongitude();
            Log.d("Test", "latitude: " + latitude + "longitude: " + longitude);

            destinationPosition = Point.fromLngLat(longitude, latitude);
            originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());

            getRoute(originPosition, destinationPosition);
            Log.d("Test", "<==============>origin: " + originPosition + "destination<==============>: " + destinationPosition);
            startButton.setEnabled(true);

            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(15)
                    .build();

            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 2000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        Log.d(TEST, "onMapReady");
        map = mapboxMap;
        enableLocation();
    }

    private void enableLocation() {
        Log.d(TEST, "enableLocation");
        if (permissionsManager.areLocationPermissionsGranted(this)) {
            Log.d(TEST, "LocationPermission Ok");
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            Log.d(TEST, "LocationPermission NO");
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
        Log.d(TAG, "enable Location");
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        Log.d(TEST, "initializeLocationEngine");
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        if (locationEngine == null)
            Log.d(TEST, "LocationEngine is null");
        Location lastLocation = locationEngine.getLastLocation();

        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            Log.d(TEST, "lastLocation is null");
            locationEngine.addLocationEngineListener(this);
        }
        Log.d(TAG, "initialize location Engine");
    }


    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.COMPASS);
    }


    private void setCameraPosition(Location location) {
        Log.d(TAG, "settingcamera");
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 1000.0));
    }


    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin).destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(TAG, "No Routes Found,Check right user and access token");
                            return;
                        } else if (response.body().routes().size() == 0) {
                            Log.e(TAG, "No Route");
                            return;
                        }
                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error" + t.getMessage());
                    }
                });
    }

    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            Log.d(TAG, "on location changed");
            setCameraPosition(location);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit: {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                show();
                break;
            }
            case R.id.startbutton: {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .origin(originPosition)
                        .destination(destinationPosition)
                        .directionsProfile(DirectionsCriteria.PROFILE_CYCLING)
                        .shouldSimulateRoute(false)
                        .build();
                NavigationLauncher.startNavigation(MapTestActivity.this, options);
                break;
            }
            case R.id.btn_locateme: {
                Toast.makeText(this, "Testasdasasds", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
        }
        Log.d(TAG, "onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        Log.d(TAG, "onSavedInstance");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
        Log.d(TAG, "OnLowmemory");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy");
    }
}
