package com.masaga.goyorider.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.R;
import com.masaga.goyorider.model.model_mytrips;

import java.util.List;

/**
 * Created by mTech on 03-Apr-2017.
 */
public class my_trip_listAdapter extends BaseAdapter {
    List<model_mytrips> list;
    LayoutInflater inflater;
    Context context;
    String _completed, _canceled, _started,_pending,_drop,_pickup, _pause;


    public my_trip_listAdapter(Context context, List<model_mytrips> lst, Resources rs) {

        _completed = rs.getString(R.string.status_completed);
        _canceled = rs.getString(R.string.status_canceled);
        _started = rs.getString(R.string.status_started);
        _pending = rs.getString(R.string.status_pending);
        _drop = rs.getString(R.string.drop);
        _pickup = rs.getString(R.string.pickup);
        _pause = rs.getString(R.string.status_pause);

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
            convertView = inflater.inflate(R.layout.layout_my_trip_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }




        mViewHolder.txtTitle.setText(list.get(position).nm);
        mViewHolder.txtSubTitle.setText(list.get(position).btch);
        mViewHolder.hidid.setText(list.get(position).id + "");

        mViewHolder.txtFromToTime.setText(list.get(position).date  + "    " + list.get(position).time);
       /* if(list.get(position).get(Tables.tbl_driver_info.sentToserver).equals("true")){
            mViewHolder.uploadonRes.setBackgroundResource(R.drawable.bluetick);

        }else{
            mViewHolder.uploadonRes.setBackgroundResource(R.drawable.graytick);
        }*/
        //Log.e("date",list.get(position).get(Tables.tbl_driver_info.createon));
        //String date = common.getDateFormat("MM/dd/yyyy HH:mm:ss.SSS", list.get(position).get(Tables.tbl_driver_info.createon), "dd MMM yyyy HH:mm");

        if(list.get(position).pd.equalsIgnoreCase("p")){
            mViewHolder.txtType.setText(_pickup);
            mViewHolder.txtType.setTextColor(Color.parseColor("#18b400"));
            mViewHolder.txtColor.setBackgroundColor(Color.parseColor("#18b400"));
           }
        else{
           mViewHolder.txtType.setText(_drop);
            mViewHolder.txtColor.setBackgroundColor(Color.RED);
           mViewHolder.txtType.setTextColor(Color.RED);
        }


        mViewHolder.thisitem.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        if(list.get(position).stsi.equalsIgnoreCase(Global.start)){
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_play);
            mViewHolder.txtStatus.setText(_started);
            mViewHolder.txtStatus.setTextColor(Color.parseColor("#18b400"));
        }
        else if(list.get(position).stsi.equalsIgnoreCase(Global.pause)) {
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_pause);
            mViewHolder.txtStatus.setText(_pause);
            mViewHolder.txtStatus.setTextColor(Color.parseColor("#FFAA00"));
        }
        else if(list.get(position).stsi.equalsIgnoreCase(Global.done)) {
            mViewHolder.thisitem.setBackgroundColor(Color.parseColor("#88CCCCCC"));
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_done);
            mViewHolder.txtStatus.setText(_completed);
            mViewHolder.txtStatus.setTextColor(Color.parseColor("#18b400"));
        }
        else if(list.get(position).stsi.equalsIgnoreCase(Global.cancel)) {
            mViewHolder.thisitem.setBackgroundColor(Color.parseColor("#88CCCCCC"));
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_cancel);
            mViewHolder.txtStatus.setText(_canceled);
            mViewHolder.txtStatus.setTextColor(Color.parseColor("#FF2100"));
        }
        else if(list.get(position).stsi.equalsIgnoreCase(Global.pending)){
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_wait);
            mViewHolder.txtStatus.setText(_pending);
            mViewHolder.txtStatus.setTextColor(Color.parseColor("#FFAA00"));
            //mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_play);
        }



        return convertView;
    }


    private class MyViewHolder {
        private TextView txtTitle,txtSubTitle,txtIcon,hidid,txtType, txtStatus,txtFromToTime, txtColor;
        private LinearLayout thisitem;


        public MyViewHolder(View item) {
            thisitem = (LinearLayout) item.findViewById(R.id.lstItem);
            hidid = (TextView) item.findViewById(R.id.txtHidId);
            txtTitle = (TextView) item.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) item.findViewById(R.id.txtsubtitle);
            txtIcon = (TextView) item.findViewById(R.id.txtIcon);
            txtType = (TextView) item.findViewById(R.id.txtType);
            txtStatus = (TextView) item.findViewById(R.id.txtStatus);
            txtColor= (TextView) item.findViewById(R.id.txtColor);
            txtFromToTime = (TextView) item.findViewById(R.id.txtFromToTime);
        }
    }

}