package com.masaga.goyo.initials;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyo.R;
import com.masaga.goyo.common.Checker;
import com.masaga.goyo.gloabls.Global;
import com.masaga.goyo.goyoschool.MainActivity;
import com.masaga.goyo.model.model_loginusr;
import com.masaga.goyo.utils.SHP;
import com.masaga.goyo.utils.common;

import java.lang.reflect.Type;
import java.util.List;

public class sessionchecker extends AppCompatActivity {
    boolean isLanguageShow = false;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessionchecker);
        isLanguageShow = !common.setLanguage(getApplicationContext(), getBaseContext());
        loader = new ProgressDialog(sessionchecker.this);
        setTitle("Login Checking...");
    }

    private void checkLogin() {
        if (!SHP.get(sessionchecker.this, SHP.ids.sessionid, "").toString().equals("")) {

            String sessionid = SHP.get(sessionchecker.this, SHP.ids.sessionid, "-1").toString();
            String uid = SHP.get(sessionchecker.this, SHP.ids.uid, "-1").toString();

            JsonObject json = new JsonObject();
            json.addProperty("base", "_sid");
            json.addProperty("sid", sessionid);
            //json.addProperty("uid", uid);
            json.addProperty("type", "driver");
            json.addProperty("otherdetails", "{}");
            json.addProperty("src", "m");
            loader.setCancelable(false);
            loader.setMessage("Session Login. Please wait.");
            loader.show();
            Ion.with(this)
                    .load(Global.urls.getlogin.value)
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            try {
                                if (result != null) {
                                    //Log.v("result", result.toString())
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<model_loginusr>>() {
                                    }.getType();
                                    List<model_loginusr> login = (List<model_loginusr>) gson.fromJson(result.get("data"), listType);
                                    if (login.size() > 0) {
                                        loader.setMessage("Auto Login...");
                                        Global.loginusr = login.get(0);
                                        if (Global.loginusr.getStatus() == 1) {
                                            Toast.makeText(sessionchecker.this, "Auto Login Successfully!", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(sessionchecker.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(sessionchecker.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                            nextScreen();
                                        }
                                    } else {
                                        Toast.makeText(sessionchecker.this, "Oops there is some issue! please login later!", Toast.LENGTH_SHORT).show();
                                        nextScreen();
                                    }
                                } else {
                                    if (e.getMessage().contains("Time")) {
                                        Toast.makeText(sessionchecker.this, "Auto Login Failed!", Toast.LENGTH_SHORT).show();
                                        nextScreen();
                                    } else {
                                        Toast.makeText(sessionchecker.this, "Auto Login Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        nextScreen();
                                    }
                                    return;
                                }

                            } catch (Exception ea) {
                                Toast.makeText(sessionchecker.this, "Error: " + ea.getMessage(), Toast.LENGTH_LONG).show();
                                ea.printStackTrace();
                                nextScreen();
                            }
                            loader.hide();
                        }
                    });


        } else {
            nextScreen();
        }
    }

    private void nextScreen() {
        Intent i;
        if (isLanguageShow) {
            i = new Intent(sessionchecker.this, com.masaga.goyo.settings.languages.class);
            i.putExtra("frmsplash", true);
        } else {
            i = new Intent(sessionchecker.this, login.class);
        }
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Checker(this).pass(new Checker.Pass() {
            @Override
            public void pass() {
                checkLogin();
            }

        }).check(Checker.Resource.NETWORK);
    }
}
