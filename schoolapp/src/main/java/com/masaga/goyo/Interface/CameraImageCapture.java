package com.masaga.goyo.Interface;

import android.graphics.Bitmap;

/**
 * Created by mTech on 09-Mar-2017.
 */
public interface CameraImageCapture {
    void onCapture(Bitmap result);
    void onCancel(String result);
    void onError(String result);

}
