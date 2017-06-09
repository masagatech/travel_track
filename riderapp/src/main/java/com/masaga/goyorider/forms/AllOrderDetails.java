package com.masaga.goyorider.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.masaga.goyorider.R;
import com.masaga.goyorider.adapters.OrderDetailsAdapter;
import com.masaga.goyorider.model.model_order_details;
import com.masaga.goyorider.model.model_rider_list;

import java.util.ArrayList;

public class AllOrderDetails extends AppCompatActivity {
    private ArrayList<model_order_details> data;
    ListView AllDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order_details);

        if(getSupportActionBar()!=null)
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data= populateList();
        AllDetails=(ListView)findViewById(R.id.all_details);

        OrderDetailsAdapter orderDetailsAdapter=new OrderDetailsAdapter(data,this);
        AllDetails.setAdapter(orderDetailsAdapter);
        orderDetailsAdapter.notifyDataSetChanged();







    }

    public static ArrayList<model_order_details> populateList(){
        ArrayList<model_order_details> mRiderList = new ArrayList<model_order_details>();
        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
        mRiderList.add(new model_order_details("13-5-17",1,0,9,10));
        mRiderList.add(new model_order_details("14-5-17",2,2,9,12));
        mRiderList.add(new model_order_details("15-5-17",6,0,4,10));
        mRiderList.add(new model_order_details("16-5-17",4,3,9,13));
        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));
        mRiderList.add(new model_order_details("12-5-17",4,3,9,13));

        return mRiderList;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
