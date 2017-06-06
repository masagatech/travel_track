package com.masaga.goyorider.adapters;

import android.content.Context;
import android.database.Observable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.R;
import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.Orientation;
import com.masaga.goyorider.forms.PendingModel;
import com.masaga.goyorider.forms.PendingOrdersView;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.model.model_completed;
import com.masaga.goyorider.model.model_pending;
import com.masaga.goyorider.utils.VectorDrawableUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static android.R.color.holo_orange_light;
import static com.masaga.goyorider.gloabls.Global.urls.getOrders;

/**
 * Created by fajar on 22-May-17.
 */

public class ComplatedOrderAdapter extends RecyclerView.Adapter<pending_order_viewHolder>  {

    private List<model_completed> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


    public ComplatedOrderAdapter(List<model_completed> feedList, Orientation orientation, boolean withLinePadding) {
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

        view = mLayoutInflater.inflate(R.layout.complated_order_timeline, parent, false);

        return new pending_order_viewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final pending_order_viewHolder holder, final int position) {

        final model_completed timeLineModel = mFeedList.get(position);

        if(timeLineModel.status == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.status == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.round));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.round));
        }
        holder.mOrder.setText(timeLineModel.ordno +"");
        holder.mMarchant.setText(timeLineModel.olnm);
//        holder.Custmer_name.setText(timeLineModel.custname);
//        holder.mDeliver_at.setText(timeLineModel.custaddr);
//        holder.Remark.setText("Remark: "+timeLineModel.remark);
//        holder.mTime.setText(timeLineModel.deltime);
//        holder.collected_cash.setText("₹ " +timeLineModel.amtcollect +"");
        final int newPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View m) {
                ComplatedOrder(timeLineModel,holder,position);
                if(holder.ClickToHide.getVisibility() == View.VISIBLE){
                    holder.ClickToHide.setVisibility(View.GONE);
                    holder.mDate.setVisibility(View.GONE);
                    holder.mOrder.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    holder.mMarchant.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }else {

                    ComplatedOrder(timeLineModel,holder,position);
                holder.ClickToHide.setVisibility(View.VISIBLE);
                    holder.mDate.setVisibility(View.VISIBLE);
                    holder.mOrder.setCompoundDrawablesWithIntrinsicBounds( R.drawable.order_id, 0, 0, 0);
                    holder.mMarchant.setCompoundDrawablesWithIntrinsicBounds( R.drawable.pending_outlets, 0, 0, 0);
                }
            }
        });
        holder.collected_cash.setEnabled(false);



//        holder.Btn_Delivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final PendingModel status = mFeedList.get(position+1);
//                status.setmStatus(OrderStatus.ACTIVE);
//                mFeedList.remove(newPosition);
//                notifyItemRemoved(newPosition);
//                notifyItemRangeChanged(newPosition,mFeedList.size());
//            }
//        });

    }
    private void ComplatedOrder(final model_completed timeLineModel, final pending_order_viewHolder holder,final int position){

        Ion.with(mContext)
                .load("GET", getOrders.value)
                .addQuery("flag", "completed")
                  .addQuery("subflag", "detl")
                  .addQuery("ordid", timeLineModel.ordid + "")
                  .addQuery("orddid", timeLineModel.orderdetailid + "")
                  .addQuery("rdid", Global.loginusr.getDriverid() + "")
                .addQuery("stat","1")
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
