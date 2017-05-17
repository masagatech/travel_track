package com.masaga.goyo.common;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.util.Log;


import com.masaga.goyo.gloabls.Global;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by mTech on 04-Mar-2017.
 */
@SuppressLint("SimpleDateFormat")
public class HttpData {
    int timeout = 50000;
    public HttpPost post;

    public InputStream connectionEstablished(String mUrl, ContentValues nameValuePairs) throws Exception {
        //GlobalVariable.ConnectionError = "";
        InputStream mInputStreamis = null;
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), timeout); // Timeout Limit
        HttpConnectionParams.setSoTimeout(client.getParams(), timeout);
        HttpResponse response;
        try {
            post = new HttpPost(mUrl);
            if (nameValuePairs != null) {
                try {
                    JSONObject json = new JSONObject();
                    for (String key : nameValuePairs.keySet()) {
                        try {
                            json.put(key, nameValuePairs.get(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    post.setHeader("Content-Type", "application/json;charset=UTF-8");
                    post.setEntity(new StringEntity(json.toString()));
                    Log.e("String Entitiy", "<><><><> " + nameValuePairs.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            response = client.execute(post);
            /* Checking response */
            if (response != null) {
                mInputStreamis = response.getEntity().getContent(); // Get the entity.
            }

        } catch (ConnectTimeoutException e) {
            //GlobalVariable.ConnectionError = "Connection Timeout. Try again.";
            Log.e("HTTP Timeout exception", "Error  ===== " + e.toString());
            throw e;

        } catch (HttpHostConnectException e) {
            Log.e("HTTP Conn exception", "Error  ===== " + e.toString());
            throw e;
            //GlobalVariable.ConnectionError = "Unable to connect to url. Contact to administrator.";
        } catch (Exception e) {
            //GlobalVariable.ConnectionError = "Error while connecting. Try again.";
            Log.e("Caught in exception", "Error  ===== " + e.toString());
            throw e;
        }
        return mInputStreamis;
    }

    public HttpResult CallService(Global.urls mUrl, ContentValues nameValuePairs){
        return this.CallService(mUrl.value, nameValuePairs);
    }

    public HttpResult CallService(String mUrl, ContentValues nameValuePairs) {
        HttpResult res = new HttpResult();
        res.setIsSuccess(false);
        InputStream mInputStreamis = null;
        try {
            mInputStreamis = connectionEstablished(mUrl, nameValuePairs);
            res.setData(converResponseToString(mInputStreamis));
            res.setIsSuccess(true);
        } catch (Exception e) {
            res.setData(e.getMessage());
            res.setIsSuccess(false);
            e.printStackTrace();
        }
        return res;
    }

    public String converResponseToString(InputStream InputStream) {
        String mResult = "";
        StringBuilder mStringBuilder;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    InputStream, "UTF-8"), 8);
            mStringBuilder = new StringBuilder();
            mStringBuilder.append(reader.readLine() + "\n");
            String line = "0";
            while ((line = reader.readLine()) != null) {
                mStringBuilder.append(line + "\n");
            }
            InputStream.close();
            mResult = mStringBuilder.toString();

            return mResult;
        } catch (Exception e) {

            return mResult;
        }
    }

    public void close() {
        try {
            post.abort();
        } catch (Exception ex) {
            Log.e("HTTP Error", ex.getMessage());
        }


    }

}
