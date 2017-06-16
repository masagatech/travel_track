package com.travel.tracker.forms;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.travel.tracker.SocketClient.SC_IOApplication;
import com.travel.tracker.adapters.picup_listAdapter;
import com.travel.tracker.common.Checker;
import com.travel.tracker.common.ProximityIntentReceiver;
import com.travel.tracker.gloabls.Global;
import com.travel.tracker.googlemap.LatLngInterpolator;
import com.travel.tracker.googlemap.MarkerAnimation;
import com.travel.tracker.R;
import com.travel.tracker.model.model_crewdata;
import com.travel.tracker.nwt.londatiga.android.ActionItem;
import com.travel.tracker.nwt.londatiga.android.QuickAction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.travel.tracker.utils.common;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class pickupcrew extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, com.google.android.gms.location.LocationListener, Animation.AnimationListener {
    /*Controls*/
    private ListView lst_ripcrew_list;
    private TextView txtNodata, dialog_txtCountdown;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private Dialog dialog;
    private ProgressBar prgDialog;
    private ProgressDialog loader;
    private FloatingActionButton btnStartStop;
    //variables
    private ArrayList<model_crewdata> lstcrew;
    private String CurrentFilter = "";
    private Boolean isLocationPermission = false;
    boolean isstarted = false;
    private Resources mResources;
    private boolean isRecenter = true;
    // Keep track of our markers
    private List<HashMap<String, Marker>> crewsOnMap = new ArrayList<HashMap<String, Marker>>();

    //variables holding values
    private String msttrpid = "0";
    private String TripId = "0";

    //googel map related variables
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Marker mypos;
    private GoogleApiClient mGoogleApiClient;
    private Location mPreviousLoc;
    private Location mCurrentLoc;
    private LocationManager locationManager;

    //crew list view and its hover menu
    private int mSelectedRow = 0;
    private static final int ID_ROUTE = 1;
    private static final int ID_ACCEPT = 2;
    private static final int ID_ABSENT = 3;
    private static final int ID_CALL = 4;
    private static final int ID_DETAILS = 5;
    private QuickAction mQuickAction;

    private Context viewContext;
    //map
    //private Animator animator;

    //Location Manager and settings
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 3000; // 3 seconds
    private static final long FASTEST_TIME_BW_UPDATES = 7000; // 30 seconds

    //map constants
    private float tilt = 90;
    private float zoom = 17.5f;
    private boolean upward = true;

    //drag panel
    private SlidingUpPanelLayout mLayout;

    //socket
    private Socket mSocket;
    private boolean isSocConnected;
    //proximity location reciver
    private PendingIntent pendingIntent;
    private ProximityIntentReceiver pibrRec;
    //check manually distance
    private LatLng lastLatLongChecked;
    private Integer getCheck_location_when_my_pos = Global.appsettings.getCheck_location_when_my_pos();
    private Integer getRedious_user_alert = Global.appsettings.getRedious_user_alert();

    //List for notify
    private ArrayList<model_crewdata> lstNotify;

    //Animation
    Animation searchAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickupcrews);
        //getSupportActionBar().hide();
        ButterKnife.bind(this);
        screenSettings();
        setTitle(getResources().getString(R.string.pickup_title));
        //checkGPS();
        initAllControls();
        getBundle();
        addListner();
        // SocketClient();
    }

    private void screenSettings() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /*fill all controls*/
    private void initAllControls() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        googleMapInit();
        loader = new ProgressDialog(this);
        lst_ripcrew_list = (ListView) findViewById(R.id.lst_ripcrew_list);
        txtNodata = (TextView) findViewById(R.id.txtNodata);
        btnStartStop = (FloatingActionButton) findViewById(R.id.btnStartStop);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        btnStartStop.setEnabled(false);

        searchAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
//        btnAll = (Button) findViewById(R.id.btnAll);
//        btnPending = (Button) findViewById(R.id.btnPending);
//        btnOthers = (Button) findViewById(R.id.btnOthers);

        waitingForLocation();

        // startLocationTracing(5000);

    }

    private void googleMapInit() {
        SupportMapFragment mMap1 = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap1.getMapAsync(this);

        if (mGoogleApiClient == null) {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //animator = new Animator();

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String stat = bundle.get("stat").toString();
        String title = bundle.get("title").toString();
        String subtitle = bundle.get("subtitle").toString();
        setTitle(title);
        try {
            this.getSupportActionBar().setSubtitle(subtitle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        msttrpid = bundle.get("msttrpid").toString();
        TripId = bundle.get("tripid").toString();
        if (stat.equals(Global.start)) {
            StartStopButtonSource("stop");
            isstarted = true;
            bottomBar.setDefaultTab(R.id.tab_pending);
            listviewMenuBind();

        } else if (stat.equals(Global.done) || stat.equals(Global.cancel)) {
            btnStartStop.setVisibility(View.GONE);
            bottomBar.setDefaultTab(R.id.tab_all);

        } else if (stat.equals(Global.pending)) {
            StartStopButtonSource("start");
            bottomBar.setDefaultTab(R.id.tab_pending);
            isstarted = false;
        }
        checkGetTripDetails();
    }

    private void checkGetTripDetails() {
        btnStartStop.setEnabled(true);
        JsonObject json = new JsonObject();
        json.addProperty("driverid", Global.loginusr.getDriverid());
        json.addProperty("tripid", TripId);
        json.addProperty("msttripid", msttrpid);
        Global.showProgress(loader);
        Ion.with(this)
                .load(Global.urls.getmytripscrew.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            if (result != null) Log.v("result", result.toString());
                            // JSONObject jsnobject = new JSONObject(jsond);
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<model_crewdata>>() {
                            }.getType();
                            JsonElement k = result.get("data");
                            lstcrew = (ArrayList<model_crewdata>) gson.fromJson(result.get("data"), listType);
                            bindCreawData(lstcrew);
                            //addCrewLocationToMap();

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }

                        Global.hideProgress(loader);
                    }
                });
    }

    private void addListner() {
        btnStartStop.setOnClickListener(this);

        //slide panel

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //Filter List
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_pending) {
                    CurrentFilter = "0";
                    notifyCrewChanges();
                }else if(tabId == R.id.tab_done)
                {
                    CurrentFilter = "1";
                    notifyCrewChanges();
                }else if(tabId == R.id.tab_all){
                    CurrentFilter = "";
                    notifyCrewChanges();
                }
            }
        });
    }


    private void addMapListner() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                isRecenter = false;
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //TODO: Any custom actions
                isRecenter = true;
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartStop: {
                if (isstarted) {
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.str_stop))
                            .setMessage(getResources().getString(R.string.alertconfirmtext_stopstrop))
                            .setPositiveButton(R.string.alert_ok_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    StopTrip();

                                }
                            })
                            .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(R.drawable.ic_action_stop).show();

                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.str_start))
                            .setMessage(getResources().getString(R.string.alertconfirmtext_starttrip))
                            .setPositiveButton(R.string.alert_ok_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    StartTrip();

                                }
                            })
                            .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(R.drawable.ic_action_start).show();
                }
            }
            break;
        }
    }

    //start trip server api call
    private void StartTrip() {
        JsonObject json = new JsonObject();
        json.addProperty("tripid", TripId);
        json.addProperty("pdid", msttrpid);
        json.addProperty("driverid", Global.loginusr.getDriverid());
        String loc = "0,0";
        if (mCurrentLoc != null) {
            loc = mCurrentLoc.getLatitude() + "," + mCurrentLoc.getLongitude();
        }
        json.addProperty("loc", loc);
        Global.showProgress(loader);
        Ion.with(this)
                .load(Global.urls.starttrip.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            if (result != null) Log.v("result", result.toString());
                            String Status = (result.get("data").getAsJsonObject()).get("resstatus").toString();
                            if (Boolean.parseBoolean(Status)) {
                                TripId = (result.get("data").getAsJsonObject()).get("tripid").toString();
                                StartStopButtonSource("stop");
                                onStrtAnimation();
                                //registerForAlert();
                                listviewMenuBind();
                                isstarted = true;
                            } else {
                                Toast.makeText(pickupcrew.this, (result.get("data").getAsJsonObject()).get("resmessage").toString(), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        Global.hideProgress(loader);
                    }
                });
    }

    //stop trip server api call
    private void StopTrip() {
        JsonObject json = new JsonObject();
        json.addProperty("tripid", TripId);
        json.addProperty("pdid", msttrpid);
        json.addProperty("driverid", Global.loginusr.getDriverid());
        String loc = "0,0";
        if (mCurrentLoc != null) {
            loc = mCurrentLoc.getLatitude() + "," + mCurrentLoc.getLongitude();
        }
        json.addProperty("loc", loc);
        Global.showProgress(loader);
        Ion.with(this)
                .load(Global.urls.stoptrip.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            if (result != null) Log.v("result", result.toString());
                            String Status = (result.get("data").getAsJsonObject()).get("resstatus").toString();
                            if (Boolean.parseBoolean(Status)) {
                                Toast.makeText(pickupcrew.this, (result.get("data").getAsJsonObject()).get("resmessage").toString(), Toast.LENGTH_LONG).show();
                                btnStartStop.setVisibility(View.GONE);
                                isstarted = false;
                            } else {
                                Toast.makeText(pickupcrew.this, (result.get("data").getAsJsonObject()).get("resmessage").toString(), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        Global.hideProgress(loader);

                    }
                });
    }
    //stop trip

    //store delta

    private void StoreTripsDelta() {

        if (!isstarted) {
            return;
        }
        float bearing = 0f;
        if (mPreviousLoc != null) {
            bearing = mPreviousLoc.bearingTo(mCurrentLoc);
        }
        int speed = (int) ((mCurrentLoc.getSpeed() * 3600) / 1000);

        JsonObject json = new JsonObject();
        json.addProperty("tripid", TripId);
        json.addProperty("pdid", msttrpid);
        json.addProperty("drvid", Global.loginusr.getDriverid());
        json.addProperty("appver", "1.0");
        json.addProperty("speed", speed + "");
        json.addProperty("uid", "1");
        json.addProperty("bearing", bearing + "");
        json.addProperty("loctm", common.dateandtime(this));

        String loc = "[0,0]";
        if (mCurrentLoc != null) {
            loc = "[" + mCurrentLoc.getLatitude() + "," + mCurrentLoc.getLongitude() + "]";
        }
        json.addProperty("loc", loc);
        Ion.with(this)
                .load(Global.urls.storetripdelta.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            /*if (result != null) Log.v("result", result.toString());
                            String Status = (result.get("data").getAsJsonObject()).get("resstatus").toString();
                            if (Boolean.parseBoolean(Status)) {

                            } else {
                                Toast.makeText(pickupcrew.this, (result.get("data").getAsJsonObject()).get("resmessage").toString(), Toast.LENGTH_LONG).show();
                            }*/

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }


                    }
                });

    }
    //store delta

    // pickup crew server api call
    private void PickUpDropCrew(final String _Studid, String _TripId, final String _Status) {
        JsonObject json = new JsonObject();
        json.addProperty("tripid", _TripId);
        json.addProperty("studid", _Studid);
        json.addProperty("status", _Status);
        json.addProperty("driverid", Global.loginusr.getDriverid());
        String loc = "0,0";
        if (mCurrentLoc != null) {
            loc = mCurrentLoc.getLatitude() + "," + mCurrentLoc.getLongitude();
        }
        json.addProperty("loc", loc);
        Global.showProgress(loader);
        Ion.with(this)
                .load(Global.urls.picdropcrew.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            if (result != null) Log.v("result", result.toString());
                            String Status = (result.get("data").getAsJsonObject()).get("resstatus").toString();
                            if (Boolean.parseBoolean(Status)) {
                                Toast.makeText(pickupcrew.this, (result.get("data").getAsJsonObject()).get("resmessage").toString(), Toast.LENGTH_LONG).show();
                                Global.crewDetails.stsi = _Status;
                                notifyCrewChanges();
                                removeCreawFromMap(_Studid);

                                //btnStartStop.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(pickupcrew.this, (result.get("data").getAsJsonObject()).get("resmessage").toString(), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        Global.hideProgress(loader);
                    }
                });


    }
    ///

    picup_listAdapter _picup_listAdapter;
    ArrayList<model_crewdata> _crewlst;

    private void bindCreawData(ArrayList<model_crewdata> lst) {
        if (lst.size() > 0) {
            _crewlst = lst;
            findViewById(R.id.txtNodata).setVisibility(View.GONE);
            _picup_listAdapter = new picup_listAdapter(this, lst, getResources());
            lst_ripcrew_list.setAdapter(_picup_listAdapter);
            notifyCrewChanges();
            addCrewLocationToMap();
            //registerForAlert();
        } else {
            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
        }
    }

    private void notifyCrewChanges() {
        if (_picup_listAdapter != null) {
            /*Collections.sort(_crewlst, new Comparator<model_crewdata>() {
                public int compare(model_crewdata o1, model_crewdata o2) {
                    return o1.stsi.compareToIgnoreCase(o2.stsi);
                }
            });*/
            //_picup_listAdapter.notifyDataSetChanged();
            _picup_listAdapter.getFilter().filter(CurrentFilter);
        }
    }

    private void listviewMenuBind() {
        ActionItem picup = new ActionItem(ID_ACCEPT, getResources().getString(R.string.pickup_opt_pickup), getResources().getDrawable(R.drawable.ic_person_add_black_24dp), "#049A0D");
        ActionItem absent = new ActionItem(ID_ABSENT, getResources().getString(R.string.pickup_opt_absent), getResources().getDrawable(R.drawable.ic_highlight_off_black_24dp), "#FF0000");
        ActionItem details = new ActionItem(ID_DETAILS, getResources().getString(R.string.pickup_opt_info), getResources().getDrawable(R.drawable.ic_face_black_24dp), "#FF0000");
        ActionItem call = new ActionItem(ID_CALL, getResources().getString(R.string.pickup_opt_call), getResources().getDrawable(R.drawable.ic_call_black_24dp), "#FF8B00");
        ActionItem location = new ActionItem(ID_ROUTE, getResources().getString(R.string.pickup_opt_route), getResources().getDrawable(R.drawable.ic_location_on_black_24dp), "#000000");

        mQuickAction = new QuickAction(this);

        mQuickAction.addActionItem(location);
        mQuickAction.addActionItem(picup);
        mQuickAction.addActionItem(absent);
        mQuickAction.addActionItem(call);
        mQuickAction.addActionItem(details);


        if (mQuickAction != null) {
            mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                @Override
                public void onItemClick(QuickAction quickAction, int pos, int actionId) {
                    ActionItem actionItem = quickAction.getActionItem(pos);

                    if (actionId == ID_DETAILS) { //Add item selected
                        Intent i = new Intent(pickupcrew.this, crew_details.class);
                        startActivity(i);

                        //Toast.makeText(getApplicationContext(), "Add item selected on row " + mSelectedRow, Toast.LENGTH_SHORT).show();
                    } else if (actionId == ID_ACCEPT) { //Add item selected
                        PickUpDropCrew(Global.crewDetails.stdid, TripId, "1");
                    } else if (actionId == ID_ABSENT) { //Add item selected
                        PickUpDropCrew(Global.crewDetails.stdid, TripId, "2");
                    } else if (actionId == ID_ROUTE) { //Add item selected

                       /* Intent i = new Intent(pickupcrew.this, googlemapexp.class);
                        startActivity(i);*/
                        //Toast.makeText(getApplicationContext(), "Add item selected on row " + mSelectedRow, Toast.LENGTH_SHORT).show();

                        // Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row "
                        //       + mSelectedRow, Toast.LENGTH_SHORT).show();
                    } else if (actionId == ID_ACCEPT) { //Add item selected
                        // animator.startAnimation(true);
                    }
                }
            });
        }
        if (mQuickAction != null) {
            //setup on dismiss listener, set the icon back to normal
            mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    // mMoreIv.setImageResource(R.drawable.ic_list_more);
                }
            });
        }
        lst_ripcrew_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedRow = position; //set the selected row
                Global.crewDetails = ((model_crewdata) parent.getItemAtPosition(position));
                //  viewContext = view.getContext();

                if (mQuickAction != null) {

                    mQuickAction.show(view);
                }
            }
        });
    }

    private void addCrewLocationToMap() {

        for (model_crewdata creaw : this.lstcrew) {
            if (creaw.stsi.equals("0")) {
                addMarkerToMap(new LatLng(Float.parseFloat(creaw.loc.lat), Float.parseFloat(creaw.loc.lon)), creaw.stdnm, creaw.addr, creaw.stdid);
                //
            }
        }
    }

    private void registerForAlert() {
        if (pibrRec == null) pibrRec = new ProximityIntentReceiver();

        if (!isstarted) return;
        for (model_crewdata creaw : this.lstcrew) {
            if (creaw.stsi.equals("0")) {
                setProximityAlert(Float.parseFloat(creaw.loc.lat), Float.parseFloat(creaw.loc.lon), TripId, creaw.stdid);
            }
        }
    }

    private void unRegisterForAlert() {
        if (!isstarted) return;
        for (model_crewdata creaw : this.lstcrew) {
            if (creaw.stsi.equals("0")) {
                try {
                    removeProximityAlerts(TripId, creaw.stdid);
                } catch (Exception ec) {
                    ec.printStackTrace();
                }
            }
        }
        try {
            if (pibrRec != null)
                unregisterReceiver(pibrRec);
            pibrRec = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private boolean removeCreawFromMap(String id) {
        int searchListLength = crewsOnMap.size();
        for (int i = 0; i < searchListLength; i++) {
            if (crewsOnMap.get(i).containsKey(id)) {
                Marker mrk = crewsOnMap.get(i).get(id);
                mrk.remove();
                crewsOnMap.get(i).remove(id);
                return true;
            }
        }
        return false;
    }

    private void addMarkerEntry(String id, Marker marker) {
        HashMap<String, Marker> entry = new HashMap<String, Marker>();
        entry.put(id, marker);
        crewsOnMap.add(entry);
    }

    Dialog _location_waiting;

    private void waitingForLocation() {
        _location_waiting = new Dialog(pickupcrew.this);
        _location_waiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _location_waiting.setCancelable(false);
        _location_waiting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        _location_waiting.setContentView(R.layout.layout_waiting_for_location);
        ImageView im = (ImageView) _location_waiting.findViewById(R.id.imganimate);
        TextView txts = (TextView) _location_waiting.findViewById(R.id.txtSearching);
        im.setAnimation(searchAnimation);
        //txts.setAnimation(searchAnimation);
        searchAnimation.start();
        _location_waiting.show();

    }


    private void onStrtAnimation() {
        dialog = new Dialog(pickupcrew.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_countdown);
        dialog_txtCountdown = (TextView) dialog.findViewById(R.id.txtCounter);
        prgDialog = (ProgressBar) dialog.findViewById(R.id.prgCountDown);
        dialog_txtCountdown.setText("0");
        prgDialog.setProgress(0);
        dialog.show();

        new CountDownTimer(4000, 700) {
            Integer prgval = -100;
            Integer counter = -1;

            public void onTick(long millisUntilFinished) {
                prgval = prgval + 100;
                counter += 1;
                //  mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                if (counter <= 3) {
                    switch (counter) {
                        case 1:
                            dialog_txtCountdown.setText("GET");
                            break;
                        case 2:
                            dialog_txtCountdown.setText("SET");
                            break;
                        case 3:
                            dialog_txtCountdown.setText("GO");
                            break;
                        default:
                            dialog_txtCountdown.setText("...");
                            break;
                    }

                }
                ObjectAnimator animation = ObjectAnimator.ofInt(prgDialog, "progress", prgval - 100, prgval); // see this max value coming back here, we animale towards that value
                animation.setDuration(300); //in milliseconds
                animation.setInterpolator(new DecelerateInterpolator());
                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (counter > 3) {
                            prgDialog.clearAnimation();
                            dialog_txtCountdown.setText("START");
                            animation.removeUpdateListener(this);

                        }
                    }
                });
                animation.start();


            }

            public void onFinish() {
                dialog.hide();
                listviewMenuBind();
            }
        }.start();
    }

    private void StartStopButtonSource(String btnState) {
        if (btnState.equals("start")) {
            btnStartStop.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_start));
        } else {
            btnStartStop.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_stop));
        }

    }


    @Override
    public Resources getResources() {
        if (mResources == null && VectorEnabledTintResources.shouldBeUsed()) {
            mResources = new VectorEnabledTintResources(this, super.getResources());
        }
        return mResources == null ? super.getResources() : mResources;
    }

    //############################################################################################

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);
        mLocationRequest.setFastestInterval(FASTEST_TIME_BW_UPDATES);
        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        //unRegisterForAlert();
        super.onStop();
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.v("location", location.getLatitude() + " - " + location.getLongitude());
        final LatLng CIU = new LatLng(location.getLatitude(), location.getLongitude());
        float bearing = 0;
        mCurrentLoc = location;
        if (mypos == null) {
            mypos = mMap.addMarker(new MarkerOptions()
                    .position(CIU).title("My Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                    .anchor(0.5f, 0.5f)
                    .rotation(location.getBearing())
                    .flat(true));
            mypos.setZIndex(1.0f);
            searchAnimation.cancel();
            _location_waiting.dismiss();

        } else {
            if (mPreviousLoc != null) {
                bearing = mPreviousLoc.bearingTo(location);
            }
            mypos.setRotation(bearing);
            LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();
            MarkerAnimation.animateMarker(mypos, CIU, latLngInterpolator);
            StoreTripsDelta();
        }
        //Toast.makeText(this, "location-" + location.getLatitude() + " - " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        if (isRecenter) {
            navigateToPoint(CIU, this.tilt, bearing, this.zoom, true);
        }
        checkPointsWithinRadius(location);
        mPreviousLoc = location;
    }


    public void navigateToPoint(LatLng latLng, float tilt, float bearing, float zoom, boolean animate) {
        CameraPosition position =
                new CameraPosition.Builder().target(latLng)
                        .zoom(zoom)
                        .bearing(bearing)
                        .tilt(tilt)
                        .build();

        changeCameraPosition(position, animate);


    }

    public void navigateToPoint(LatLng latLng, boolean animate) {
        CameraPosition position = new CameraPosition.Builder().target(latLng).build();
        changeCameraPosition(position, animate);
    }

    /**
     * Adds a marker to the map.
     */
    public void addMarkerToMap(LatLng latLng, String title, String snippet, String Id) {
        View icon = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custommapicon_1, null);
        TextView numTxt = (TextView) icon.findViewById(R.id.num_txt);
        numTxt.setText(title);

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, icon)))
        );
        addMarkerEntry(Id, marker);

    }

    private void changeCameraPosition(CameraPosition cameraPosition, boolean animate) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        if (animate) {
            mMap.animateCamera(cameraUpdate);
        } else {
            mMap.moveCamera(cameraUpdate);
        }

    }

    private void checkPointsWithinRadius(Location lc) {
        LatLng ml = new LatLng(lc.getLatitude(), lc.getLongitude());

        if (lastLatLongChecked == null) {
            lastLatLongChecked = ml;
        } else {
            double distance = distanceBetween(ml, lastLatLongChecked);
            Toast.makeText(this, distance + "", Toast.LENGTH_SHORT).show();
            if (distance >= getCheck_location_when_my_pos) {
                lastLatLongChecked = ml;
            } else {
                return;
            }
        }
        if (lstcrew == null) return;
        //boolean isfound = false;
        for (int i = 0; i < lstcrew.size(); i++) {
            model_crewdata l = lstcrew.get(i);
            if (l.isNotify > 0 || l.stsi != "0") continue;
            double dist = distanceBetween(ml, new LatLng(Double.parseDouble(l.loc.lat), Double.parseDouble(l.loc.lon)));
            if (dist <= getRedious_user_alert) {
                l.isNotify = 1;
                if (lstNotify == null) this.lstNotify = new ArrayList<>();
                lstNotify.add(l);
                //isfound = true;
            }
            Log.v("distance", l.stdnm + " = " + dist);
        }
        sendForNotification();
        //currently comment
//        if (isfound) {
//            sendForNotification();
//        }
    }

    private void sendForNotification() {
        if (lstNotify == null) return;
        for (int i = 0; i < lstNotify.size(); i++) {
            model_crewdata k = lstNotify.get(i);
            if (k.isNotify == 1) {
                sendInfoToServer(k);
            }
        }
    }


    private void sendInfoToServer(final model_crewdata m) {
        JsonObject json = new JsonObject();
        json.addProperty("tripid", TripId);
        json.addProperty("studid", m.stdid);
        Ion.with(this)
                .load(Global.urls.sendreachingalert.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            m.isNotify = 2;
                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        // menu_refresh.setEnabled(false);

                    }
                });

    }

    public static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }

    //google map related events
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;//get map object after ready
        //check permission
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
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setTrafficEnabled(true);
        addMapListner();
    }


    ///////////////////////////////////////////Alert for reaching

    private static final long POINT_RADIUS = 1000; // in Meters
    private static final long PROX_ALERT_EXPIRATION = 1000 * 60; // It will never expire


    private void setProximityAlert(double lat, double lon, final String tripid, final String studentid) {

        Intent intent = new Intent(ProximityIntentReceiver.PROX_ALERT_INTENT);
        intent.putExtra(ProximityIntentReceiver.TripID_INTENT_EXTRA, tripid);
        intent.putExtra(ProximityIntentReceiver.StudID_INTENT_EXTRA, studentid);
        Integer requestCode = Integer.parseInt(ProximityIntentReceiver.LocationReach + "" + tripid + "" + studentid);
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
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
        locationManager.addProximityAlert(
                lat, // the latitude of the central point of the alert region
                lon, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no                           expiration
                pendingIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );
        IntentFilter filter = new IntentFilter(ProximityIntentReceiver.PROX_ALERT_INTENT);
        registerReceiver(pibrRec, filter);
    }


    private void removeProximityAlerts(final String tripid, final String studentid) {
        Intent intent = new Intent();
        intent.setAction(ProximityIntentReceiver.PROX_ALERT_INTENT);
        Integer requestCode = Integer.parseInt(ProximityIntentReceiver.LocationReach + "" + tripid + "" + studentid);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
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
        locationManager.removeProximityAlert(pendingIntent);
    }


    //Utils
    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    //pub sub socket client


    private void SocketClient() {
        SC_IOApplication app = new SC_IOApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("tripd", onNewMessage);
        mSocket.connect();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isSocConnected) {
                        //if(null!=mUsername)
                        /*Toast.makeText(getApplicationContext(),
                                "Connected", Toast.LENGTH_LONG).show();*/
                        isSocConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.i(TAG, "diconnected");
                    isSocConnected = false;
                    /*Toast.makeText(getApplicationContext(),
                            "Disconnect", Toast.LENGTH_LONG).show();*/
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Log.e(TAG, "Error connecting");
                    /*Toast.makeText(getApplicationContext(),
                            "Unable to connect server!", Toast.LENGTH_LONG).show();*/
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;*/
                    try {
                       /* username = data.getString("username");
                        message = data.getString("message");*/
                        Toast.makeText(getApplicationContext(),
                                args[0].toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //Log.e(TAG, e.getMessage());
                        return;
                    }
                }
            });
        }
    };


    //###############################################Animator Class////////////////////////

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    //############################################################################################

    //event of form
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Checker(this).pass(new Checker.Pass() {
            @Override
            public void pass() {
                //LocationManagerSettings();
                if (mGoogleApiClient.isConnected()) {
                    startLocationUpdates();
                }
            }
        }).check(Checker.Resource.GPS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mSocket.disconnect();
//        mSocket.off(Socket.EVENT_CONNECT, onConnect);
//        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
//        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.off("tripd", onNewMessage);
        //unRegisterForAlert();
        mGoogleApiClient.disconnect();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isstarted) {
            exitWithConfirm();
        } else {
            pickupcrew.this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }

        if (searchAnimation != null) {
            searchAnimation.cancel();
        }

        if (_location_waiting.isShowing()) {
            _location_waiting.dismiss();
        }
        if (isstarted) {
            exitWithConfirm();
        } else {
            pickupcrew.this.finish();
        }
    }

    private void exitWithConfirm() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.pickup_exit))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.alert_yes_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pickupcrew.this.finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.alert_no_text), null)
                .show();
    }


}