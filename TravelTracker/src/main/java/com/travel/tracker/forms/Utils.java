package com.travel.tracker.forms;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by fajar on 22-May-17.
 */

public class Utils {

    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}
