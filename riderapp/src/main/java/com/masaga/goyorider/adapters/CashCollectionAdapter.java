package com.masaga.goyorider.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.masaga.goyorider.R;
import com.masaga.goyorider.forms.CashModel;
import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.Orientation;
import com.masaga.goyorider.forms.PendingModel;
import com.masaga.goyorider.forms.PendingOrdersView;
import com.masaga.goyorider.goyorider.MainActivity;
import com.masaga.goyorider.model.model_mykids;
import com.masaga.goyorider.utils.VectorDrawableUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;
import static java.security.AccessController.getContext;

/**
 * Created by fajar on 23-May-17.
 */

public class CashCollectionAdapter extends BaseAdapter{

    List<CashModel> list = new ArrayList<CashModel>();
    LayoutInflater inflater;
    Context context;
    double Total=0;
    int t=0;
    private  static  String Header="";
    public CashCollectionAdapter(Context context,List<CashModel> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        //super(context, R.layout.cash_collection_timeline);
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
        return  position;
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



        if (!Header.equals(list.get(position).getmMarchant()))
        {
            Header=list.get(position).getmMarchant();
            mViewHolder.mMarchant.setText(list.get(position).getmMarchant());
            mViewHolder.mMarchant.setVisibility(View.VISIBLE);
            mViewHolder.devider.setVisibility(View.VISIBLE);
            mViewHolder.margintop.setVisibility(View.VISIBLE);
        }else {

            mViewHolder.mMarchant.setVisibility(View.GONE);
            mViewHolder.devider.setVisibility(View.GONE);
            t=t+1;
//            if(t==9){
//                mViewHolder.Sub_Total.setVisibility(View.VISIBLE);
//            }
//            else if(t==8){
//                mViewHolder.Sub_Total.setVisibility(View.VISIBLE);
//            }


        }

        mViewHolder.order_id.setText(list.get(position).getmOrderID());
        mViewHolder.cash_amount.setText("₹ "+list.get(position).getmCashAmount());
        mViewHolder.Hand_over.setText(list.get(position).getmHandover());
        mViewHolder.subtotal_text.setText("Sub Total : ");
//        for (int i=0;i<=list.get(position);i++){
//            Total=Total+Double.parseDouble(mViewHolder.cash_amount.toString());
//            mViewHolder.CashSubTotal.setText(""+Total);
//        }
        mViewHolder.CashSubTotal.setText("₹ 1110");

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
        View devider;
        LinearLayout Sub_Total;

        ViewHolder(View view) {

            subtotal_text = (TextView) view.findViewById(R.id.text_subtotal);
            mMarchant = (TextView) view.findViewById(R.id.text_marchent);
            CashSubTotal = (TextView) view.findViewById(R.id.cash_sub_total);
            Sub_Total = (LinearLayout) view.findViewById(R.id.Sub_total);
            order_id = (TextView) view.findViewById(R.id.order_id);
            cash_amount = (TextView) view.findViewById(R.id.cash_amount);
            devider = (View) view.findViewById(R.id.devider);
            Hand_over = (TextView) view.findViewById(R.id.Hand_over);
            margintop = (TextView) view.findViewById(R.id.margintop);
        }
    }
}