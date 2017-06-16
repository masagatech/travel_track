package com.travel.tracker.common;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.travel.tracker.gloabls.Global;

/**
 * Created by mTech on 04-May-2017.
 */
public class ProximityIntentReceiver extends BroadcastReceiver {
    public static final String TripID_INTENT_EXTRA = "TripID";
    public static final String StudID_INTENT_EXTRA = "StudID";
    public static final String PROX_ALERT_INTENT = "com.goyo.goyorider";
    public static final String LocationReach = "0010";


    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            //context.unregisterReceiver(this);
            String key = LocationManager.KEY_PROXIMITY_ENTERING;
            Boolean entering = intent.getBooleanExtra(key, false);
            String TripId = intent.getStringExtra(ProximityIntentReceiver.TripID_INTENT_EXTRA);
            String StudId = intent.getStringExtra(ProximityIntentReceiver.StudID_INTENT_EXTRA);
            removeProximityAlert(context, Integer.parseInt(LocationReach +"" + TripId + "" + StudId));
            if (entering) {
                Log.d(getClass().getSimpleName(), "entering");
                Toast.makeText(context, "Welcome to my Area " + StudId, Toast.LENGTH_SHORT).show();
                sendInfoToServer(context, TripId, StudId);
            } else {
                Log.d(getClass().getSimpleName(), "exiting");
                Toast.makeText(context, "Exiting stdid " + StudId, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void sendInfoToServer(Context c, String tripid, String stdid) {
        JsonObject json = new JsonObject();
        json.addProperty("tripid", tripid);
        json.addProperty("studid", stdid);
        Ion.with(c)
                .load(Global.urls.sendreachingalert.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {


                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        // menu_refresh.setEnabled(false);

                    }
                });
    }

    private void removeProximityAlert(Context c, int reqcode) {
        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        Intent intent = new Intent();
        intent.setAction(ProximityIntentReceiver.PROX_ALERT_INTENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, reqcode, intent, 0);
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeProximityAlert(pendingIntent);
    }

}
