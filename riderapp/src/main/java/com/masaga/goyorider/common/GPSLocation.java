package com.masaga.goyorider.common;

/**
 * Created by mTech on 09-Mar-2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class GPSLocation implements LocationListener {

    Context mContext;
    LocationManager locationManager;
    Location location;
    Boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    Long MIN_TIME_BW_UPDATES = (long) 2000.0;
    Float MIN_DISTANCE_CHANGE_FOR_UPDATES = (float) 5;
    double latitude, longitude;

    public GPSLocation(Context mContext) {
        this.mContext = mContext;

        // getLocation();
    }

    public Location getLocation() {
        // TODO Auto-generated method stub

        if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
//				CommonUtils.buildAlertMessageNoGps(mContext);
				/* Toast.makeText(mContext, "No network provider is enabled",
						 Toast.LENGTH_SHORT).show();*/
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.e("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.e("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast.makeText(mContext,
        // String.valueOf(latitude)+"  "+String.valueOf(longitude),
        // 5000).show();
        // txtGpsStatus.setText(String.valueOf(latitude)+" "+String.valueOf(longitude));
        return location;

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
		/*
		 * Log.e("Latitude=", location.getLatitude()+" "); Log.e("Longitude=",
		 * location.getLongitude()+" ");
		 */

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

}
