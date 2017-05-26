package com.masaga.goyorider.forms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by fajar on 26-May-17.
 */

public class TimeService  extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    Context mContext;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {


        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {

            //   buildAlertMessageNoGps();
        }else{
            Intent i = new Intent(context, RiderLocationService.class);
            context.startService(i);
        }
    }

}
