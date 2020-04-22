package com.example.cyctrack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SpeedOMeter extends AppCompatActivity implements LocationListener {

    int MY_PERMISSION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_ometer);


        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SpeedOMeter.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
            }, MY_PERMISSION);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.onLocationChanged(null);
        }
    }

    @Override
    public void onLocationChanged (Location location){
        TextView txt = (TextView) this.findViewById(R.id.textView);

        if (location == null) {
            txt.setText("-.- m/s");

        } else {
            float nCurrentSpeed = location.getSpeed();
            txt.setText(nCurrentSpeed + "m/s");

        }

    }

    @Override
    public void onStatusChanged (String provider,int status, Bundle extras){

    }

    @Override
    public void onProviderEnabled (String provider){

    }

    @Override
    public void onProviderDisabled (String provider){

    }
}
