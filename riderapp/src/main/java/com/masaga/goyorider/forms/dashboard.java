package com.masaga.goyorider.forms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.hanks.htextview.HTextView;
import com.masaga.goyorider.R;

import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.text1;

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

    private PopupWindow OrderPopup;
    private Button Btn_Accept, Btn_Reject;
    private TextView Deliver_at_Text;
    private TextView PopUp_CountText;
    final Popup_Counter CountTimer = new Popup_Counter(180000,1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.rider_online_switch);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

}

    private void initiatePopupWindow() {
        try {
            String role;
            role="db";
            LayoutInflater inflater=(LayoutInflater) dashboard.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_orderummery,
                    (ViewGroup) findViewById(R.id.PopUp));
            OrderPopup= new PopupWindow(layout,850,700,true);
            OrderPopup.showAtLocation(layout, Gravity.CENTER,0,0);
            OrderPopup.setOutsideTouchable(true);
            OrderPopup.setFocusable(true);
            CountTimer.start();

            Btn_Accept=(Button)layout.findViewById(R.id.Accept_order);
            Btn_Reject=(Button)layout.findViewById(R.id.Reject_Order);
            PopUp_CountText=(TextView)layout.findViewById(R.id.popup_counter);
            Deliver_at_Text=(TextView)layout.findViewById(R.id.Deliver_at);

            //If User role = "TL" or "Admin" Show address else don't show address
            if(role.toLowerCase()=="db")
                Deliver_at_Text.setText("xxx");
            else
                Deliver_at_Text.setText("Actual First Delivery Location");


            Btn_Accept.setOnClickListener(accept_order_click);
            Btn_Reject.setOnClickListener(reject_order_click);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
    private View.OnClickListener accept_order_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderPopup.dismiss();
        }
    };
    private View.OnClickListener reject_order_click= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderPopup.dismiss();
        }
    };

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
        initiatePopupWindow();
        //Intent intent=new Intent(this,complated_order.class);
        //startActivity(intent);
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
    public class Popup_Counter extends CountDownTimer{

        public Popup_Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            OrderPopup.dismiss();
        }

        @Override
        public void onTick(long millisUntilFinished) {
           PopUp_CountText.setText(""+String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
        }
    }
}
