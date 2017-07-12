package com.travel.tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.travel.tracker.R;
import com.travel.tracker.model.model_order_details;

import java.util.ArrayList;

/**
 * Created by fajar on 08-Jun-17.
 */

public class OrderDetailsAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    public ArrayList<model_order_details> data;

    public OrderDetailsAdapter(ArrayList<model_order_details> item,Context context) {

        this.data = item;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.all_order_details_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        model_order_details details = data.get(position);



//        mViewHolder.uDate.setText(details.dt+"");
//        mViewHolder.uReturn.setText(details.ret+"");
//        mViewHolder.uDelivery.setText(details.del+"");
//        mViewHolder.uRejected.setText("");
//        mViewHolder.uTotal.setText((details.del+details.ret)+"");

        mViewHolder.uDate.setText(details.mDate+"");
        mViewHolder.uStops.setText(details.mStops+"");
        mViewHolder.uKM.setText(details.mKM+"");
        mViewHolder.uCheckIn.setText(details.mCheckIn+"");
        mViewHolder.uCheckOut.setText(details.mCheckOut+"");
        mViewHolder.uRemark.setText(details.mRemark+"");


        return convertView;

    }






    private class MyViewHolder {

        TextView uDate;
        TextView uStops;
        TextView uKM;
        TextView uCheckIn;
        TextView uCheckOut;
        TextView uRemark;

        public MyViewHolder(View item) {
            uDate = (TextView) item.findViewById(R.id.DateAllOrder);
            uStops = (TextView) item.findViewById(R.id.StopsAllOrder);
            uKM = (TextView) item.findViewById(R.id.KMAllOrder);
            uCheckIn = (TextView) item.findViewById(R.id.CheckInAllOrder);
            uCheckOut = (TextView) item.findViewById(R.id.CheckOutAllOrder);
            uRemark = (TextView) item.findViewById(R.id.RemarkAllOrder);
        }
    }
}