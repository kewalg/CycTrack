package com.example.cyctrack;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.cyctrack.Model.Weather;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
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
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationEventListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapTestActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, View.OnClickListener, LocationListener, NavigationListener {

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private Button startButton;
    private MapboxMap map;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private NotificationManager mNotificationManager;
    private Location lastlocation;
    private Point originPosition;
    private Point destinationPosition;
    private NavigationMapRoute navigationMapRoute;
    private NavigationListener navigationListener;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "MainActivity";
    private String TEST = "NAVI_TEST";
    private EditText edt_search;
    private Button btn_submit, btn_home;
    private TextView tv_speedtest;
    private SwitchCompat tglbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map_test);

        mapView = findViewById(R.id.mapView);
        startButton = findViewById(R.id.startbutton);
        btn_home = findViewById(R.id.homeBtn);
        edt_search = findViewById(R.id.edt_address);
        tglbtn = findViewById(R.id.tglNew);
        tv_speedtest = findViewById(R.id.tv_speed_latest);
        btn_submit = findViewById(R.id.btn_submit);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        startButton.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_home.setOnClickListener(this);


        tglbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
                    Toast.makeText(MapTestActivity.this, "DND Enabled!", Toast.LENGTH_SHORT).show();
                } else {
                    changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
                    Toast.makeText(MapTestActivity.this, "DND Disabled!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //added because new access fine location policies, imported class..
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        //check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start the program if permission is granted
            doStuff();
        }


    }

    private void doStuff() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //commented, this is from the old version
            // this.onLocationChanged(null);
        }
        Toast.makeText(this, "Waiting for GPS connection!", Toast.LENGTH_SHORT).show();
    }

    protected void changeInterruptionFiler(int interruptionFilter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // If api level minimum 23
            if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                mNotificationManager.setInterruptionFilter(interruptionFilter);
            } else {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }

    private void show() {
        String destination = edt_search.getText().toString();

        /*Intent myIntent = new Intent(MapTestActivity.this, AddItem.class);
        myIntent.putExtra("destionation_key", destination);
        startActivity(myIntent);*/
        sharedPreferences = getSharedPreferences("Destination_key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Destination_key_value", destination);
        editor.apply();


        Geocoder mGeocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> mResultLocation = mGeocoder.getFromLocationName(destination, 1);
            double latitude = mResultLocation.get(0).getLatitude();
            double longitude = mResultLocation.get(0).getLongitude();

            Log.d("Address", "Destination Address: " + destination);


            destinationPosition = Point.fromLngLat(longitude, latitude);
            originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
            getRoute(originPosition, destinationPosition);


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

        MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                .accessToken(Mapbox.getAccessToken())
                .query(originPosition)
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();

        reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().features();
                if (results.size() > 0) {
                    String source_address_complete = response.body().features().get(0).placeName();
                    String source_address_short = response.body().features().get(0).text();


                    sharedPreferences = getSharedPreferences("Source_key", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Source_key_value", source_address_short);
                    editor.apply();


                    Point firstResultPoint = results.get(0).center();
                    //Log.d(TAG, "onResponse: " + firstResultPoint.coordinates());
                    Log.d(TAG, "onResponse: " + source_address_short);
                    Log.d(TAG, "onResponse: " + source_address_complete);
                } else {
                    Log.d(TAG, "onResponse: No result found");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
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
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }


    private void setCameraPosition(Location location) {
        Log.d(TAG, "settingcamera");
        //  map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 1000.0));
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
            //  setCameraPosition(location);
        }

        final MediaPlayer speedAlertPlayer = MediaPlayer.create(MapTestActivity.this, R.raw.speedalert);
        if (location == null) {
            tv_speedtest.setText("-.- km/h");
        } else {
            float nCurrentSpeed = location.getSpeed() * 3.6f;
            tv_speedtest.setText(String.format("%.2f", nCurrentSpeed) + " km/h");


            //Toast.makeText(this, "Current Speed is: " + (String.format("%.2f", nCurrentSpeed)), Toast.LENGTH_SHORT).show();


            if (nCurrentSpeed > 30.0) {
                speedAlertPlayer.start();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_red,
                        (ViewGroup) findViewById(R.id.toast_layout_red));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText((String.format("%.1f", nCurrentSpeed)));

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER | Gravity.RIGHT, 0, -110);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.toast_layout));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText((String.format("%.1f", nCurrentSpeed)));

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER | Gravity.RIGHT, 0, -110);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
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

            case R.id.homeBtn: {
                Intent i = new Intent(MapTestActivity.this, WeatherActivity.class);
                startActivity(i);
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
        doStuff();
    }


    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
       /* if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
       /* if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCancelNavigation() {
        Log.d("Text", "onCancelNavigation");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNavigationFinished() {
        // End the navigation session
        if (navigationListener != null) {
            navigationListener.onNavigationFinished();
            Log.d("oasihdlasdhasldhashdjlasd", "finishedfinishedfinishedfinishedfinishedfinishedfinishedfinished");
        }
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNavigationRunning() {
    }
}
