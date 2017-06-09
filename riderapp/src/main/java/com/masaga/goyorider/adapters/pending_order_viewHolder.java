package com.masaga.goyorider.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.vipulasri.timelineview.TimelineView;
import com.masaga.goyorider.R;
import com.masaga.goyorider.forms.PendingOrdersView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fajar on 22-May-17.
 */

public class pending_order_viewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_timeline_date)
    TextView mDate;
    @BindView(R.id.text_timeline_title)
    TextView mOrder;
    @BindView(R.id.text_marchent)
    TextView mMarchant;
    @BindView(R.id.Time)
    TextView mTime;
    @BindView(R.id.Deliver_at)
    TextView mDeliver_at;
    @BindView(R.id.Custmer_name)
    TextView Custmer_name;
    @BindView(R.id.Remark)
    TextView Remark;
    @BindView(R.id.Btn_Call)
    ImageButton Btn_Call;
    @BindView(R.id.Btn_Delivery)
    Button Btn_Delivery;
    @BindView(R.id.Btn_map)
    ImageButton Btn_Map;
    @BindView(R.id.remarkhide)
    ImageButton ArrowRemark;
    @BindView(R.id.Btn_Return)
    Button Btn_Return;
    @BindView(R.id.Btn_AcceptReject)
    ImageButton Btn_AcceptReject;
    @BindView(R.id.Collected_Cash)
    EditText collected_cash;
    @BindView(R.id.time_marker)
    TimelineView mTimelineView;
    @BindView(R.id.ClickToHide)
    LinearLayout ClickToHide;
    @BindView(R.id.hideButton)
    ToggleButton ButtonHide;
    @BindView(R.id.border)
    RelativeLayout Border;

    public pending_order_viewHolder(View itemView, int viewType) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);

    }
}

