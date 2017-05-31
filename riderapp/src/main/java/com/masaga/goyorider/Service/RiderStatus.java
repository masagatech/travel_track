package com.masaga.goyorider.Service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.masaga.goyorider.forms.dashboard;

import static android.R.id.message;
import static com.masaga.goyorider.database.Tables.tbl_driver_info.name;


/**
 * Created by fajar on 31-May-17.
 */

public class RiderStatus extends Service implements LocationListener {


    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds
    public static LocationManager locationManager;
   public static Handler handler = new Handler();
    public  Context context=RiderStatus.this;
    public static String Rider_Lat,Rider_Long;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(new Runnable() {

            public void run() {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, RiderStatus.this);
                showCurrentLocation();
                handler.postDelayed(this, MINIMUM_TIME_BETWEEN_UPDATES);
            }
        }, MINIMUM_TIME_BETWEEN_UPDATES);


        return START_STICKY;

    }

    protected void showCurrentLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location == null){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
//            String message = String.format(
//                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );

            Rider_Lat= String.valueOf(location.getLatitude());
            Rider_Long= String.valueOf(location.getLongitude());

//            Toast.makeText(this, message,
//                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null){
//            String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude());
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Rider_Lat= String.valueOf(location.getLatitude());
           Rider_Long= String.valueOf(location.getLongitude());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        locationManager.removeUpdates(this);
        Toast.makeText(this, "Provider status changed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider enabled by the user. GPS turned on",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this,"Provider disabled by the user. GPS turned off",Toast.LENGTH_LONG).show();
//        settingsrequest();
    }





}
