package com.masaga.goyorider.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.masaga.goyorider.R;
import com.masaga.goyorider.model.model_rider_list;

import java.util.ArrayList;

import static android.R.attr.country;

/**
 * Created by fajar on 07-Jun-17.
 */

public class RiderListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    public ArrayList<model_rider_list> data;

    public RiderListAdapter(Context context, ArrayList<model_rider_list> item) {

        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = item;


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
            convertView = inflater.inflate(R.layout.csutem_spinner, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        model_rider_list Rider = data.get(position);



        mViewHolder.uRidername.setText(Rider.nm);
        mViewHolder.uPhone.setText(Rider.mb);
        mViewHolder.uKm.setText("1 KM Away");
        mViewHolder.uBtry.setText(Rider.btry+"%");



        return convertView;

    }






    private class MyViewHolder {

        TextView uRidername;
        TextView uPhone;
        TextView uKm;
        TextView uBtry;



        public MyViewHolder(View item) {
            uRidername = (TextView) item.findViewById(R.id.name);
            uPhone = (TextView) item.findViewById(R.id.rider_numbr);
            uKm = (TextView) item.findViewById(R.id.km);
            uBtry = (TextView) item.findViewById(R.id.rider_btry);
        }
    }
}