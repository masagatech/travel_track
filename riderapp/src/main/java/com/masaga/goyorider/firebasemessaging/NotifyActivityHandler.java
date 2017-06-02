package com.masaga.goyorider.firebasemessaging;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.masaga.goyorider.gloabls.Global;

/**
 * Created by fajar on 02-Jun-17.
 */

public class NotifyActivityHandler extends Activity {
    public static final String PERFORM_NOTIFICATION_BUTTON = "perform_notification_button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = (String) getIntent().getExtras().get("do_action");
        if (action != null) {
            if (action.equals("Accept")) {

                Toast.makeText(getApplicationContext(),"Send Data",Toast.LENGTH_SHORT).show();

                    JsonObject json = new JsonObject();
                    json.addProperty("flag", "order");
                    json.addProperty("status", "2");
                    json.addProperty("ordid", "1");
                    json.addProperty("rdid", Global.loginusr.getDriverid() + "");

                    Ion.with(this)
                            .load(Global.urls.setStatus.value)
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    try {
                                        if (result != null) Log.v("result", result.toString());
                                    }
                                    catch (Exception ea) {
                                        ea.printStackTrace();
                                    }
                                }
                            });
            } else if (action.equals("Reject")) {
                // close current notification
            }
        }

        finish();
    }
}
