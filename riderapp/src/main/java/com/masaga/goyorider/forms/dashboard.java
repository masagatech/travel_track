package com.masaga.goyorider.forms;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
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
import com.masaga.goyorider.model.model_push_order;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.masaga.goyorider.Service.RiderStatus.Rider_Lat;
import static com.masaga.goyorider.Service.RiderStatus.Rider_Long;
import static com.masaga.goyorider.Service.RiderStatus.handler;
import static com.masaga.goyorider.Service.RiderStatus.locationManager;

public class dashboard extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private TextView Online;
    final Popup_Counter CountTimer = new Popup_Counter(180000, 1000);
    private SwitchCompat RiderStatusSwitch;
   Intent mServiceIntent;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        Toast.makeText(this,"Current Battery : "+getBatteryLevel()+"%",Toast.LENGTH_SHORT).show();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();





        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.rider_online_switch);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

        RiderStatusSwitch = (SwitchCompat) findViewById(R.id.compatSwitch);
        Online = (TextView) findViewById(R.id.online);


        RiderStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Online.setText("Online");

                    //Getting JSON data from server
                    Data();
                } else {
                    Online.setText("Offline");
                    handler.removeMessages(0);
                    dashboard.this.stopService(mServiceIntent);
                }
            }
        });


    }

    private  void Data(){
//        Global.showProgress(loader);
        Ion.with(this)
                .load("GET",Global.urls.saveLiveBeat.value)
                .addQuery("rdid", "1")
                .addQuery("av_stat", "1")
                .addQuery("lat", Rider_Lat)
                .addQuery("lon", Rider_Long)
                .addQuery("onoff", "true")
                .addQuery("hs_id", "1")
                .addQuery("btr", getBatteryLevel()+"")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                        if (result != null) Log.v("result", result.toString());
                         // Gson gson = new Gson();
                            if(result.get("data").getAsJsonObject().get("status").getAsBoolean()){

                                //Check if GPS on in user phone, If not promt them on
                                settingsrequest();

                                //Starting Location service and running background
                                mServiceIntent = new Intent(dashboard.this, RiderStatus.class);
                                dashboard.this.startService(mServiceIntent);
                            }
                          // if(gson.fromJson(result.get("data"))==true){


                           // }
                        }
                        catch (Exception ea) {
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
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }


    public void settingsrequest()
    {
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

    private void initiatePopupWindow() {
        try {
            String role;
            role="db";
            LayoutInflater inflater=(LayoutInflater) dashboard.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_orderummery,
                    (ViewGroup) findViewById(R.id.PopUp));
            OrderPopup= new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
            OrderPopup.showAtLocation(layout, Gravity.CENTER,0,0);
            OrderPopup.setOutsideTouchable(false);
            CountTimer.start();

            Btn_Accept=(Button)layout.findViewById(R.id.Accept_order);
            Btn_Reject=(Button)layout.findViewById(R.id.Reject_Order);
            PopUp_CountText=(TextView)layout.findViewById(R.id.popup_counter);
            Deliver_at_Text=(TextView)layout.findViewById(R.id.Deliver_at);

            //If User role = "TL" or "Admin" Show address else don't show address
            if(role.toLowerCase()=="db")
                Deliver_at_Text.setText("xxx");
            else
                Deliver_at_Text.setText("Actual First Delivery Location");


            Btn_Accept.setOnClickListener(accept_order_click);
            Btn_Reject.setOnClickListener(reject_order_click);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
    private View.OnClickListener accept_order_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderPopup.dismiss();
        }
    };
    private View.OnClickListener reject_order_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderPopup.dismiss();
        }
    };

    @OnClick(R.id.Pending_Order)
    void click(){
        Intent intent=new Intent(this,pending_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.Cash_Collection)
    void click2(){
        Intent intent=new Intent(this,cash_collection.class);
        startActivity(intent);
    }
    @OnClick(R.id.Complated_Orders)
    void click3(){
        Intent intent=new Intent(this,complated_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.Rejected_Orders)
    void click4(){
        Intent intent=new Intent(this,rejected_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.All_Order)
    void click5(){
        Intent intent=new Intent(this,all_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.Notifications)
    void click6(){
        initiatePopupWindow();
//        Intent intent=new Intent(this,notification.class);
//        startActivity(intent);
    }
    @OnClick(R.id.pushOrder)
    void click7(){
        Intent intent=new Intent(this,PushOrder.class);
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

    public class Popup_Counter extends CountDownTimer{

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
