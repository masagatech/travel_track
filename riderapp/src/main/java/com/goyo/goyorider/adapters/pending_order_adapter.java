package com.goyo.goyorider.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.goyo.goyorider.forms.dashboard;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.goyo.goyorider.R;
import com.goyo.goyorider.forms.OrderStatus;
import com.goyo.goyorider.forms.Orientation;
import com.goyo.goyorider.forms.PendingOrdersView;
import com.goyo.goyorider.forms.pending_order;
import com.goyo.goyorider.gloabls.Global;
import com.goyo.goyorider.model.model_pending;
import com.goyo.goyorider.utils.VectorDrawableUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static com.goyo.goyorider.R.drawable.cash;
import static com.goyo.goyorider.Service.RiderStatus.Rider_Lat;
import static com.goyo.goyorider.Service.RiderStatus.Rider_Long;
import static com.goyo.goyorider.forms.pending_order.TripId;
import static com.goyo.goyorider.gloabls.Global.urls.setTripAction;

/**
 * Created by fajar on 22-May-17.
 */

public class pending_order_adapter extends RecyclerView.Adapter<pending_order_viewHolder> {

    private List<model_pending> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    private LayoutInflater mLayoutInflater;
    public String tripid = "0";
    private ProgressDialog loader;
    private Spinner RejectSpinner;
    private Double CashCollected;
    List<String> RejectList;
    String SelectedReason;


    public pending_order_adapter(List<model_pending> feedList, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
    }
    @Override
    public int getItemViewType(int position) {
        return PendingOrdersView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public pending_order_viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        view = mLayoutInflater.inflate(R.layout.pending_order_timeline, parent, false);

        return new pending_order_viewHolder(view, viewType);
    }

    public void updateTripId(String Tripid){
        tripid = Tripid;
//        for (int i=0; i<=mFeedList.size() -1 ; i ++){
//            mFeedList.get(i).tripid = Tripid;
//        }
    }

    @Override
    public void onBindViewHolder(final pending_order_viewHolder holder, final int position) {

        final model_pending timeLineModel = mFeedList.get(position);

        if(timeLineModel.status == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.status == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorAccent));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorAccent));
        }
//        holder.mDate.setText(currentDateTimeString);
        holder.mOrder.setText(timeLineModel.ordno +"");
        holder.mMarchant.setText(timeLineModel.olnm);
        holder.Custmer_name.setText(timeLineModel.custname);
      //  holder.mDeliver_at.setText(timeLineModel.custmob+"\n"+ timeLineModel.custaddr+"\n");
        holder.mDeliver_at.setText(timeLineModel.custaddr+"\n");
        holder.Remark.setText("Remark: "+timeLineModel.remark);
        holder.mTime.setText(timeLineModel.deltime);
        holder.mDate.setText(timeLineModel.dltm + "");
        holder.collected_cash.setText(+timeLineModel.amtcollect +"");
        final int newPosition = holder.getAdapterPosition();


        holder.Btn_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone(timeLineModel.custmob);
            }
        });
//
//        holder.ArrowRemark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.Remark.getVisibility()==INVISIBLE){
//                holder.Remark.setVisibility(View.VISIBLE);
//                }
//                else {
//                    holder.Remark.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        holder.Btn_Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashCollected= Double.valueOf(holder.collected_cash.getText().toString());
                loader = new ProgressDialog(mContext);
                loader.setCancelable(false);
                loader.setMessage("Please wait..");
                loader.show();

                Deliver(timeLineModel,position,newPosition,CashCollected);


//                    final model_pending status = mFeedList.get(mFeedList.size() == position +1 ? position :position+1);
//                    status.status=(OrderStatus.ACTIVE);
//
//
//               // complated_order.mDataList.add(new PendingModel(timeLineModel.getmOrder(),timeLineModel.getmMarchant(),timeLineModel.getmTime(),timeLineModel.getmDeliver_at(),currentDateTimeString,OrderStatus.ACTIVE,timeLineModel.getCash()));
//                mFeedList.remove(newPosition);
//                notifyItemRemoved(newPosition);
//                notifyItemRangeChanged(newPosition,mFeedList.size());
            }
        });

        holder.Btn_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.reject_reason);
                dialog.setCancelable(true);

               RejectSpinner = (Spinner) dialog.findViewById(R.id.rej_list_spinner);
                Button Cancel = (Button) dialog.findViewById(R.id.cancel);
                Button Return = (Button) dialog.findViewById(R.id.returne);

//                List<String> categories = new ArrayList<String>();
//                categories.add("Automobile");
//                categories.add("Business Services");
//                categories.add("Computers");
//                categories.add("Education");
//                categories.add("Personal");
//                categories.add("Travel");

//                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categories);
//                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                RejectSpinner.setAdapter(dataAdapter);
//                RejectSpinner.setAdapter(adapter);
                GetRejectReason();
                RejectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        SelectedReason=RejectList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
               Cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                Return.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        loader = new ProgressDialog(mContext);
                        loader.setCancelable(false);
                        loader.setMessage("Please wait..");
                        loader.show();
                        dialog.dismiss();
                        Return(timeLineModel,position,newPosition,SelectedReason);

                    }
                });
                dialog.show();






//                TextView tx;
//                String[] s = { "India ", "Arica", "India ", "Arica", "India ", "Arica",
//                        "India ", "Arica", "India ", "Arica" };
//
//                final ArrayAdapter<String> adp = new ArrayAdapter<String>(mContext,
//                        android.R.layout.simple_spinner_item, s);
//
//                tx= (TextView)findViewById(R.id.txt1);
//                final Spinner sp = new Spinner(WvActivity.this);
//                sp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//                sp.setAdapter(adp);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(WvActivity.this);
//                builder.setView(sp);
//                builder.create().show();
//            }
//

//
//                View alertView = mContext.getLayoutInflater().inflate(R.layout.reject_reason, null);
//
//                List<String> spinnerArray =  new ArrayList<String>();
//                spinnerArray.add("item1");
//                spinnerArray.add("item2");
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                        mContext, android.R.layout.simple_spinner_item, spinnerArray);
//
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                Spinner sItems = (Spinner).findViewById(R.id.);
//                sItems.setAdapter(adapter);





//                final EditText edittext = new EditText(mContext);
//                edittext.setMaxLines(100);
//                new AlertDialog.Builder(mContext)
//                        .setTitle("Return")
//                        .setMessage("Are you sure you want Return Delivery ? \n\nTell us why?")
//                        .setView(edittext)
//                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                //Getting rider feed back
//                                String feedabck = edittext.getText().toString();
//                                loader = new ProgressDialog(mContext);
//                                loader.setCancelable(false);
//                                loader.setMessage("Please wait..");
//                                loader.show();
//                                Return(timeLineModel,position,newPosition,feedabck);
//                            }
//                        })
//                        .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setIcon(R.drawable.stop_trip).show();

            }
        });


    }

    private void AutoStop(){
            if(mFeedList.size()==1){
                new AlertDialog.Builder(mContext)
                        .setTitle("Stop Trip")
                        .setMessage("No Pending Delivery. Are you want Stop Trip?")
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

    public void stopTrip(){

        JsonObject json = new JsonObject();
        json.addProperty("flag", "stop");
        json.addProperty("loc", Rider_Lat+","+Rider_Long);

        json.addProperty("tripid", TripId);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");

        Ion.with(mContext)
                .load(setTripAction.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            if(result.get("data").getAsJsonObject().get("status").getAsBoolean()){
                                Toast.makeText(mContext,result.get("data").getAsJsonObject().get("msg").toString()
                                        ,Toast.LENGTH_SHORT).show();
                                //StartRide.setVisibility(View.GONE);
//                               mContext.onBackPressed();
                                Intent intent=new Intent(mContext,dashboard.class);
                                mContext.startActivity(intent);
                            }
                            else{
                                Toast.makeText(mContext,result.get("data").getAsJsonObject().get("msg").toString()
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });

    }


    private void GetRejectReason(){
        loader = new ProgressDialog(mContext);
        loader.setCancelable(false);
        loader.setMessage("Please wait..");
        loader.show();

        JsonObject json = new JsonObject();
        json.addProperty("flag", "");
        json.addProperty("group", "rejectreason");

//        Global.showProgress(loader);
        Ion.with(mContext)
                .load(Global.urls.getMOM.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
//                            Gson gson = new Gson();
//                            Type listType = new TypeToken<List<String>>() {
//                            }.getType();
                            RejectList = new ArrayList<String>();
                            for(int i=0;i<result.get("data").getAsJsonArray().size();i++){
                                RejectList.add(result.get("data").getAsJsonArray().get(i).getAsJsonObject().get("val").getAsString());
                            }
                            bindCurrentTrips(RejectList);

                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        loader.hide();
                    }
                });

    }

    private void bindCurrentTrips(List<String> lst) {
        if (lst.size() > 0) {

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, lst);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            RejectSpinner.setAdapter(dataAdapter);

//            findViewById(R.id.txtNodata).setVisibility(View.GONE);
//            mTimeLineAdapter = new PushOrderAdapter(lst, mOrientation, mWithLinePadding);
//            mRecyclerView.setAdapter(mTimeLineAdapter);
//            mTimeLineAdapter.notifyDataSetChanged();
        } else {
//            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
        }
    }

    private void dialContactPhone(final String phoneNumber) {
        mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void Deliver(final model_pending timeLineModel, final int position, final int newPosition,double Cash){


        JsonObject json = new JsonObject();
        json.addProperty("flag", "delvr");
        json.addProperty("loc", Rider_Lat+","+Rider_Long);
        json.addProperty("tripid", tripid);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");
        json.addProperty("amtrec", timeLineModel.amtcollect + "");   //        Cash
        json.addProperty("ordid", timeLineModel.ordid + "");
        json.addProperty("orddid", timeLineModel.orderdetailid + "");
        json.addProperty("remark", timeLineModel.remark);


        Ion.with(mContext)
                .load(setTripAction.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            if(result.get("data").getAsJsonObject().get("status").getAsBoolean()){
//                                if(timeLineModel.tripid.equals("0")){
//                                    Toast.makeText(mContext, result.get("data").getAsJsonObject().get("msg").toString()
//                                            , Toast.LENGTH_SHORT).show();
//
//                                }else {
                                    final model_pending status = mFeedList.get(mFeedList.size() == position + 1 ? position : position + 1);
                                    status.status = (OrderStatus.ACTIVE);
                                    AutoStop();
                                    mFeedList.remove(newPosition);
                                    notifyItemRemoved(newPosition);
                                    notifyItemRangeChanged(newPosition, mFeedList.size());

                                    Toast.makeText(mContext, result.get("data").getAsJsonObject().get("msg").toString()
                                            , Toast.LENGTH_SHORT).show();
                                }
//                            }
                            else{
                                Toast.makeText(mContext,result.get("data").getAsJsonObject().get("msg").toString()
                                        ,Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        loader.hide();
                    }
                });

    }

    private void Return(final model_pending timeLineModel, final int position, final int newPosition,String feedback){

        if(feedback==null){
            feedback="";
        }

        JsonObject json = new JsonObject();
        json.addProperty("flag", "retn");
        json.addProperty("loc", Rider_Lat+","+Rider_Long);
        json.addProperty("tripid", tripid);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");
        json.addProperty("amtrec", timeLineModel.amtcollect + "");
        json.addProperty("ordid", timeLineModel.ordid + "");
        json.addProperty("orddid", timeLineModel.orderdetailid + "");
        json.addProperty("remark", feedback);


        Ion.with(mContext)
                .load(setTripAction.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            if(result.get("data").getAsJsonObject().get("status").getAsBoolean()){
//                                if(timeLineModel.tripid.equals("0")){
//                                    Toast.makeText(mContext, result.get("data").getAsJsonObject().get("msg").toString()
//                                            , Toast.LENGTH_SHORT).show();
//
//                                }else {
                                    final model_pending status = mFeedList.get(mFeedList.size() == position + 1 ? position : position + 1);
                                    status.status = (OrderStatus.ACTIVE);
                                     AutoStop();
                                    mFeedList.remove(newPosition);
                                    notifyItemRemoved(newPosition);
                                    notifyItemRangeChanged(newPosition, mFeedList.size());
                                    Toast.makeText(mContext, result.get("data").getAsJsonObject().get("msg").toString()
                                            , Toast.LENGTH_SHORT).show();
//                                }
                            }
                            else{
                                Toast.makeText(mContext,result.get("data").getAsJsonObject().get("msg").toString()
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ea) {
                            ea.printStackTrace();
                        }
                        loader.hide();
                    }
                });

    }


    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }


}
