package com.travel.tracker.adapters;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.travel.tracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fajar on 29-May-17.
 */

public class PushOrderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.order_ids)
    TextView mOrderID;
    @BindView(R.id.Alert)
    TextView mAlertTime;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.marchent)
    TextView mMarchant;
    @BindView(R.id.rider)
    TextView mRider;
    @BindView(R.id.adress)
    TextView mAddr;
    @BindView(R.id.Cash)
    TextView mCash;
    @BindView(R.id.Btn_Push)
    Button Btn_Push;
    CountDownTimer conter;

    public PushOrderViewHolder(View itemView, int viewType) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
