package com.goyo.goyorider.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.goyo.goyorider.R;
import com.goyo.goyorider.forms.OrderStatus;
import com.goyo.goyorider.forms.Orientation;
import com.goyo.goyorider.forms.PendingOrdersView;
import com.goyo.goyorider.gloabls.Global;
import com.goyo.goyorider.model.model_completed;
import com.goyo.goyorider.utils.VectorDrawableUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static com.goyo.goyorider.gloabls.Global.urls.getOrders;

/**
 * Created by fajar on 26-May-17.
 */

public class AllOrdersAdapter extends RecyclerView.Adapter<pending_order_viewHolder>  {

    private List<model_completed> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    private ProgressDialog loader;
    private int []Stat;


    public AllOrdersAdapter(List<model_completed> feedList, Orientation orientation, boolean withLinePadding,int []stat) {
        mFeedList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        Stat=stat;
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

        view = mLayoutInflater.inflate(R.layout.all_order_timeline, parent, false);

        return new pending_order_viewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final pending_order_viewHolder holder,  int position) {

        final model_completed timeLineModel = mFeedList.get(position);

        if(timeLineModel.status == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.status == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.round));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.round));
        }

//        holder.mDate.setText(currentDateTimeString);

        holder.mOrder.setText(timeLineModel.ordno +"");
        holder.mMarchant.setText(timeLineModel.olnm);

        if(Stat[position]==(1)){

            GradientDrawable gd = (GradientDrawable) holder.Border.getBackground();
            int width_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mContext.getResources().getDisplayMetrics());
            gd.setStroke(width_px, Color.parseColor("#ff99cc00"));

            holder.DeliverTexts.setText("Delivered");
            holder.DeliverTexts.setBackgroundColor(Color.parseColor("#ff99cc00"));
            holder.DeliverTexts.setCompoundDrawablesWithIntrinsicBounds( R.drawable.del, 0, 0, 0);

//            holder.Btn_AcceptReject.setImageDrawable(tempImage);
//            GradientDrawable gd = new GradientDrawable();
//            gd.setStroke(2,Color.parseColor("#ff99cc00"));
//            holder.Border.setBackground(gd);
//            holder.Btn_AcceptReject.setBackgroundColor(Color.parseColor("#ff99cc00"));
//                                    holder.Btn_AcceptReject.setBackgroundResource(R.drawable.complated_orders);
//            Drawable tempImage = ContextCompat.getDrawable(mContext,R.drawable.complated_orders);
//            holder.Btn_AcceptReject.setImageDrawable(tempImage);
        }else if(Stat[position]==(2)){
            GradientDrawable gd = (GradientDrawable) holder.Border.getBackground();
            int width_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mContext.getResources().getDisplayMetrics());
            gd.setStroke(width_px, Color.parseColor("#ffff4444"));

            holder.DeliverTexts.setText("Returned");
            holder.DeliverTexts.setBackgroundColor(Color.parseColor("#ffff4444"));
            holder.DeliverTexts.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ret, 0, 0, 0);

//            GradientDrawable gd = new GradientDrawable();
//            gd.setStroke(2,Color.parseColor("#ffff4444"));
//            holder.Border.setBackground(gd);
//            holder.Btn_AcceptReject.setBackgroundColor(Color.parseColor("#ffff4444"));
//            Drawable tempImage = ContextCompat.getDrawable(mContext,R.drawable.rejected_order);
//            holder.Btn_AcceptReject.setImageDrawable(tempImage);
//                                    holder.Btn_AcceptReject.setBackgroundResource(R.drawable.rejected_order);
        }

//        holder.Custmer_name.setText(timeLineModel.custname);
//        holder.mDeliver_at.setText(timeLineModel.custmob+"\n"+ timeLineModel.custaddr+"\n"+"Remark: "+timeLineModel.remark);
//        holder.mTime.setText(timeLineModel.deltime);
//        holder.collected_cash.setText("₹ " +timeLineModel.amtcollect +"");

        holder.ButtonHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View m) {
                if(holder.ClickToHide.getVisibility() == View.VISIBLE){
                    holder.ClickToHide.setVisibility(View.GONE);
                    holder.mDate.setVisibility(View.GONE);
                    holder.mOrder.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    holder.mMarchant.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }else {
                    loader = new ProgressDialog(mContext);
                    loader.setCancelable(false);
                    loader.setMessage("Please wait..");
                    loader.show();
                    AllOrder(timeLineModel,holder);
                    holder.ClickToHide.setVisibility(View.VISIBLE);
                    holder.mDate.setVisibility(View.VISIBLE);
                    holder.mOrder.setCompoundDrawablesWithIntrinsicBounds( R.drawable.order_id, 0, 0, 0);
                    holder.mMarchant.setCompoundDrawablesWithIntrinsicBounds( R.drawable.pending_outlets, 0, 0, 0);
                }
            }
        });
        holder.collected_cash.setEnabled(false);
    }



    private void AllOrder(final model_completed timeLineModel, final pending_order_viewHolder holder) {

        Ion.with(mContext)
                .load("GET", getOrders.value)
                .addQuery("flag", "completed")
                .addQuery("status", "0")
                .addQuery("subflag", "detl")
                .addQuery("ordid", timeLineModel.ordid + "")
                .addQuery("orddid", timeLineModel.orderdetailid + "")
                .addQuery("rdid", Global.loginusr.getDriverid() + "")
                .addQuery("stat","0")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) Log.v("result", result.toString());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<model_completed>>() {
                            }.getType();
                            List<model_completed> events = (List<model_completed>) gson.fromJson(result.get("data"), listType);

                            if (events.size() > 0) {

                                JsonObject Data = result.get("data").getAsJsonArray().get(0).getAsJsonObject();
                                timeLineModel.custaddr = Data.get("cadr").getAsString();
                                timeLineModel.custname = Data.get("cnm").getAsString();
                                timeLineModel.deltime = Data.get("dtm").getAsString();
                                timeLineModel.custname = Data.get("cnm").getAsString();
                                timeLineModel.dltm = Data.get("dltm").getAsString();




                                holder.mTime.setText(timeLineModel.deltime + "");
                                holder.collected_cash.setText("₹ " + timeLineModel.amtcollect + "");
                                holder.Custmer_name.setText(timeLineModel.custname + "");
                                holder.mDeliver_at.setText(timeLineModel.custaddr + " ");
                                holder.mDate.setText(timeLineModel.dltm + "");

                            }

                        } catch (Exception ea) {
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
