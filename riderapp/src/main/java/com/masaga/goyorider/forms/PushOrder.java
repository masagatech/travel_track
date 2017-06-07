package com.masaga.goyorider.forms;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.R;
import com.masaga.goyorider.adapters.ComplatedOrderAdapter;
import com.masaga.goyorider.adapters.PushOrderAdapter;

import com.masaga.goyorider.adapters.RiderListAdapter;
import com.masaga.goyorider.adapters.pending_order_adapter;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.model.model_pending;
import com.masaga.goyorider.model.model_push_order;
import com.masaga.goyorider.model.model_rider_list;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.Toast;

import java.util.logging.LogRecord;

public class PushOrder extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private com.masaga.goyorider.adapters.PushOrderAdapter mTimeLineAdapter;
    private List<model_push_order> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = true;

        setTitle(getResources().getString(R.string.Push_Order));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

//        initView();
        loader = new ProgressDialog(this);
        loader.setCancelable(false);
        loader.setMessage("Please wait..");
        loader.show();

        JsonObject json = new JsonObject();
        json.addProperty("flag", "dashboard");
        json.addProperty("status", "0");

//        Global.showProgress(loader);
        Ion.with(this)
                .load(Global.urls.getOrderDash.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<model_push_order>>() {
                            }.getType();
                            List<model_push_order> events = (List<model_push_order>) gson.fromJson(result.get("data").getAsJsonArray().get(0), listType);
                            bindCurrentTrips(events);
                            loader.hide();
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

//    private void initView() {
//        setDataListItems();
//        mTimeLineAdapter = new PushOrderAdapter(mDataList, mOrientation, mWithLinePadding);
//        mRecyclerView.setAdapter(mTimeLineAdapter);
//    }

//    private void setDataListItems(){
//        mDataList.add(new model_push_order("#198" , "01:29 PM", "Pizza Hut, Pralhad Nagar", "Navi Mumbai,sector 15, Raj Legacy Socity"));
//        mDataList.add(new model_push_order("#199" , "01:30 PM", "Pizza Hut, Navi Mumbai", "Navi Mumbai,sector 1, Raj Legacy Socity"));
//        mDataList.add(new model_push_order("#200" , "01:20 PM", "Pizza Hut, West Vikroli", "West Vikroli,sector 17, Raj Legacy Socity"));
//        mDataList.add(new model_push_order("#201" , "12:00 PM", "Pizza Hut, Gandhi Nagar", "Bandup,sector 18, Raj Legacy Socity"));
//        mDataList.add(new model_push_order("#202" , "12:32 PM", "Pizza Hut, Pralhad Nagar", "Powai,sector 30, Raj Legacy Socity"));
//
//    }

    private void bindCurrentTrips(List<model_push_order> lst) {
        if (lst.size() > 0) {
            findViewById(R.id.txtNodata).setVisibility(View.GONE);
            mTimeLineAdapter = new PushOrderAdapter(lst, mOrientation, mWithLinePadding);
            mRecyclerView.setAdapter(mTimeLineAdapter);
            mTimeLineAdapter.notifyDataSetChanged();
        } else {
            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
        }
    }

//    public void onClickRider(View view) {
//        final ArrayList<model_rider_list> data = populateList();//init() just get some data
//
//        RiderListAdapter adapter = new RiderListAdapter(this, data);
//
//        View v = PushOrder.this.getLayoutInflater().inflate(R.layout.rider_list, null, true);
//
//        ListView list = (ListView) v.findViewById(R.id.listView1);
//        list.setAdapter(adapter);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                String Slecteditem = data.get(position).RiderName;
//                Toast.makeText(PushOrder.this, Slecteditem, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        final Dialog dialogOut = new Dialog(PushOrder.this);
//
//        dialogOut.setContentView(list);
//        dialogOut.setCancelable(true);
//        dialogOut.setTitle("Riders");
////        dialogOut.buNeutralButton("Voltar",
////                new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialogOut.dismiss();
////                    }
////                });
//
//        dialogOut.show();
//    }
//
//    public static ArrayList<model_rider_list> populateList(){
//        ArrayList<model_rider_list> mRiderList = new ArrayList<model_rider_list>();
//        mRiderList.add(new model_rider_list("Select a Rider", 0));
//        mRiderList.add(new model_rider_list("Rider 1", 308));
//        mRiderList.add(new model_rider_list("Rider 2", 948));
//        mRiderList.add(new model_rider_list("Rider 3", 340));
//        mRiderList.add(new model_rider_list("Rider 3", 340));
//        mRiderList.add(new model_rider_list("Rider 3", 340));
//        mRiderList.add(new model_rider_list("Rider 3", 340));
//        mRiderList.add(new model_rider_list("Rider 3", 340));
//        mRiderList.add(new model_rider_list("Rider 3", 340));
//        mRiderList.add(new model_rider_list("Rider 1", 308));
//        mRiderList.add(new model_rider_list("Rider 2", 948));
//        mRiderList.add(new model_rider_list("Rider 3", 340));
//
//        return mRiderList;
//    }

    public static ArrayList<model_rider_list> populateList(){
        ArrayList<model_rider_list> mRiderList = new ArrayList<model_rider_list>();
        mRiderList.add(new model_rider_list("Select a Rider", 0,"","",""));
        mRiderList.add(new model_rider_list("Rider 1", 308,"9946253286","25KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 2", 948,"994685586","22KM away","28%"));
        mRiderList.add(new model_rider_list("Rider 3", 340,"9946253286","25KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 3", 340,"992586286","756KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 3", 340,"9946253286","25KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 3", 340,"9946253286","25KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 3", 340,"9946253286","82KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 3", 340,"9946253286","25KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 1", 308,"9946253286","25KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 2", 948,"9946253286","23KM away","26%"));
        mRiderList.add(new model_rider_list("Rider 3", 340,"9946253286","25KM away","26%"));

        return mRiderList;
    }

//
//    private JsonObject getRiders() {
//        String jsond = "{\n" +
//                "  \"data\" : [\n" +
//                "      {\"id\" : 1, \"nm\" : \"Rider Rider 1\" },\n" +
//                "      {\"id\" : 2, \"nm\" : \"Rider Rider 2\" },\n" +
//                "      {\"id\" : 3, \"nm\" : \"Rider Rider 3\" },\n" +
//                "      {\"id\" : 4, \"nm\" : \"Rider Rider 4\" },\n" +
//                "      {\"id\" : 5, \"nm\" : \"Rider Rider 5\" },\n" +
//                "      {\"id\" : 6, \"nm\" : \"Rider Rider 6\" },\n" +
//                "      {\"id\" : 7, \"nm\" : \"Rider Rider 7\" },\n" +
//                "      {\"id\" : 8, \"nm\" : \"Rider Rider 8\" },\n" +
//                "      {\"id\" : 9, \"nm\" : \"Rider Rider 9\" },\n" +
//                "      {\"id\" : 10, \"nm\" : \"Rider Rider 10\" },\n" +
//                "      {\"id\" : 11, \"nm\" : \"Rider Rider 11\" },\n" +
//                "      {\"id\" : 12, \"nm\" : \"Rider Rider 12\" },\n" +
//                "      {\"id\" : 13, \"nm\" : \"Rider Rider 13\" },\n" +
//                "      {\"id\" : 14, \"nm\" : \"Rider Rider 14\" },\n" +
//                "      {\"id\" : 15, \"nm\" : \"Rider Rider 15\" },\n" +
//                "      {\"id\" : 16, \"nm\" : \"Rider Rider 16\" },\n" +
//                "      {\"id\" : 17, \"nm\" : \"Rider Rider 17\" }\n" +
//                "    ]\n" +
//                "}";
//        return  new JsonParser().parse(jsond).getAsJsonObject();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimeLineAdapter!=null){
            mTimeLineAdapter.Kill();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mTimeLineAdapter!=null){
            mTimeLineAdapter.Kill();
        }
    }
}
