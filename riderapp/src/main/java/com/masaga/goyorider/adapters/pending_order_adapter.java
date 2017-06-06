package com.masaga.goyorider.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.R;
import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.Orientation;
import com.masaga.goyorider.forms.PendingModel;
import com.masaga.goyorider.forms.PendingOrdersView;
import com.masaga.goyorider.forms.complated_order;
import com.masaga.goyorider.forms.dashboard;
import com.masaga.goyorider.forms.pending_order;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.model.model_pending;
import com.masaga.goyorider.utils.VectorDrawableUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.data;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static com.masaga.goyorider.Service.RiderStatus.Rider_Lat;
import static com.masaga.goyorider.Service.RiderStatus.Rider_Long;
import static com.masaga.goyorider.forms.PendingOrdersView.getTimeLineViewType;
import static com.masaga.goyorider.gloabls.Global.urls.setTripAction;

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

        for (int i=0; i<=mFeedList.size() -1 ; i ++){
            mFeedList.get(i).tripid = Tripid;
        }
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
        holder.mDate.setText(currentDateTimeString);
        holder.mOrder.setText(timeLineModel.ordno +"");
        holder.mMarchant.setText(timeLineModel.olnm);
        holder.Custmer_name.setText(timeLineModel.custname);
      //  holder.mDeliver_at.setText(timeLineModel.custmob+"\n"+ timeLineModel.custaddr+"\n");
        holder.mDeliver_at.setText(timeLineModel.custaddr+"\n");
        holder.Remark.setText("Remark: "+timeLineModel.remark);
        holder.mTime.setText(timeLineModel.deltime);
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

                Deliver(timeLineModel,position,newPosition);

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
                new AlertDialog.Builder(mContext)
                        .setTitle("Return")
                        .setMessage("Are you sure you want Return Delivery ?")
                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Return(timeLineModel,position,newPosition);
                            }
                        })
                        .setNegativeButton(R.string.alert_no_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.stop_trip).show();

            }
        });

    }
    private void dialContactPhone(final String phoneNumber) {
        mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void Deliver(final model_pending timeLineModel, final int position, final int newPosition){

        JsonObject json = new JsonObject();
        json.addProperty("flag", "delvr");
        json.addProperty("loc", Rider_Lat+","+Rider_Long);
        json.addProperty("tripid", timeLineModel.tripid);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");
        json.addProperty("amtrec", timeLineModel.amtcollect + "");
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
                    }
                });

    }

    private void Return(final model_pending timeLineModel, final int position, final int newPosition){

        JsonObject json = new JsonObject();
        json.addProperty("flag", "delvr");
        json.addProperty("loc", Rider_Lat+","+Rider_Long);
        json.addProperty("tripid", timeLineModel.tripid);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");
        json.addProperty("amtrec", timeLineModel.amtcollect + "");
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
                    }
                });

    }


    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }


}
