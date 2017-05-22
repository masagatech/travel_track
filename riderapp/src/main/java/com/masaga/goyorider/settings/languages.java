package com.masaga.goyorider.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import com.masaga.goyorider.goyorider.MainActivity;
import com.masaga.goyorider.R;
import com.masaga.goyorider.utils.SHP;
import com.masaga.goyorider.utils.common;
import com.masaga.goyorider.initials.login;

import java.util.ArrayList;
import java.util.HashMap;

public class languages extends AppCompatActivity {
    languages m_this;
    ListView lv;
    Button btnChange;
    boolean isFromSplash= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        getBundleExtra();
        setTitle(getResources().getString(R.string.language_title));

        m_this = this; // set to this activity in local variable
        initializeAllControls();

        bindListView();


    }

    private void getBundleExtra(){
        Bundle b =   getIntent().getExtras();
        if(b!=null){
            isFromSplash =  b.getBoolean("frmsplash");
        }
    }

    //intialize all controls
    private void initializeAllControls(){
        if(!isFromSplash)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView)m_this.findViewById(R.id.lstLanguages);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        btnChange = (Button)m_this.findViewById(R.id.btnChange);
    }

    private void bindListView(){
        final ArrayList<HashMap<String, Object>> m_data = dataSource();
        final SimpleAdapter adapter = new SimpleAdapter(m_this,
                m_data,
                R.layout.layout_radiolist,
                new String[] {"maintext","subtext" ,"checked"},
                new int[] {R.id.tv_MainText,R.id.tv_SubText,  R.id.rb_Choice});


        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (data == null) //if 2nd line text is null, its textview should be hidden
                {
                    view.setVisibility(View.GONE);
                    return true;
                }
                view.setVisibility(View.VISIBLE);
                return false;
            }


        });

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                RadioButton rb = (RadioButton) v.findViewById(R.id.rb_Choice);
                if (!rb.isChecked()) //OFF->ON
                {
                    for (HashMap<String, Object> m : m_data) //clean previous selected
                        m.put("checked", false);
                    m_data.get(arg2).put("checked", true);
                    adapter.notifyDataSetChanged();
                }
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int r = -1;
                String lg = "";
                for (int i = 0; i < m_data.size(); i++) //clean previous selected
                {
                    HashMap<String, Object> m = m_data.get(i);
                    Boolean x = (Boolean) m.get("checked");
                    if (x == true) {
                        r = i;
                        lg = (String) m.get("subtext");
                        break; //break, since it's a single choice list
                    }
                }
                SHP.set(getApplicationContext(), SHP.ids.sett_lang, lg);
                common.setLanguage(getApplicationContext(), getBaseContext());
                Intent intent;
                if(isFromSplash){
                    intent = new Intent(getApplicationContext(), login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }else{
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
                finish();
            }
        });

    }



    private ArrayList<HashMap<String, Object>> dataSource(){
        final ArrayList<HashMap<String, Object>> m_data = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> en = new HashMap<String, Object>();
        en.put("maintext", "English");
        en.put("subtext", "en");
        m_data.add(en);

        HashMap<String, Object> gu = new HashMap<String, Object>();
        gu.put("maintext", "ગુજરાતી");// no small text of this item!
        gu.put("subtext", "gu");// no small text of this item!
        m_data.add(gu);

       /* HashMap<String, Object> hi = new HashMap<String, Object>();
        hi.put("maintext", "हिंदी");
        hi.put("subtext", "hi");
        m_data.add(hi);

        HashMap<String, Object> ma = new HashMap<String, Object>();
        ma.put("maintext", "मराठी");
        ma.put("subtext", "ma");
        m_data.add(ma);*/

        String strLang = SHP.get(getApplicationContext(), SHP.ids.sett_lang, "en").toString();
        String Val = common.Language(strLang);


        for (HashMap<String, Object> m :m_data) //make data of this view should not be null (hide )
        {
            if(m.get("subtext").equals(strLang)){
                m.put("checked", true);
            }else{
                m.put("checked", false);
            }
        }
        //end init data

        return m_data;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
