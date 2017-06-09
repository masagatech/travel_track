package com.masaga.goyorider.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.masaga.goyorider.R;
import com.masaga.goyorider.model.model_order_details;
import com.masaga.goyorider.model.model_rider_list;

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



        mViewHolder.uDate.setText(details.dt+"");
        mViewHolder.uReturn.setText(details.ret+"");
        mViewHolder.uDelivery.setText(details.del+"");
        mViewHolder.uRejected.setText("");
        mViewHolder.uTotal.setText((details.del+details.ret)+"");



        return convertView;

    }






    private class MyViewHolder {

        TextView uDate;
        TextView uReturn;
        TextView uDelivery;
        TextView uRejected;
        TextView uTotal;



        public MyViewHolder(View item) {
            uDate = (TextView) item.findViewById(R.id.DateAllOrder);
            uReturn = (TextView) item.findViewById(R.id.ReturnAllOrder);
            uDelivery = (TextView) item.findViewById(R.id.DeliverAllOrder);
            uRejected = (TextView) item.findViewById(R.id.RejectedAllOrder);
            uTotal = (TextView) item.findViewById(R.id.TotalAllOrder);
        }
    }
}