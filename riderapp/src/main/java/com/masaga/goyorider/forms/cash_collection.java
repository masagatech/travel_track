package com.masaga.goyorider.forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.masaga.goyorider.R;
import com.masaga.goyorider.adapters.CashCollectionAdapter;
import com.masaga.goyorider.adapters.ComplatedOrderAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class cash_collection extends AppCompatActivity {

    private ListView mRecyclerView;
    private com.masaga.goyorider.adapters.CashCollectionAdapter mTimeLineAdapter;
    private List<CashModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.drawable.rider_del);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }


//        mOrientation = Orientation.VERTICAL;
//        mWithLinePadding = true;

        setTitle(getResources().getString(R.string.Cash_Collection));

        List<CashModel> cl = new ArrayList<>();
        cl.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1986", 122.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1985", 140.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1983", 422.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1925", 122.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1925", 122.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1925", 122.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1942", 822.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1923", 190.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1923", 190.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1923", 190.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1923", 190.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1925", 100.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1963", 722.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Navi Mumbai", "#1996", 22.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, West Vikroli", "#198", 99.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, West Vikroli", "#198", 33.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, West Vikroli", "#198", 1110.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, West Vikroli", "#1986", 122.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, West Vikroli", "#1985", 140.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, West Vikroli", "#1983", 422.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, West Vikroli", "#1925", 122.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Thane", "#1942", 822.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Thane", "#1923", 190.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Thane", "#1925", 100.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Thane", "#1963", 722.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, Thane", "#1996", 22.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, East Vikroli", "#198", 99.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, East Vikroli", "#198", 33.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, East Vikroli", "#198", 1110.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, East Vikroli", "#198", 1110.00, "Handed Over", 0));
        cl.add(new CashModel("Pizza Hut, East Vikroli", "#198", 1110.00, "Handed Over", 0));
        List<CashModel> sorted = lstSortedData(cl);

        ListView Cash_Collection = (ListView) findViewById(R.id.recyclerView);
        CashCollectionAdapter adapter = new CashCollectionAdapter(this, sorted);

        Cash_Collection.setAdapter(adapter);


//        mRecyclerView = (ListView) findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(getLinearLayoutManager());
//        mRecyclerView.setHasFixedSize(true);
////
//        initView();

//    private LinearLayoutManager getLinearLayoutManager() {
//        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//    }
//
//    private void initView() {
//        setDataListItems();
//        mTimeLineAdapter = new CashCollectionAdapter(mDataList, mOrientation, mWithLinePadding);
//        mRecyclerView.setAdapter(mTimeLineAdapter);
//    }
//
//    private void setDataListItems() {
//        mDataList.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1986", 122.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1985", 140.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1983", 422.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Pralhad Nagar", "#1925", 122.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Navi Mumbai", "#1942", 822.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Navi Mumbai", "#1923", 190.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Navi Mumbai", "#1925", 100.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Navi Mumbai", "#1963", 722.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, Navi Mumbai", "#1996", 22.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, West Vikroli", "#198", 99.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, West Vikroli", "#198", 33.00, "Team Leader"));
//        mDataList.add(new CashModel("Pizza Hut, West Vikroli", "#198", 1110.00, "Team Leader"));
//
   }
//

    private List<CashModel> lstSortedData(List<CashModel> cl) {
        List<CashModel> l = new ArrayList<>();
        String Header = "";
        double Total=0;
        CashModel header = null;
        for (int i = 0; i <= cl.size() - 1; i++) {
            CashModel re = cl.get(i);

            if (!Header.equals(re.getmMarchant())) {

                if(header!=null){
                    header.setmCashAmount(Total);
                    Total = 0;
                }


                CashModel cl1 = new CashModel(re.getmMarchant(),re.getmOrderID(),re.getmCashAmount(),re.getmHandover(),re.getmType());;
                header = cl1;

                cl1.setmType(1);
                l.add(cl1);
                Header = cl1.getmMarchant();
            }
            Total += re.getmCashAmount();
            re.setmType(0);
            l.add(re);

            if(i == cl.size()){
                header.setmCashAmount(Total);
            }
        }
        return l;
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

    //  @Override
    // protected void onSaveInstanceState(Bundle savedInstanceState) {
    //   if(mOrientation!=null)
    //         savedInstanceState.putSerializable(MainActivity.EXTRA_ORIENTATION, mOrientation);
    //    super.onSaveInstanceState(savedInstanceState);
    // }

    // @Override
    // protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //    if (savedInstanceState != null) {
    //        if (savedInstanceState.containsKey(MainActivity.EXTRA_ORIENTATION)) {
    //            mOrientation = (Orientation) savedInstanceState.getSerializable(MainActivity.EXTRA_ORIENTATION);
    //        }
    //     }
    //     super.onRestoreInstanceState(savedInstanceState);
    // }
}

