package com.travel.tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.travel.tracker.database.Tables;
import com.travel.tracker.R;
import com.travel.tracker.utils.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mTech on 07-Mar-2017.
 */
public class driver_info_listAdapter extends BaseAdapter {
    List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    LayoutInflater inflater;
    Context context;

    public driver_info_listAdapter(Context context, List<HashMap<String,String>> lst) {
        this.list  = lst;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_driver_info_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }




        mViewHolder.titleTxt.setText(list.get(position).get(Tables.tbl_driver_info.sarthinm));
        mViewHolder.povTitle.setText(list.get(position).get(Tables.tbl_driver_info.mob1));
        mViewHolder.hidid.setText(list.get(position).get(Tables.tbl_driver_info.autoid));
        if(list.get(position).get(Tables.tbl_driver_info.sentToserver).equals("true")){
            mViewHolder.uploadonRes.setBackgroundResource(R.drawable.bluetick);

        }else{
            mViewHolder.uploadonRes.setBackgroundResource(R.drawable.graytick);
        }
        //Log.e("date",list.get(position).get(Tables.tbl_driver_info.createon));
        String date = common.getDateFormat("MM/dd/yyyy HH:mm:ss.SSS", list.get(position).get(Tables.tbl_driver_info.createon), "dd MMM yyyy HH:mm");
        mViewHolder.Date.setText(date);



        return convertView;
    }


    private class MyViewHolder {
        TextView titleTxt,povTitle,uploadonRes,Date,hidid;


        public MyViewHolder(View item) {
            hidid = (TextView) item.findViewById(R.id.hidid);
            titleTxt = (TextView) item.findViewById(R.id.titleTxt);
            povTitle = (TextView) item.findViewById(R.id.povTitle);
            uploadonRes = (TextView) item.findViewById(R.id.uploadonRes);
            Date = (TextView) item.findViewById(R.id.Date);
        }
    }

}
