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
import com.masaga.goyorider.utils.VectorDrawableUtils;

import java.util.List;

import static com.masaga.goyorider.forms.PendingOrdersView.getTimeLineViewType;

/**
 * Created by fajar on 22-May-17.
 */

public class pending_order_adapter extends RecyclerView.Adapter<pending_order_viewHolder> {

    private List<PendingModel> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public pending_order_adapter(List<PendingModel> feedList, Orientation orientation, boolean withLinePadding) {
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
    public void onBindViewHolder(final pending_order_viewHolder holder, final int position) {

        final PendingModel timeLineModel = mFeedList.get(position);

        if(timeLineModel.getmStatus() == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.getmStatus() == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorAccent));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorAccent));
        }

        holder.mDate.setText(timeLineModel.getmDate());
        holder.mOrder.setText(timeLineModel.getmOrder());
        holder.mMarchant.setText(timeLineModel.getmMarchant());
        holder.mDeliver_at.setText(timeLineModel.getmDeliver_at());
        holder.mTime.setText(timeLineModel.getmTime());
        holder.collected_cash.setText("₹ " +timeLineModel.getCash());
        final int newPosition = holder.getAdapterPosition();

        holder.Btn_Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PendingModel status = mFeedList.get(position+1);
                status.setmStatus(OrderStatus.ACTIVE);
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
