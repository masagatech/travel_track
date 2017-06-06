package com.masaga.goyorider.forms;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;

import android.location.LocationListener;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.R;
import com.masaga.goyorider.Service.RiderStatus;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.goyorider.MainActivity;
import com.masaga.goyorider.initials.splash_screen;
import com.masaga.goyorider.model.model_loginusr;
import com.masaga.goyorider.model.model_pending;
import com.masaga.goyorider.model.model_push_order;
import com.masaga.goyorider.utils.SHP;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.masaga.goyorider.Service.RiderStatus.handler;
import static com.masaga.goyorider.Service.RiderStatus.locationManager;
import static com.masaga.goyorider.gloabls.Global.urls.getStatus;
import static com.masaga.goyorider.gloabls.Global.urls.setStatus;

public class dashboard extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.Pending_Order)
    FrameLayout Pending_Order;
    @BindView(R.id.pushOrder)
    FrameLayout Push_Order;
    @BindView(R.id.Complated_Orders)
    FrameLayout Complated_Orders;
    @BindView(R.id.Rejected_Orders)
    FrameLayout Rejected_Orders;
    @BindView(R.id.Cash_Collection)
    FrameLayout Cash_Collection;
    @BindView(R.id.Notifications)
    FrameLayout Notifications;
    @BindView(R.id.All_Order)
    FrameLayout All_Order;

    private PopupWindow OrderPopup;
    private Button Btn_Accept, Btn_Reject;
    private TextView Deliver_at_Text;
    private TextView PopUp_CountText;
    private TextView Online, RiderName;
    private ImageButton Logout;
    private SwitchCompat RiderStatusSwitch;
    Intent mServiceIntent;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    GoogleApiClient mGoogleApiClient;
    private String latitude, longitude;
    public Criteria criteria;
    public String bestProvider;
    LocationManager locationManager2;
    final Popup_Counter CountTimer = new Popup_Counter(180000, 1000);
    private NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.rider_online_switch);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

        getStatus();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        //Check if GPS on in user phone, If not promt them on
        settingsrequest();


        locationManager2 = (LocationManager) this.getSystemService(LOCATION_SERVICE);
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
        Location location = locationManager2.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        location = locationManager2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager2.getBestProvider(criteria, true)).toString();
        if ((location == null)) {
            locationManager2.requestLocationUpdates(bestProvider, 1000, 0, this);
        }

        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            Toast.makeText(this, latitude + "\n" + longitude, Toast.LENGTH_SHORT).show();
        }

//      Toast.makeText(this,"Current Battery : "+getBatteryLevel()+"%",Toast.LENGTH_SHORT).show();


        RiderStatusSwitch = (SwitchCompat) findViewById(R.id.compatSwitch);
        Online = (TextView) findViewById(R.id.online);
        RiderName = (TextView) findViewById(R.id.nameRider);
        Logout = (ImageButton) findViewById(R.id.Logout);

        RiderName.setText(Global.loginusr.getFullname());

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(dashboard.this)
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.confirm_logout))
                        .setPositiveButton(R.string.alert_ok_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String sessionid = SHP.get(dashboard.this, SHP.ids.sessionid, "-1").toString();
                                String uid = SHP.get(dashboard.this, SHP.ids.uid, "-1").toString();
                                JsonObject json = new JsonObject();
                                json.addProperty("sessionid", sessionid);
                                json.addProperty("email", uid);
//                                Global.showProgress(loader);
                                Ion.with(dashboard.this)
                                        .load(Global.urls.getlogout.value)

                                        .setJsonObjectBody(json)
                                        .asJsonObject()
                                        .setCallback(new FutureCallback<JsonObject>() {
                                            @Override
                                            public void onCompleted(Exception e, JsonObject result) {
                                                // do stuff with the result or error
                                                try {
                                                    if (result != null)
                                                        Log.v("result", result.toString());
                                                    // JSONObject jsnobject = new JSONObject(jsond);
                                                    Gson gson = new Gson();
                                                    Type listType = new TypeToken<List<model_loginusr>>() {
                                                    }.getType();
                                                    List<model_loginusr> login = (List<model_loginusr>) gson.fromJson(result.get("data"), listType);
                                                    if (login.size() > 0) {
                                                        model_loginusr m = login.get(0);
                                                        if (m.getStatus() == 1) {
                                                            SHP.set(dashboard.this, SHP.ids.uid, "");
                                                            SHP.set(dashboard.this, SHP.ids.sessionid, "");
                                                            Intent i = new Intent(dashboard.this, com.masaga.goyorider.initials.login.class);
                                                            startActivity(i);
                                                            dashboard.this.finish();
                                                        } else {
                                                            Toast.makeText(dashboard.this, "Faild to logout " + m.getErrcode() + " " + m.getErrmsg(), Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(dashboard.this, "Oops there is some issue! please logout later!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception ea) {
                                                    Toast.makeText(dashboard.this, "Oops there is some issue! Error: " + ea.getMessage(), Toast.LENGTH_LONG).show();
                                                    ea.printStackTrace();
                                                }
//                                                Global.hideProgress(loader);
                                            }
                                        });

                            }
                        })
                        .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_lock_lock).show();
            }
        });


        RiderStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(isStatusDbCheck) {
                        //Notification
                        showNotification();
                        Online.setText("Online");
                        SwitchTurnedOnOFF("true");
                        return;
                    }

                    new AlertDialog.Builder(dashboard.this)
                            .setTitle("Online")
                            .setMessage("Ready for some delivery?")
                            .setPositiveButton("Yes, Lets go!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Getting JSON data from server
                                    SwitchTurnedOnOFF("true");
                                }
                            })
                            .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    RiderStatusSwitch.setChecked(false);

                                }
                            })
                            .setIcon(R.drawable.rider_del).show();

                } else {
                    if(isStatusDbCheck) {
                        return;
                    }
                    new AlertDialog.Builder(dashboard.this)
                            .setTitle("Offline")
                            .setMessage("Are you sure you want go Offline Mode?")
                            .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    notificationManager.cancel(0);
                                    Online.setText("Offline");
                                    handler.removeMessages(0);
                                    if(isMyServiceRunning(RiderStatus.class)){
                                            dashboard.this.stopService(mServiceIntent);
                                            SwitchTurnedOnOFF("false");
                                            Toast.makeText(getApplicationContext(), "Stopping Service", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    RiderStatusSwitch.setChecked(true);
                                }
                            })
                            .setIcon(R.drawable.rider_del).show();

                }
            }
        });


    }

    boolean isStatusDbCheck = false;

    private void getStatus() {

        Ion.with(this)
                .load("GET", getStatus.value)
                .addQuery("flag", "rider")
                .addQuery("rdid", Global.loginusr.getDriverid() + "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            JsonObject o = result.get("data").getAsJsonObject();
                            boolean avail = o.get("onoff").getAsBoolean();
                            isStatusDbCheck = true;
                            RiderStatusSwitch.setChecked(avail);
                            isStatusDbCheck = false;
                            // }
                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });

    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, splash_screen.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Online!")
                .setSmallIcon(R.drawable.rider)
                .setContentTitle("Your Online!")
//                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setAutoCancel(false)
                .setOngoing(true)
                .build();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }


    private void SwitchTurnedOnOFF(final String state) {
//        Global.showProgress(loader);
        if(latitude == null){
            latitude = "0.0";
            longitude = "0.0";
        }
        Ion.with(this)
                .load("GET", Global.urls.saveLiveBeat.value)
                .addQuery("flag", "avail")
                .addQuery("rdid", Global.loginusr.getDriverid() + "")
                .addQuery("lat", latitude)
                .addQuery("lon", longitude)
                .addQuery("onoff", state)
                .addQuery("hs_id", Global.loginusr.getHsid())
                .addQuery("btr", getBatteryLevel() + "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            if (state == "true") {
                                if (result.get("data").getAsJsonObject().get("status").getAsBoolean()) {
                                    //Starting Location service and running background

                                    if(!isMyServiceRunning(RiderStatus.class)) {
                                        //Notification
                                        showNotification();
                                        Online.setText("Online");
                                        Toast.makeText(getApplicationContext(), "Starting Service", Toast.LENGTH_SHORT).show();
                                        mServiceIntent = new Intent(dashboard.this, RiderStatus.class);
                                        dashboard.this.startService(mServiceIntent);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),result.get("data").getAsJsonObject().get("msg").toString() , Toast.LENGTH_SHORT).show();
                                    RiderStatusSwitch.setChecked(false);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Switch Off", Toast.LENGTH_SHORT).show();
                            }
                            // }
                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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


    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(dashboard.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsrequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }



    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @OnClick(R.id.Pending_Order)
    void click() {
        Intent intent = new Intent(this, pending_order.class);
        startActivity(intent);
    }

    @OnClick(R.id.Cash_Collection)
    void click2() {
        Intent intent = new Intent(this, cash_collection.class);
        startActivity(intent);
    }

    @OnClick(R.id.Complated_Orders)
    void click3() {
        Intent intent = new Intent(this, complated_order.class);
        startActivity(intent);
    }

    @OnClick(R.id.Rejected_Orders)
    void click4() {
        Intent intent = new Intent(this, rejected_order.class);
        startActivity(intent);
    }

    @OnClick(R.id.All_Order)
    void click5() {
        Intent intent = new Intent(this, all_order.class);
        startActivity(intent);
    }

    @OnClick(R.id.Notifications)
    void click6() {
        int flag=1;
        Intent intent=new Intent(this,newOrder.class);
//        Intent intent=new Intent(this,notification.class);
//        intent.putExtra("FromDashboard", flag);
        startActivity(intent);
    }

    @OnClick(R.id.pushOrder)
    void click7() {
        Intent intent = new Intent(this, PushOrder.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        Toast.makeText(this, latitude + "\n" + longitude, Toast.LENGTH_SHORT).show();

        locationManager2.removeUpdates(this);

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
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        super.onDestroy();
    }


    public class Popup_Counter extends CountDownTimer {

        public Popup_Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            OrderPopup.dismiss();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            PopUp_CountText.setText(""+String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
        }
    }
}
