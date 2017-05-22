package com.masaga.goyorider.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.adapters.clnt_mykids_listAdapter;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.R;
import com.masaga.goyorider.model.model_mykids;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

public class clnt_mykids extends AppCompatActivity {

    //variable
    ListView lstmykids;
    private ProgressDialog loader;
    private List<model_mykids> lstmykidsd;
    private model_mykids mykid;
    MenuItem menu_refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clnt_mykids);
        setTitle("My Kids");
        initUI();
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstmykids = (ListView) findViewById(R.id.lstmykids);
        loader = new ProgressDialog(this);
        addListners();
        bindListView();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu_refresh = menu.findItem(R.id.menu_refresh);
        return true;
    }

    //set action bar button menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todaystrips_activity, menu);
        return true;
    }

    //action bar menu button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                bindListView();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void addListners(){
        lstmykids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mykid = ((model_mykids) parent.getItemAtPosition(position));
                Intent in = new Intent(clnt_mykids.this, clnt_tripview.class);
                in.putExtra("tripid",mykid.tripid);
                in.putExtra("status",mykid.stsi);
                startActivity(in);
            }
        });
    }

    private void bindListView() {
        JsonObject json = new JsonObject();
        //json.addProperty("uid", Global.loginusr.getUid());
        Global.showProgress(loader);
        Ion.with(this)
                .load(Global.urls.getmykids.value)
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
                            Type listType = new TypeToken<List<model_mykids>>() {
                            }.getType();
                            JsonElement k = result.get("data");
                            lstmykidsd = (List<model_mykids>) gson.fromJson(result.get("data"), listType);
                            bindCreawData(lstmykidsd);
                            //addCrewLocationToMap();

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }

                        Global.hideProgress(loader);
                    }
                });
    }

    Comparator<model_mykids > comperator = new Comparator<model_mykids >()
    {
        @Override
        public int compare(model_mykids s1, model_mykids s2) {
            return s1.btch.compareToIgnoreCase(s2.btch) & (s1.pd.compareToIgnoreCase(s2.pd)) ;
        }
    };


    clnt_mykids_listAdapter _clnt_mykids_listAdapter;
    List<model_mykids> _mykidlst;

    private void bindCreawData(List<model_mykids> lst) {
        if (lst.size() > 0) {
            _mykidlst = lst;
            findViewById(R.id.txtNodata).setVisibility(View.GONE);
            _clnt_mykids_listAdapter = new clnt_mykids_listAdapter(this, lst, getResources());
            lstmykids.setAdapter(_clnt_mykids_listAdapter);
            notifyCrewChanges();

        } else {
            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
        }
    }

    private void notifyCrewChanges() {
        if (_clnt_mykids_listAdapter != null) {
            /*Collections.sort(_crewlst, new Comparator<model_crewdata>() {
                public int compare(model_crewdata o1, model_crewdata o2) {
                    return o1.stsi.compareToIgnoreCase(o2.stsi);
                }
            });*/
            _clnt_mykids_listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
