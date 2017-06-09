package com.goyo.goyorider.adapters;

/**
 * Created by mTech on 03-Mar-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.goyo.goyorider.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    List<String> listStrings;
    String[] objects;

    public CustomAdapter(Context context, int textViewResourceId,
                           List<String> listStrings) {
        super(context, textViewResourceId, listStrings);
        this.listStrings = listStrings;
        // TODO Auto-generated constructor stub
    }

    public CustomAdapter(Context context, int textViewResourceId,
                           String[] objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        // TODO Auto-generated constructor stub
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = null;
        // If this is the initial dummy entry, make it hidden
        if (position == -1) {
            TextView tv = new TextView(getContext());
            tv.setHeight(0);
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            // Pass convertView as null to prevent reuse of special case views
            v = super.getDropDownView(position, null, parent);
        }
        // Hide scroll bar because it appears sometimes unnecessarily, this does
        // not prevent scrolling
        parent.setVerticalScrollBarEnabled(false);
        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {
        // TODO Auto-generated method stub
        // return super.getView(position, convertView, parent);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.layout_ddl, parent, false);
        TextView label = (TextView) row.findViewById(R.id.text);
        if(listStrings!=null) {
            if (listStrings.size() > 0)
                label.setText(listStrings.get(position));
        }
        if(objects!=null) {
            if (objects.length > 0) {
                label.setText(objects[position]);
            }
        }
		/*label.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e("value are", value.get(position).get("id").toString());
			}
		});*/
        return row;
    }
}