package com.travel.tracker.forms;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.travel.tracker.R;
import com.travel.tracker.adapters.OrderDetailsAdapter;
import com.travel.tracker.gloabls.Global;
import com.travel.tracker.model.model_order_details;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.travel.tracker.gloabls.Global.urls.getOrdersCount;

public class AllOrderDetails extends AppCompatActivity {
    private ArrayList<model_order_details> data;
    ListView AllDetails;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order_details);

        if(getSupportActionBar()!=null)
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        data= populateList();
        AllDetails=(ListView)findViewById(R.id.all_details);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_details, AllDetails,
                false);
        AllDetails.addHeaderView(header, null, false);

//        OrderDetailsAdapter orderDetailsAdapter=new OrderDetailsAdapter(data,this);
//        AllDetails.setAdapter(orderDetailsAdapter);
//        orderDetailsAdapter.notifyDataSetChanged();


        loader = new ProgressDialog(this);
        loader.setCancelable(false);
        loader.setMessage("Please wait..");
        loader.show();


        Ion.with(this)
                .load("GET", getOrdersCount.value)
                .addQuery("flag", "month")
                .addQuery("rdid", Global.loginusr.getDriverid() + "")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<model_order_details>>() {
                            }.getType();
                            ArrayList<model_order_details> events = (ArrayList<model_order_details>) gson.fromJson(result.get("data"), listType);
                            bindCurrentTrips(events);

                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        loader.hide();
                    }
                });






    }

    private void bindCurrentTrips(ArrayList<model_order_details> lst) {
        if (lst.size() > 0) {
            findViewById(R.id.txtNodata).setVisibility(View.GONE);

            OrderDetailsAdapter orderDetailsAdapter=new OrderDetailsAdapter(lst,this);
            AllDetails.setAdapter(orderDetailsAdapter);
            orderDetailsAdapter.notifyDataSetChanged();
        } else {
            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
        }
    }
//
//    public static ArrayList<model_order_details> populateList(){
//        ArrayList<model_order_details> mRiderList = new ArrayList<model_order_details>();
//        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
//        mRiderList.add(new model_order_details("13-5-17",1,0,9,10));
//        mRiderList.add(new model_order_details("14-5-17",2,2,9,12));
//        mRiderList.add(new model_order_details("15-5-17",6,0,4,10));
//        mRiderList.add(new model_order_details("16-5-17",4,3,9,13));
//        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
//        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
//        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
//        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
//
//        return mRiderList;
//    }
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
}
