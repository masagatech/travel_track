package com.masaga.goyorider.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masaga.goyorider.R;
import com.masaga.goyorider.database.SQLBase;
import com.masaga.goyorider.forms.Orientation;
import com.masaga.goyorider.model.model_new_order;
import com.masaga.goyorider.model.model_notification;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fajar on 05-Jun-17.
 */

public class NotificationAdapter extends BaseAdapter {


    private List<model_new_order> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    Context mcontext;



    public NotificationAdapter( Context context,List<model_new_order> feedList, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mcontext=context;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
    }

    private class ViewHolder {
        TextView mStops;
        TextView mTime;
        TextView Cash_amount;
        TextView popup_counter;
        TextView mOulets;
        LinearLayout NewOrderItem;
        Button Btn_Accept;
        Button Btn_Reject;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        model_new_order timeLineModel = (model_new_order) getItem(position);

//        final model_notification timeLineModel = mFeedList.get(position);

//
//        convertView.mStops.setText(timeLineModel.autoid + "");
//        convertView.Cash_amount.setText(timeLineModel.amt);
//        convertView.mOulets.setText(timeLineModel.olnm);
//        convertView.mTime.setText(timeLineModel.pcktm);

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(R.layout.content_notification, parent, false);
            holder = new  ViewHolder();
            holder.mStops = (TextView) convertView.findViewById(R.id.stops_new_order);
            holder.Cash_amount = (TextView) convertView.findViewById(R.id.Cash);
            holder.mTime = (TextView) convertView.findViewById(R.id.picktime_new_order);
            holder.Btn_Accept = (Button) convertView.findViewById(R.id.Accept_order);
            holder.Btn_Reject = (Button) convertView.findViewById(R.id.Reject_Order);
            holder.popup_counter = (TextView) convertView.findViewById(R.id.popup_counter);
            holder.mOulets = (TextView) convertView.findViewById(R.id.outlets_new_order);
            holder.NewOrderItem = (LinearLayout) convertView.findViewById(R.id.NewOrderItem);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.mStops.setText(timeLineModel.getMstops()+"");
        holder.Cash_amount.setText(timeLineModel.getmCash()+"");
        holder.mTime.setText(timeLineModel.getmTime());
        holder.mOulets.setText(timeLineModel.getmOutlets());

        return convertView;
    }
    @Override
    public int getCount() {
        return mFeedList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFeedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFeedList.indexOf(getItem(position));
    }
}

