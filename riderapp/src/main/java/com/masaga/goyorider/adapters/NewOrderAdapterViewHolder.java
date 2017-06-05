package com.masaga.goyorider.adapters;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.masaga.goyorider.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fajar on 02-Jun-17.
 */

public class NewOrderAdapterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.stops_new_order)
    TextView mStops;
    @BindView(R.id.NewOrderItem)
    LinearLayout NewOrderItem;
    @BindView(R.id.picktime_new_order)
    TextView mTime;
//    @BindView(R.id.marchent_new_order)
//    TextView mMarchant;
    @BindView(R.id.outlets_new_order)
    TextView mOulets;
    @BindView(R.id.Cash)
    TextView Cash_amount;
    @BindView(R.id.popup_counter)
    TextView popup_counter;

    @BindView(R.id.Accept_order)
    Button Btn_Accept;
    @BindView(R.id.Reject_Order)
    Button Btn_Reject;

    CountDownTimer timer;
    public NewOrderAdapterViewHolder(View itemView, int viewType) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
