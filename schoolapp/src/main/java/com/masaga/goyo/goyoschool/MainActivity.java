package com.masaga.goyo.goyoschool;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyo.R;
import com.masaga.goyo.adapters.dashboard_menuAdapter;
import com.masaga.goyo.gloabls.Global;
import com.masaga.goyo.initials.login;
import com.masaga.goyo.model.model_appsettings;
import com.masaga.goyo.model.model_loginusr;
import com.masaga.goyo.utils.SHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* Variable Declarations */
    //Buttons
    Button btnDriverInfo, btnDriverList, btnMyTrip;
    final List<HashMap<String, String>> mDashboard = new ArrayList<>();
    GridView grid_dashboardmenu;
    private Boolean isConnected = true;
    private ProgressDialog loader;
    private static final String TAG = "MainActivity";
    /*Data*/
    String[] gridViewString = {"", ""};
    int[] gridViewImageId = {
            R.drawable.dashboard_menu_trips,
            R.drawable.dashboard_menu_settings

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getResources().getString(R.string.dashboard_title));
        //Initialize Id's
        initialize();
        fillSettings();
        //add click
        addClickListners();

        //check allpermisstions
        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
       /* GPSLocation l =new GPSLocation(this.getApplicationContext());
        Location loc = l.getLocation();
        String lat = "";
        String lon = "";
        if(loc != null){
            lat = "" + loc.getLatitude();
            lon ="" + loc.getLongitude();
        }
        upload o = new upload();
        o.checkServerDataDriverInfo(getApplicationContext());*/

        //Log.v("Tokened :: ", FirebaseInstanceId.getInstance().getToken());
    }

    private void initialize() {
//        btnDriverInfo = (Button)this.findViewById(R.id.btnDriverInfo);
//        btnDriverList = (Button)this.findViewById(R.id.btnDriverList);
        loader = new ProgressDialog(MainActivity.this);
        grid_dashboardmenu = (GridView) this.findViewById(R.id.grid_dashboardmenu);
        bindMenu();
    }

    private void addClickListners() {

        //btn Driver Info click
//        btnDriverInfo.setOnClickListener(this);
//        btnDriverList.setOnClickListener(this);

        grid_dashboardmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Intent in;
                switch (i) {
                    case 0:
                        in = new Intent(MainActivity.this, com.masaga.goyo.forms.my_trip.class);
                        startActivity(in);
                        break;
                    case 1:
                        //in = new Intent(MainActivity.this, com.masaga.goyo.settings.settings.class);
                        in = new Intent(MainActivity.this, com.masaga.goyo.customer.clnt_mykids.class);
                        startActivity(in);
                        break;
                }

                //Toast.makeText(GridViewImageTextActivity.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
            }
        });

    }

    private void bindMenu() {
        gridViewString[0] = getResources().getString(R.string.dashboard_menu_trips);
        gridViewString[1] = getResources().getString(R.string.dashboard_menu_settings);

        dashboard_menuAdapter adapterViewAndroid = new dashboard_menuAdapter(this, gridViewString, gridViewImageId);
        grid_dashboardmenu = (GridView) findViewById(R.id.grid_dashboardmenu);
        grid_dashboardmenu.setAdapter(adapterViewAndroid);


    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
//            case R.id.btnDriverInfo:
//                i = new Intent(MainActivity.this, com.masaga.goyo.forms.driver_info.class);
//                startActivity(i);
//                break;
//            case R.id.btnDriverList:
//                i = new Intent(MainActivity.this, com.masaga.goyo.forms.driver_info_view.class);
//                startActivity(i);
//                break;
          /*  case R.id.btnMyTrip:
                i = new Intent(MainActivity.this, com.masaga.goyo.forms.my_trip.class);
                startActivity(i);
                break;*/
            default:
                break;
        }

    }


    //set action bar button menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }


    //action bar menu button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_user:

                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.confirm_logout))
                        .setPositiveButton(R.string.alert_ok_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String sessionid = SHP.get(MainActivity.this, SHP.ids.sessionid, "-1").toString();
                                String uid = SHP.get(MainActivity.this, SHP.ids.uid, "-1").toString();
                                JsonObject json = new JsonObject();
                                json.addProperty("sessionid", sessionid);
                                json.addProperty("email", uid);
                                Global.showProgress(loader);
                                Ion.with(MainActivity.this)
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
                                                            SHP.set(MainActivity.this, SHP.ids.uid, "");
                                                            SHP.set(MainActivity.this, SHP.ids.sessionid, "");
                                                            Intent i = new Intent(MainActivity.this, com.masaga.goyo.initials.login.class);
                                                            startActivity(i);
                                                            MainActivity.this.finish();
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "Faild to logout " + m.getErrcode() + " " + m.getErrmsg(), Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Oops there is some issue! please logout later!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception ea) {
                                                    Toast.makeText(MainActivity.this, "Oops there is some issue! Error: " + ea.getMessage(), Toast.LENGTH_LONG).show();
                                                    ea.printStackTrace();
                                                }
                                                Global.hideProgress(loader);
                                            }
                                        });

                            }
                        })
                        .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_lock_lock).show();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    // on back press for exit
    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
            System.exit(0);
        } else {
            Toast.makeText(this, getResources().getString(R.string.presagain_exit),
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void fillDashboardMenu() {
        String jsond = "{\"data\":[{\"icon\":\"ewad\",\"name\":\"My Trips\"}]}";
        try {
            JSONObject jsnobject = new JSONObject(jsond);
            JSONArray _data = jsnobject.getJSONArray("data");
            for (int i = 0; i <= _data.length() - 1; i++) {
                HashMap<String, String> h = new HashMap();
                JSONObject o = _data.getJSONObject(i);
                h.put("id", o.get("id").toString());
                h.put("nm", o.get("nm").toString());
                h.put("date", o.get("date").toString());
                h.put("time", o.get("time").toString());
                h.put("pd", o.get("pd").toString());
                h.put("sts", o.get("sts").toString());
                mDashboard.add(h);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void fillSettings() {
        Global.appsettings = new model_appsettings();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
