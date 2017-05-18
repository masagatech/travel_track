package com.masaga.goyorider.forms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.masaga.goyorider.R;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dashboard extends AppCompatActivity {
    
    @BindView(R.id.Pending_Order)
    FrameLayout Pending_Order;
    @BindView(R.id.Complated_Orders)
    FrameLayout Complated_Orders;
    @BindView(R.id.Rejected_Orders)
    FrameLayout Rejected_Orders;
    @BindView(R.id.Cash_Collection)
    FrameLayout Cash_Collection;
    @BindView(R.id.Notifications)
    FrameLayout Notifications;
    @BindView(R.id.All_Order)
    FrameLayout All_Order;
    private int count=0;
    private PopupWindow OrderPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.rider_online_switch);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

        if(count==3){
            initiatePopupWindow();
        }


    }

    private void initiatePopupWindow() {
        try {
            LayoutInflater layoutInflater=(LayoutInflater) dashboard.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout= View.inflate(R.id.Popup)
        }

    }

    @OnClick(R.id.Pending_Order)
    void click(){
        Intent intent=new Intent(this,pending_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.Cash_Collection)
    void click2(){
        Intent intent=new Intent(this,cash_collection.class);
        startActivity(intent);
    }
    @OnClick(R.id.Complated_Orders)
    void click3(){
        count=count+1;
        Intent intent=new Intent(this,complated_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.Rejected_Orders)
    void click4(){
        Intent intent=new Intent(this,rejected_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.All_Order)
    void click5(){
        Intent intent=new Intent(this,all_order.class);
        startActivity(intent);
    }
    @OnClick(R.id.Notifications)
    void click6(){
        Intent intent=new Intent(this,notification.class);
        startActivity(intent);
    }
}
