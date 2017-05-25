package com.masaga.goyorider.forms;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
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
import com.google.android.gms.location.LocationRequest;
import com.masaga.goyorider.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dashboard extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @BindView(R.id.Pending_Order)
    FrameLayout Pending_Order;
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

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private PopupWindow OrderPopup;
    private Button Btn_Accept, Btn_Reject;
    private TextView Deliver_at_Text;
    private TextView PopUp_CountText;
    private TextView Online;
    final Popup_Counter CountTimer = new Popup_Counter(180000,1000);
    private SwitchCompat RiderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.rider_online_switch);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

        RiderStatus=(SwitchCompat)findViewById(R.id.compatSwitch);
        Online=(TextView) findViewById(R.id.online);


        RiderStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                    Online.setText("Online");
                   RiderLocationService gpsTracker = new RiderLocationService(getApplication());
                   if (gpsTracker.getIsGPSTrackingEnabled())
                   {
                       Toast.makeText(getApplicationContext(),"latitude = "+gpsTracker.latitude+"\n"+"longitude = "+gpsTracker.longitude,Toast.LENGTH_SHORT).show();
                   } else
               {
                   // can't get location
                   // GPS or Network is not enabled
                   // Ask user to enable GPS/network in settings

                   gpsTracker.showSettingsAlert();
               }
               }else {
                   Online.setText("Offline");
               }
            }
        });

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
        initiatePopupWindow();
//        Intent intent=new Intent(this,all_order.class);
//        startActivity(intent);
    }
    @OnClick(R.id.Notifications)
    void click6(){
        Intent intent=new Intent(this,notification.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {

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
