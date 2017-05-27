package com.masaga.goyorider.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masaga.goyorider.R;
import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.Orientation;
import com.masaga.goyorider.forms.PendingModel;
import com.masaga.goyorider.forms.PendingOrdersView;
import com.masaga.goyorider.forms.complated_order;
import com.masaga.goyorider.forms.pending_order;
import com.masaga.goyorider.model.model_pending;
import com.masaga.goyorider.utils.VectorDrawableUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.data;
import static com.masaga.goyorider.forms.PendingOrdersView.getTimeLineViewType;

/**
 * Created by fajar on 22-May-17.
 */

public class pending_order_adapter extends RecyclerView.Adapter<pending_order_viewHolder> {

    private List<model_pending> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    private LayoutInflater mLayoutInflater;

    public pending_order_adapter(List<model_pending> feedList, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
    }
    @Override
    public int getItemViewType(int position) {
        return PendingOrdersView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public pending_order_viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        view = mLayoutInflater.inflate(R.layout.pending_order_timeline, parent, false);

        return new pending_order_viewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(pending_order_viewHolder holder, final int position) {

        final model_pending timeLineModel = mFeedList.get(position);

        if(timeLineModel.status == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.status == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorAccent));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorAccent));
        }
        holder.mDate.setText(currentDateTimeString);
        holder.mOrder.setText(timeLineModel.ordno +"");
        holder.mMarchant.setText(timeLineModel.enttnm+", "+timeLineModel.olnm);
        holder.mDeliver_at.setText(timeLineModel.custaddr);
        holder.mTime.setText(timeLineModel.deltime);
        holder.collected_cash.setText("₹ " +timeLineModel.amtcollect +"");
        final int newPosition = holder.getAdapterPosition();

        holder.Btn_Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final model_pending status = mFeedList.get(mFeedList.size() == position +1 ? position :position+1);
                    status.status=(OrderStatus.ACTIVE);


               // complated_order.mDataList.add(new PendingModel(timeLineModel.getmOrder(),timeLineModel.getmMarchant(),timeLineModel.getmTime(),timeLineModel.getmDeliver_at(),currentDateTimeString,OrderStatus.ACTIVE,timeLineModel.getCash()));
                mFeedList.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition,mFeedList.size());
            }
        });

    }


    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }


}
