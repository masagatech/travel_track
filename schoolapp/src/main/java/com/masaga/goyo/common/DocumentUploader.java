package com.masaga.goyo.common;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.masaga.goyo.gloabls.Global;

import java.io.File;

/**
 * Created by mTech on 11-Mar-2017.
 */
public class DocumentUploader {
    public void UploadImages(Context c){
//        Ion.with(c).load("POST", Global.DOMAIN_URL + "/uploads").uploadProgressHandler(new ProgressCallback() {
//            @Override
//            public void onProgress(long uploaded, long total) {
//                System.out.println("uploaded " + (int) uploaded + " Total: " + total);
//            }
//        }).setMultipartParameter("platform", "android").setMultipartFile("image", new File(fileUri.getPath())).asString().setCallback(new FutureCallback<String>() {
//            @Override
//            public void onCompleted(Exception e, String result) {
//
//            }
//        });
    }
}
