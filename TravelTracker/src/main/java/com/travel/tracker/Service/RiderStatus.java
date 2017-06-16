package com.travel.tracker.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.travel.tracker.initials.splash_screen;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.travel.tracker.R;
import com.travel.tracker.common.CheckAppForground;
import com.travel.tracker.database.SQLBase;
import com.travel.tracker.forms.newOrder;
import com.travel.tracker.gloabls.Global;
import com.travel.tracker.model.model_loginusr;
import com.travel.tracker.model.model_notification;
import com.travel.tracker.utils.SHP;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import static android.app.Notification.VISIBILITY_PUBLIC;


/**
 * Created by fajar on 31-May-17.
 */

public class RiderStatus extends Service implements LocationListener {
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds

    private static final Integer NOTIFICATION_CHECKR_TIMER = 8;//seconds
    private static final Integer LOCATION_SENDER_TIMER = 15;//seconds

    public static LocationManager locationManager;
    public static Handler handler = new Handler();
    public Context context = RiderStatus.this;
    public static String Rider_Lat = "0.0", Rider_Long = "0.0";
    private static String riderid = "0", hsid = "0";
    private Location location;
    Handler handler1;
    Runnable notify;
    String ordid, olnm, stops, amt;
    Integer exptm = 3;
   public static NotificationCompat.Builder mBuilder;
    public static NotificationManager notificationManager;

    Integer NotifyTimerResseter = NOTIFICATION_CHECKR_TIMER;
    Integer LocationTimerResseter = LOCATION_SENDER_TIMER;

    SQLBase sql;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hsid = SHP.get(getBaseContext(), SHP.ids.hsid, "0").toString();
        riderid = SHP.get(getBaseContext(), SHP.ids.uid, "0").toString();
        sql = new SQLBase(this);
        handler1 = new Handler();
        notify = new Runnable() {
            @Override
            public void run() {
                if (NotifyTimerResseter == NOTIFICATION_CHECKR_TIMER) {
                    Notify();
                    NotifyTimerResseter = 0;
                }

                //checkin timer to call
                //sending data to server in frequency
                if (LocationTimerResseter == LOCATION_SENDER_TIMER) {
                    sendingLocationToServer();
                    LocationTimerResseter = 0;
                }
                NotifyTimerResseter += 1;
                LocationTimerResseter += 1;
                handler1.postDelayed(this, 1000);
            }

        };

        handler1.postDelayed(notify, 2000);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_STICKY;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, RiderStatus.this);
        showCurrentLocation();

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (location != null) {
            locationManager.removeUpdates(this);
        }
        handler1.removeCallbacks(notify);
        notify = null;
        handler1 = null;
        // sql.close();

    }

    private void sendingLocationToServer() {
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

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });
    }

    private void Notify() {
        Ion.with(this)
                .load("GET", Global.urls.getNotify.value)
                .addQuery("uid", riderid)
                .addQuery("flag", "neworder")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            {
                                JsonObject d = result.get("data").getAsJsonObject();
                                if (d.get("state").getAsBoolean()) {
                                    JsonObject data = d.get("data").getAsJsonObject().get("extra").getAsJsonObject();
                                    //sql.NOTIFICATION_DELETEAll();
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<model_notification>() {
                                    }.getType();
                                    model_notification m = gson.fromJson(data, listType);

                                    sql.NOTIFICATION_INSERT(data.toString(),m.expmin);
                                    processData(m);
                                }
                            }

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
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null) {
            Rider_Lat = String.valueOf(location.getLatitude());
            Rider_Long = String.valueOf(location.getLongitude());


        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude());
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Rider_Lat = String.valueOf(location.getLatitude());
            Rider_Long = String.valueOf(location.getLongitude());

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Toast.makeText(this, "Provider status changed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Toast.makeText(this, "GPS turned on",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(this,"Please Turn GPS ON to Tracking work",Toast.LENGTH_LONG).show();
    }

    //Notification Showing here
    private void processData(model_notification m) {
        int flag = 0;

        //Map<String,String> Data= (Map<String, String>) _msg.getData();
        Intent dialogIntent = new Intent(RiderStatus.this, newOrder.class);
        if (Global.loginusr == null) {
            Global.loginusr = new model_loginusr();
            Global.loginusr.setDriverid(Long.parseLong(riderid));
        }
        //dialogIntent.putExtra(riderid,)
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("FromDashboard", flag);
        startActivity(dialogIntent);


        ordid = m.ordid;
        olnm = m.olnm;
        stops = m.stops;//obj.get("stops").getAsString();
        amt = m.amt;
        exptm = m.expmin;

        try {
            boolean foregroud = new CheckAppForground().execute(this).get();
//            if(!foregroud)
            {
                PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, splash_screen.class), 0);
                mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.rider)
                                .setContentTitle("New Order")
                                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                                .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                                .setVisibility(BIND_IMPORTANT)
                                .setContentText(olnm)
                                .setContentIntent(pi)
                                .setVisibility(VISIBILITY_PUBLIC )
                                .setOngoing(false);

                // CountTimer.start();
//
                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification note = mBuilder.build();
                note.flags = Notification.FLAG_INSISTENT;
//to post your notification to the notification bar with a id. If a notification with same id already exists, it will get replaced with updated information.
                notificationManager.notify(0, note);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


}
