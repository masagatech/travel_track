package com.travel.tracker.forms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.travel.tracker.adapters.driver_info_listAdapter;
import com.travel.tracker.database.SQLBase;
import com.travel.tracker.database.Tables;
import com.travel.tracker.R;

import java.util.HashMap;
import java.util.List;

public class driver_info_view extends AppCompatActivity {
    /*Database variable*/
    SQLBase db;
    /* form variable */
    ListView lst_driver_info ;
    TextView txtNodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Driver List");
        setContentView(R.layout.activity_driver_info_view);
        initAllControls();
        setListners();
        bindList();
    }

    //set action bar button menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_info_view_activity, menu);
        return true;
    }


    //action bar menu button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_driver_info_view_add:
                Intent i = new Intent(driver_info_view.this, driver_info.class);
                startActivity(i);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    /*fill all controls*/
    private void initAllControls(){
        lst_driver_info  = (ListView)findViewById(R.id.lst_driver_info);
        txtNodata = (TextView)findViewById(R.id.txtNodata);

        db = new SQLBase(getApplicationContext());
    }

    private void bindList(){

        List<HashMap<String, String>> d= db.DRIVER_INFO_GET_GRID(" order by " + Tables.tbl_driver_info.createon + " desc");
        if(d.size()>0){
            findViewById(R.id.txtNodata).setVisibility(View.GONE);
            driver_info_listAdapter adapter=new driver_info_listAdapter(this,d);
            lst_driver_info.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }else{
            findViewById(R.id.txtNodata).setVisibility(View.VISIBLE);
        }



    }

    private  void setListners(){
        lst_driver_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> o = (HashMap<String, String>) parent.getItemAtPosition(position);
                Intent i = new Intent(driver_info_view.this, driver_info.class);
                i.putExtra("autoid",o.get(Tables.tbl_driver_info.autoid));
                startActivity(i);


            }
        });


    }

}
