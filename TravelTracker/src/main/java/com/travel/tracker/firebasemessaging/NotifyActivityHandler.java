package com.travel.tracker.firebasemessaging;

import android.app.Activity;
import android.os.Bundle;

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

            } else if (action.equals("Reject")) {
                // close current notification
            }
        }

        finish();
    }
}
