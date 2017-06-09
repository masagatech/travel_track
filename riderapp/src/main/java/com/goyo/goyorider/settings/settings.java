package com.goyo.goyorider.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goyo.goyorider.R;
import com.goyo.goyorider.utils.SHP;
import com.goyo.goyorider.utils.common;

public class settings extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout relLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.settings_title));
        initializeControls();
        setListners();
        fillSettings();

    }

    private void initializeControls(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relLanguage = (RelativeLayout)findViewById(R.id.relLanguage);
    }

    private void setListners(){
        relLanguage.setOnClickListener(this);
    }

    private void fillSettings(){
        setSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.relLanguage:
                startActivity(new Intent(settings.this, languages.class));
                break;

        }
    }

    private void setSettings(){
        TextView  tv_selectedlang=(TextView)relLanguage.findViewById(R.id.tv_selectedlang);

        String strLang = SHP.get(getApplicationContext(), SHP.ids.sett_lang, "en").toString();
        String Val = common.Language(strLang);
        tv_selectedlang.setText(Val);

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
