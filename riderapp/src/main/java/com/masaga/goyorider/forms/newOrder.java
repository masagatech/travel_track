package com.masaga.goyorider.forms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.masaga.goyorider.R;
import com.masaga.goyorider.adapters.AllOrdersAdapter;
import com.masaga.goyorider.adapters.NewOrderAdapter;
import com.masaga.goyorider.adapters.pending_order_adapter;
import com.masaga.goyorider.database.SQLBase;
import com.masaga.goyorider.database.Tables;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.model.model_new_order;
import com.masaga.goyorider.model.model_notification;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.masaga.goyorider.R.id.Time;
import static com.masaga.goyorider.R.id.auto;

public class newOrder extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private com.masaga.goyorider.adapters.NewOrderAdapter mTimeLineAdapter;
    List<model_notification> lst;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    SQLBase db;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = getIntent();
//        int isFromDashboard = intent.getExtras().getInt("FromDashboard");
//        if(isFromDashboard!=1){
//            Toast.makeText(this,"Start from Dashboard",Toast.LENGTH_SHORT).show();
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        }

        setContentView(R.layout.activity_new_order);


         db= new SQLBase(this) ;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().hide();
        }

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = true;

        setTitle(getResources().getString(R.string.New_Order));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
//


    }

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void initView() {
        if (lst.size() > 0) {
            findViewById(R.id.txtNodata).setVisibility(View.GONE);
            mTimeLineAdapter = new NewOrderAdapter(lst, mOrientation, mWithLinePadding, db);
            mRecyclerView.setAdapter(mTimeLineAdapter);

        } else {
            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
        }

    }

    private void setDataListItems(){

        lst.clear();
        JsonParser parser = new JsonParser();
        List<HashMap<String,String>> d = db.NOTIFICATION_GET();
        for (int i=0;i<=d.size() -1; i++){
            try{
                String autoid = "", createon = "", data = "";
                autoid =  d.get(i).get(Tables.tblnotification.autoid);
                createon = d.get(i).get(Tables.tblnotification.createon);
                data = d.get(i).get(Tables.tblnotification.data);

                Date date2 = simpleDateFormat.parse(createon);
                Date date1  = new Date();
                long difference = date1.getTime() - date2.getTime();
                Integer minutes = (int) (difference / (1000 * 60));
                long milisec = (long) (difference);
                if(minutes > Global.RedAlert){
                    db.NOTIFICATION_DELETE(autoid);
                }else {


                    JsonObject obj = parser.parse(data).getAsJsonObject();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<model_notification>() {
                    }.getType();
                    model_notification m = gson.fromJson(obj, listType);
                    m.autoid = Integer.parseInt(autoid);
                    m.createdon = date2;
                    m.remaintime = (Global.RedAlert * 60* 1000) - milisec;
                    lst.add(m);
                }

            }catch (Exception e){

            }



        }

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

    @Override
    public void onResume(){
        super.onResume();

        if(lst == null){
            lst = new ArrayList<>();
            setDataListItems();
            initView();
        }else {
            setDataListItems();
            mTimeLineAdapter.notifyDataSetChanged();
        }



    }

    @Override
    public void onStop(){
        super.onStop();
        if(mTimeLineAdapter!=null){
            mTimeLineAdapter.Kill();
        }

       //db.close();
    }
}
