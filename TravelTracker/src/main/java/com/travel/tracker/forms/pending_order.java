package com.travel.tracker.forms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.travel.tracker.R;
import com.travel.tracker.adapters.pending_order_adapter;
import com.travel.tracker.gloabls.Global;
import com.travel.tracker.model.model_pending;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.travel.tracker.R.id.txtNodata;
import static com.travel.tracker.Service.RiderStatus.Rider_Lat;
import static com.travel.tracker.Service.RiderStatus.Rider_Long;
import static com.travel.tracker.gloabls.Global.urls.getOrders;
import static com.travel.tracker.gloabls.Global.urls.setTripAction;


public class pending_order extends AppCompatActivity {

    @BindView(R.id.Btn_Call)
    ImageButton Btn_Call;
    @BindView(R.id.Btn_Delivery)
    Button Btn_Delivery;
    @BindView(R.id.Btn_map)
    ImageButton Btn_Map;
    @BindView(R.id.Btn_Return)
    Button Btn_Return;
    @BindView(R.id.Collected_Cash)
    EditText collected_cash;

    private RecyclerView mRecyclerView;
    private ImageButton StartRide,BackButton;
    private com.travel.tracker.adapters.pending_order_adapter mTimeLineAdapter;
    private List<model_pending> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    public static String TripId = "0";
    private ProgressDialog loader;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.pending_order_item);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        if(getSupportActionBar()!=null)
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = true;

        setTitle(getResources().getString(R.string.Work_Status));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        StartRide = (ImageButton) findViewById(R.id.startRide);
        BackButton = (ImageButton) findViewById(R.id.back);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        StartRide.setImageResource(R.drawable.start_trip);
        StartRide.setBackgroundColor(Color.parseColor("#ff99cc00"));
        StartRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TripId.equals("0")) {
                    new AlertDialog.Builder(pending_order.this)
                            .setTitle("Start Trip")
                            .setMessage("Are you want Start Trip?")
                            .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startTrip();


                                }
                            })
                            .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.rider).show();

                } else {
                    new AlertDialog.Builder(pending_order.this)
                            .setTitle("Stop Trip")
                            .setMessage("Are you sure you want Stop Trip?")
                            .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    stopTrip();


                                }
                            })
                            .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.stop_trip).show();


                }
            }
        });
//
     //  initView();

        loader = new ProgressDialog(this);
        loader.setCancelable(false);
        loader.setMessage("Please wait..");
        loader.show();

        Ion.with(this)
                .load("GET", getOrders.value)
                .addQuery("flag", "orders")
                .addQuery("subflag", "pending")
                .addQuery("rdid", Global.loginusr.getDriverid() + "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {

                            if (result != null) Log.v("result", result.toString());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<model_pending>>() {
                            }.getType();
                            List<model_pending> events = (List<model_pending>) gson.fromJson(result.get("data"), listType);
                            bindCurrentTrips(events);

//                            result.get("data").getAsJsonObject().get("count");
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        loader.hide();
                    }
                });

    }

    private LinearLayoutManager getLinearLayoutManager() {
        //if (mOrientation == Orientation.HORIZONTAL) {
        //    return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // } else {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // }
    }

    private void initView() {
//        setDataListItems();
//        mTimeLineAdapter = new pending_order_adapter(mDataList, mOrientation, mWithLinePadding);
//        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void startTrip(){

        JsonObject json = new JsonObject();
        json.addProperty("flag", "start");
        json.addProperty("loc", Rider_Lat+","+Rider_Long);
        json.addProperty("tripid", TripId);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");

        Ion.with(this)
                .load(setTripAction.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                          JsonObject Data=  result.get("data").getAsJsonObject();
                            if(Data.get("status").getAsBoolean()){
                                TripId=Data.get("tripid").toString();
                                StartRide.setBackgroundColor(Color.RED);
                                Toast.makeText(getApplicationContext(),"Your Ride Has started"
                                        ,Toast.LENGTH_SHORT).show();
                                StartRide.setBackgroundColor(Color.parseColor("#ffff4444"));
                                StartRide.setImageResource(R.drawable.end_trip);
                                mTimeLineAdapter.tripid = TripId;
                            }
                            else{
                                Toast.makeText(getApplicationContext(),result.get("data").getAsJsonObject().get("msg").toString()
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });

    }

    public void stopTrip(){

        JsonObject json = new JsonObject();
        json.addProperty("flag", "stop");
        json.addProperty("loc", Rider_Lat+","+Rider_Long);
        json.addProperty("tripid", TripId);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");

        Ion.with(this)
                .load(setTripAction.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            if(result.get("data").getAsJsonObject().get("status").getAsBoolean()){
                                Toast.makeText(getApplicationContext(),result.get("data").getAsJsonObject().get("msg").toString()
                                        ,Toast.LENGTH_SHORT).show();
                                //StartRide.setVisibility(View.GONE);
                                   onBackPressed();
//                                Intent intent=new Intent(pending_order.this,dashboard.class);
//                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),result.get("data").getAsJsonObject().get("msg").toString()
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });

    }

    private void setDataListItems(){



//        mDataList.add(new PendingModel("#198" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.ACTIVE,0.00));
//        mDataList.add(new PendingModel("#199" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,0.00));
//        mDataList.add(new PendingModel("#200" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,209.00));
//        mDataList.add(new PendingModel("#201" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,349.00));
//        mDataList.add(new PendingModel("#202" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,460.50));

    }
    private void bindCurrentTrips(List<model_pending> lst) {
        if (lst.size() > 0) {
            TripId = lst.get(0).tripid;
            if(TripId.equals("0")){
                //greeen
                StartRide.setImageResource(R.drawable.start_trip);
                StartRide.setBackgroundColor(Color.parseColor("#ff99cc00"));
            }else {
                //red
                StartRide.setBackgroundColor(Color.parseColor("#ffff4444"));
                StartRide.setImageResource(R.drawable.end_trip);
            }


            StartRide.setVisibility(View.VISIBLE);
            findViewById(txtNodata).setVisibility(View.GONE);
            for (int i =0; i<=lst.size()-1 ;i ++){
                if(!lst.get(i).stats.equals("0")){
                    lst.remove(i);
                    i-=1;
                }
            }

            if(!TripId.equals("0") && lst.size() ==0){
                TextView Text =(TextView)findViewById(txtNodata);
                findViewById(txtNodata).setVisibility(View.VISIBLE);
                Text.setText("You have Deliverd all food.\n Press Stop to continue..");
            }

            mTimeLineAdapter = new pending_order_adapter(lst, mOrientation, mWithLinePadding);
            mTimeLineAdapter.tripid = TripId;
            mRecyclerView.setAdapter(mTimeLineAdapter);
            mTimeLineAdapter.notifyDataSetChanged();

        } else {

            StartRide.setVisibility(View.GONE);
            findViewById(txtNodata).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTimeLineAdapter!=null){
            mTimeLineAdapter.notifyDataSetChanged();
        }
    }

    //  @Override
    // protected void onSaveInstanceState(Bundle savedInstanceState) {
    //   if(mOrientation!=null)
    //         savedInstanceState.putSerializable(MainActivity.EXTRA_ORIENTATION, mOrientation);
    //    super.onSaveInstanceState(savedInstanceState);
    // }

    // @Override
    // protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //    if (savedInstanceState != null) {
    //        if (savedInstanceState.containsKey(MainActivity.EXTRA_ORIENTATION)) {
    //            mOrientation = (Orientation) savedInstanceState.getSerializable(MainActivity.EXTRA_ORIENTATION);
    //        }
    //     }
    //     super.onRestoreInstanceState(savedInstanceState);
    // }
}
