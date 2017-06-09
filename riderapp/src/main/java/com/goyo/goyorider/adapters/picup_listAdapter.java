package com.goyo.goyorider.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goyo.goyorider.gloabls.Global;
import com.goyo.goyorider.R;
import com.goyo.goyorider.model.model_crewdata;

import java.util.ArrayList;

/**
 * Created by mTech on 09-Apr-2017.
 */
public class picup_listAdapter extends BaseAdapter implements Filterable {
    private ArrayList<model_crewdata> list;
    private ArrayList<model_crewdata> mainlist;
    private LayoutInflater inflater;
    private Context context;
    private Filter pickupfilter;
    private String _pickedup, _absent, _droped, _pending;

    public picup_listAdapter(Context context, ArrayList<model_crewdata> lst, Resources rs) {
        list = new ArrayList<>();
        this.list.addAll(lst);
        mainlist = new ArrayList<>();
        mainlist.addAll(lst);
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        _pickedup = rs.getString(R.string.crew_status_pickedup);
        _absent = rs.getString(R.string.crew_status_absent);
        _droped = rs.getString(R.string.crew_status_drop);
        _pending = rs.getString(R.string.crew_status_pending);
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
            convertView = inflater.inflate(R.layout.layout_pickup_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txtTitle.setText(list.get(position).stdnm);
        mViewHolder.txtSubTitle.setText(list.get(position).fthrmob);
        mViewHolder.hidid.setText(list.get(position).id + "");
        mViewHolder.txtType.setText(list.get(position).addr);
        mViewHolder.thisitem.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        if (list.get(position).stsi.equalsIgnoreCase(Global.pickedupdrop)) {
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_done);
            if (list.get(position).typ.equals("p")) {
                mViewHolder.txtStatus.setText(_pickedup);
            } else {
                mViewHolder.txtStatus.setText(_droped);
            }

            mViewHolder.txtStatus.setTextColor(Color.parseColor("#18b400"));
            mViewHolder.thisitem.setBackgroundColor(Color.parseColor("#88CCCCCC"));
        } else if (list.get(position).stsi.equalsIgnoreCase(Global.absent)) {
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_cancel);
            mViewHolder.txtStatus.setText(_absent);
            mViewHolder.txtStatus.setTextColor(Color.RED);
            mViewHolder.thisitem.setBackgroundColor(Color.parseColor("#88CCCCCC"));
        } else if (list.get(position).stsi.equalsIgnoreCase(Global.pending)) {
            mViewHolder.txtIcon.setBackgroundResource(R.drawable.ic_action_wait);
            mViewHolder.txtStatus.setText(_pending);
            mViewHolder.txtStatus.setTextColor(Color.parseColor("#FFAA00"));
        }
        mViewHolder.txtStatus.setText(" ");
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (pickupfilter == null)
            pickupfilter = new PickupFilter();

        return pickupfilter;
    }

    public void resetData() {
        list = mainlist;
    }


    private class MyViewHolder {
        private TextView txtTitle, txtSubTitle, txtIcon, hidid, txtType, txtStatus;
        private LinearLayout thisitem;


        public MyViewHolder(View item) {
            thisitem = (LinearLayout) item.findViewById(R.id.lstItem);
            hidid = (TextView) item.findViewById(R.id.txtHidId);
            txtTitle = (TextView) item.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) item.findViewById(R.id.txtsubtitle);
            txtIcon = (TextView) item.findViewById(R.id.txtIcon);
            txtType = (TextView) item.findViewById(R.id.txtType);
            txtStatus = (TextView) item.findViewById(R.id.txtStatus);
        }
    }

    private class PickupFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<model_crewdata> filteredItems = new ArrayList<>();

                for (int i = 0, l = mainlist.size(); i < l; i++) {
                    model_crewdata crew = mainlist.get(i);
                    if (crew.stsi.equals(constraint))
                        filteredItems.add(crew);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = mainlist;
                    result.count = mainlist.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.count == 0) {
                list = (ArrayList<model_crewdata>) results.values;
                notifyDataSetInvalidated();
            } else {
                list = (ArrayList<model_crewdata>) results.values;
                notifyDataSetChanged();
            }
        }
    }


}