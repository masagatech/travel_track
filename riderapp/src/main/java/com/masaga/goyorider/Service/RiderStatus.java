package com.masaga.goyorider.Service;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.forms.dashboard;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.utils.SHP;

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
    public Context context = RiderStatus.this;
    private String Rider_Lat, Rider_Long;
    private static String riderid, hsid;
    private Location location;

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

                hsid = SHP.get(getBaseContext(), SHP.ids.hsid, "0").toString();
                riderid = SHP.get(getBaseContext(), SHP.ids.uid, "0").toString();

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, RiderStatus.this);
                showCurrentLocation();
                handler.postDelayed(this, MINIMUM_TIME_BETWEEN_UPDATES);
            }
        }, MINIMUM_TIME_BETWEEN_UPDATES);


        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        if (location!=null){
            locationManager.removeUpdates(this);
        }
        super.onDestroy();
    }

    private void Data() {
        Ion.with(this)
                .load("GET", Global.urls.saveLiveBeat.value)
                .addQuery("rdid", riderid)
                .addQuery("lat", Rider_Lat)
                .addQuery("lon", Rider_Long)
                .addQuery("hs_id", hsid)
                .addQuery("flag", "updt")
                .addQuery("btr", getBatteryLevel() + "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            // Gson gson = new Gson();
//                            if(result.get("data").getAsJsonObject().get("status").getAsBoolean()){
//
//                            }

                            // }
                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });
    }

    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }

    protected void showCurrentLocation() {

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
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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

            Data();

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
            Data();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Provider status changed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "GPS turned on",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this,"Please Turn GPS ON to Tracking work",Toast.LENGTH_LONG).show();
    }





}
