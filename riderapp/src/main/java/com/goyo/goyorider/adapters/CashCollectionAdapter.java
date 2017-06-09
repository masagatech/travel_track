package com.goyo.goyorider.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goyo.goyorider.R;
import com.goyo.goyorider.forms.CashModel;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by fajar on 23-May-17.
 */

public class CashCollectionAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    List<CashModel> list = new ArrayList<CashModel>();
    LayoutInflater inflater;
    Context context;
    public static final int ITEM = 0;
    public static final int SECTION = 1;
    double Total=0;
    int t=0;
    private  static  String Header="";
    public CashCollectionAdapter(Context context,List<CashModel> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        //super(context, R.layout.cash_collection_timeline);
    }

    private static final int[] COLORS = new int[] {
            R.color.green_light, R.color.orange_light,
            R.color.blue_light, R.color.red_light };


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
        return  position;
    }

    @Override public int getViewTypeCount() {
        return 2;
    }
    @Override public int getItemViewType(int position) {
        return this.list.get(position).getmType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == SECTION;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cash_collection_timeline, parent, false);
            mViewHolder = new  ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = ( ViewHolder) convertView.getTag();
        }

        final CashModel ml = list.get(position);

        mViewHolder.mMarchant.setText(ml.getmMarchant());
        mViewHolder.mMarchant.setVisibility(View.VISIBLE);
        //  mViewHolder.devider.setVisibility(View.VISIBLE);
        mViewHolder.order_id.setText(ml.getmOrderID());
        mViewHolder.cash_amount.setText("₹ "+ml.getmCashAmount());
        mViewHolder.Hand_over.setText(ml.getmHandover());
        mViewHolder.subtotal_text.setText("Sub Total : ");

        if(ml.getmType() == SECTION){
            mViewHolder.summery.setVisibility(View.GONE);
            mViewHolder.CashSubTotal.setText("₹ "+Total);
            mViewHolder.Sub_Total.setVisibility(View.VISIBLE);
            mViewHolder.margintop.setVisibility(View.VISIBLE);
            mViewHolder.CashSubTotal.setText("₹ "+ml.getmCashAmount());

                mViewHolder.mMarchant.setBackgroundColor(parent.getResources().getColor(R.color.blue_light));
                mViewHolder.Sub_Total.setBackgroundColor(parent.getResources().getColor(R.color.blue_light));




        }else {

            mViewHolder.Marchant_group.setVisibility(View.GONE);
        }
        mViewHolder.Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Saved Details of "+ml.getmMarchant(),Toast.LENGTH_SHORT).show();
            }
        });


        try {
            mViewHolder.Hand_over.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(context, v);
                    popup.getMenuInflater().inflate(R.menu.handover_popup,
                            popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.dispacher:
                                    Toast.makeText(context, " Dispatcher  Selected",Toast.LENGTH_SHORT).show();
                                    mViewHolder.Hand_over.setText("Dispatcher");

                                    break;
                                case R.id.teamLeader:
                                    Toast.makeText(context, " Team Leader  Selected",Toast.LENGTH_SHORT).show();
                                    mViewHolder.Hand_over.setText("Team Leader");

                                    break;
                                default:
                                    break;
                            }

                            return true;
                        }
                    });


                }
            });
        }catch (Exception e) {

            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mMarchant,order_id,cash_amount,Hand_over,margintop,CashSubTotal,subtotal_text;
        LinearLayout Sub_Total,summery,Marchant_group;
        Button Save;

        ViewHolder(View view) {

            subtotal_text = (TextView) view.findViewById(R.id.text_subtotal);
            Save = (Button) view.findViewById(R.id.save_all);
            mMarchant = (TextView) view.findViewById(R.id.text_marchent);
            CashSubTotal = (TextView) view.findViewById(R.id.cash_sub_total);
            Sub_Total = (LinearLayout) view.findViewById(R.id.Sub_total);
            order_id = (TextView) view.findViewById(R.id.order_id);
            cash_amount = (TextView) view.findViewById(R.id.cash_amount);
            summery = (LinearLayout) view.findViewById(R.id.summery);
            Marchant_group = (LinearLayout) view.findViewById(R.id.marchant_group);
            Hand_over = (TextView) view.findViewById(R.id.Hand_over);
            margintop = (TextView) view.findViewById(R.id.margintop);
        }
    }
}