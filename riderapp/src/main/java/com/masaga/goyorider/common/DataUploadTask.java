package com.masaga.goyorider.common;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.masaga.goyorider.Interface.DataUploadTaskListner;
import com.masaga.goyorider.gloabls.Global;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by mTech on 04-Mar-2017.
 */
public class DataUploadTask extends AsyncTask<String, Void, String> {
    /*Variables*/
    InputStream is = null;
    public JSONObject json;
    DataUploadTaskListner<String> listener;
    Context context;
    InputStream mInputStreamis = null;
    ContentValues nameValuePairs;
    public HttpData sSetconnection;
    boolean isSuccess;
    ContentValues tag;
    Global.urls _url;
    int receivedId=0,errorCode=0;

    public DataUploadTask(Context context, Global.urls url, int receivedId,
                          DataUploadTaskListner<String> listener,
                          ContentValues nameValuePairs,ContentValues tag){

        this.context = context;
        this._url = url;
        this.receivedId = receivedId;
        this.listener = listener;
        this.nameValuePairs=nameValuePairs;
        this.tag=tag;
    }

    @Override
    protected String doInBackground(String... params) {
        String mResult = null;

//		JSONObject json = new JSONObject();

        try {
            mInputStreamis = sSetconnection.connectionEstablished(_url.value, nameValuePairs);
            Log.e("onclick", " " + _url.value);
            Log.e("input stream", " " + mInputStreamis);
            if (mInputStreamis != null) {
                mResult = sSetconnection.converResponseToString(mInputStreamis);
                Log.v("NetworkResult:", "" + mResult);
                isSuccess = true;
            } else {
                //checkResponse = 2;
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        /*if(GlobalVariable.ConnectionError != ""){
                            Toast.makeText(context, GlobalVariable.ConnectionError, Toast.LENGTH_SHORT).show();

                        }*/
//						Toast.makeText(context, "Please Try Again..", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            //checkResponse = 6;
        }
        return mResult;
    }

    @Override
    protected void onPreExecute() {
        sSetconnection = new HttpData();
    }

    /**
     * activateDriverCheckResponse 1 = flag(Email does not Exist) 2 = Error with
     * HTTP connection 3 = Error while convert into string 4 = Failure 5 = Email
     * Already Exist
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onPostExecute(String _result) {
        try {
            if (isSuccess) {
                if(_result==null){
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            /*if(GlobalVariable.ConnectionError != ""){
                                Toast.makeText(context, GlobalVariable.ConnectionError , Toast.LENGTH_SHORT).show();

                            }*/
//					            Toast.makeText(context, "Please Try Again..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Log.e("result><><><>",""+_result);
                if(tag != null){
                    Log.e("recid3", Integer.toString(receivedId) );
                    listener.onPostSuccess(_result, receivedId, isSuccess,tag);
                }else{
                    Log.e("recid", Integer.toString(receivedId) );
                    listener.onPostSuccess(_result, receivedId, isSuccess);
                }
            } else {
                listener.onPostError(receivedId, errorCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
