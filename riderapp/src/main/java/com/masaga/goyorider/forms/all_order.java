package com.masaga.goyorider.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.R;
import com.masaga.goyorider.adapters.AllOrdersAdapter;
import com.masaga.goyorider.adapters.ComplatedOrderAdapter;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.model.model_pending;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class all_order extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private com.masaga.goyorider.adapters.AllOrdersAdapter mTimeLineAdapter;
    private List<PendingModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = true;

        setTitle(getResources().getString(R.string.All_Order));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
//
     //   initView();


        JsonObject json = new JsonObject();
        json.addProperty("flag", "details");
        json.addProperty("status", "pending");

//        Global.showProgress(loader);
        Ion.with(this)
                .load(Global.urls.getOrderDetails.value)
                .setJsonObjectBody(json)
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
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });
    }

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void initView() {
//        setDataListItems();
//        mTimeLineAdapter = new AllOrdersAdapter(mDataList, mOrientation, mWithLinePadding);
//        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems(){
//        mDataList.add(new PendingModel("#198" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.ACTIVE,0.00));
//        mDataList.add(new PendingModel("#199" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,0.00));
//        mDataList.add(new PendingModel("#200" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,209.00));
//        mDataList.add(new PendingModel("#201" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,349.00));
//        mDataList.add(new PendingModel("#202" , "Pizza Hut, Pralhad Nagar", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,460.50));
//        mDataList.add(new PendingModel("#1942", "Pizza Hut, Navi Mumbai", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,822.00));
//        mDataList.add(new PendingModel("#1923", "Pizza Hut, Navi Mumbai", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 190.00));
//        mDataList.add(new PendingModel("#1923", "Pizza Hut, Navi Mumbai", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 190.00));
//        mDataList.add(new PendingModel("#1923", "Pizza Hut, Navi Mumbai", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 190.00));
//        mDataList.add(new PendingModel("#1923", "Pizza Hut, Navi Mumbai", "Time : 08.00 " ,"Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED ,190.00));
//        mDataList.add(new PendingModel("#1925", "Pizza Hut, Navi Mumbai", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,100.00));
//        mDataList.add(new PendingModel("#1963", "Pizza Hut, Navi Mumbai", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,722.00));
//        mDataList.add(new PendingModel("#1996", "Pizza Hut, Navi Mumbai", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,22.00));
//        mDataList.add(new PendingModel( "#198", "Pizza Hut, West Vikroli","Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,99.00));
//        mDataList.add(new PendingModel( "#198", "Pizza Hut, West Vikroli","Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,33.00));
//        mDataList.add(new PendingModel( "#198", "Pizza Hut, West Vikroli","Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,1110.00));
//        mDataList.add(new PendingModel( "#1986","Pizza Hut, West Vikroli","Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 122.00));
//        mDataList.add(new PendingModel( "#1985","Pizza Hut, West Vikroli","Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 140.00));
//        mDataList.add(new PendingModel( "#1983","Pizza Hut, West Vikroli","Time : 08.00 ", "Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,422.00));
//        mDataList.add(new PendingModel( "#1925","Pizza Hut, West Vikroli","Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 122.00));
//       mDataList.add(new PendingModel("#1942", "Pizza Hut, Thane", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 822.00));
//       mDataList.add(new PendingModel("#1923", "Pizza Hut, Thane", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 100.00));
//       mDataList.add(new PendingModel("#1923", "Pizza Hut, Thane", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 722.00));
//       mDataList.add(new PendingModel("#1923", "Pizza Hut, East Vikroli", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,33.00));
//        mDataList.add(new PendingModel("#1923", "Pizza Hut, East Vikroli","Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED, 1110.00));
//        mDataList.add(new PendingModel("#1925", "Pizza Hut, East Vikroli","Time : 08.00 ", "Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,1110.00));
//       mDataList.add(new PendingModel("#1963", "Pizza Hut, East Vikroli", "Time : 08.00 ","Navi Mumbai,sector 15", currentDateTimeString, OrderStatus.COMPLETED,1110.00));

    }

    private void bindCurrentTrips(List<model_pending> lst) {
        if (lst.size() > 0) {
            findViewById(R.id.txtNodata).setVisibility(View.GONE);
            mTimeLineAdapter = new AllOrdersAdapter(lst, mOrientation, mWithLinePadding);
            mRecyclerView.setAdapter(mTimeLineAdapter);
            mTimeLineAdapter.notifyDataSetChanged();

        } else {
            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
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