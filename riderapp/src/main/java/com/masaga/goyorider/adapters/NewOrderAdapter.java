package com.masaga.goyorider.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.masaga.goyorider.database.SQLBase;
import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.Orientation;
import com.masaga.goyorider.forms.PendingOrdersView;
import com.masaga.goyorider.forms.dashboard;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.model.model_new_order;
import com.masaga.goyorider.model.model_notification;
import com.masaga.goyorider.model.model_pending;
import com.masaga.goyorider.utils.VectorDrawableUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.R.id.text1;
import static com.masaga.goyorider.gloabls.Global.urls.setStatus;

/**
 * Created by fajar on 02-Jun-17.
 */

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapterViewHolder> {

    private List<model_notification> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    static HashMap<Integer, CountDownTimer> thread = new HashMap<>();
    SQLBase sb;

    public NewOrderAdapter(List<model_notification> feedList, Orientation orientation, boolean withLinePadding, SQLBase _sb) {
        mFeedList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        sb = _sb;
    }

    @Override
    public int getItemViewType(int position) {
        return PendingOrdersView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public NewOrderAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        view = mLayoutInflater.inflate(R.layout.timeline_new_order, parent, false);

        return new NewOrderAdapterViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final NewOrderAdapterViewHolder holder, final int position) {
        if (holder.timer != null) KillTimer(holder.timer);
        final model_notification timeLineModel = mFeedList.get(position);


        holder.mStops.setText(timeLineModel.autoid + "");
        holder.Cash_amount.setText(timeLineModel.amt);
        holder.mOulets.setText(timeLineModel.olnm);
        holder.mTime.setText(timeLineModel.pcktm);

        holder.Btn_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.timer.cancel();
                holder.timer= null;
                holder.Btn_Reject.setVisibility(View.GONE);
                holder.Btn_Accept.setVisibility(View.GONE);
                setStatus("accord", timeLineModel.ordid, position, timeLineModel.autoid, holder);


            }
        });

        holder.Btn_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.timer.cancel();
                holder.timer= null;
                holder.Btn_Reject.setVisibility(View.GONE);
                holder.Btn_Accept.setVisibility(View.GONE);
                setStatus("rejord", timeLineModel.ordid, position, timeLineModel.autoid, holder);

            }
        });
        Log.v("rmtm", "" + timeLineModel.remaintime);

        holder.timer = new CountDownTimer(timeLineModel.remaintime, 1000) {
            public void onFinish() {
                Log.v("autoid", "" + timeLineModel.autoid);
                Log.v("pos", "" + position);
                sb.NOTIFICATION_DELETE(timeLineModel.autoid + "");
                holder.popup_counter.setText("Expired !!!");
                holder.Btn_Reject.setVisibility(View.GONE);
                holder.Btn_Accept.setVisibility(View.GONE);
//                mFeedList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mFeedList.size());
            }

            public void onTick(long millisUntilFinished) {
                timeLineModel.remaintime -= 1000;
                holder.popup_counter.setText("" + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
        }.start();

        //  thread.put(timeLineModel.autoid, c);

    }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View m) {
//                if(holder.ClickToHide.getVisibility() == View.VISIBLE){
//                    holder.ClickToHide.setVisibility(View.GONE);
//                    holder.mDate.setVisibility(View.GONE);
//                    holder.mOrder.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    holder.mMarchant.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                }else {
//                    holder.ClickToHide.setVisibility(View.VISIBLE);
//                    holder.mDate.setVisibility(View.VISIBLE);
//                    holder.mOrder.setCompoundDrawablesWithIntrinsicBounds( R.drawable.order_id, 0, 0, 0);
//                    holder.mMarchant.setCompoundDrawablesWithIntrinsicBounds( R.drawable.pending_outlets, 0, 0, 0);
//                }
//            }
//        });
//        holder.collected_cash.setEnabled(false);


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


    private void setStatus(final String flag, String ordid, final int position, final int autoid,final NewOrderAdapterViewHolder holder) {

        JsonObject json = new JsonObject();
        json.addProperty("flag", flag);
        json.addProperty("ordid", ordid);
        json.addProperty("rdid", Global.loginusr.getDriverid() + "");

        Ion.with(mContext)
                .load(setStatus.value)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {
                            if (result != null) {
                                if (result.get("status").getAsBoolean()) {
                                    String msg = result.get("data").getAsJsonObject().get("msg").toString();
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                    if(flag=="accord" ){
                                        holder.popup_counter.setText("ACCEPTED") ;
                                    }else{
                                        holder.popup_counter.setText("REJECTED") ;
                                    }
                                } else {
                                    String msg = result.get("data").getAsJsonObject().get("msg").toString();
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                }
                                sb.NOTIFICATION_DELETE(autoid + "");

                            } else {
                                Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }
                });

    }

    public void Kill() {
        Set<Map.Entry<Integer, CountDownTimer>> s = thread.entrySet();
        if (s != null) {
            Iterator it = s.iterator();
            while (it.hasNext()) {
                try {

                    Map.Entry pairs = (Map.Entry) it.next();
                    CountDownTimer r = (CountDownTimer) pairs.getValue();
                    r.cancel();
                    r = null;
                } catch (Exception x) {

                }
            }
            s = null;
            it = null;
            thread.clear();
        }


    }

    public void KillTimer(CountDownTimer tmr) {
        tmr.cancel();
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }


}
