package com.masaga.goyorider.gloabls;

import android.app.ProgressDialog;
import android.os.Environment;

import com.masaga.goyorider.model.model_appsettings;
import com.masaga.goyorider.model.model_crewdata;
import com.masaga.goyorider.model.model_loginusr;

import java.io.File;
import java.net.URL;

/**
 * Created by mTech on 04-Mar-2017.
 */
public class Global {
    //public static String DOMAIN_URL = "http://192.168.1.16:8081/goyoapi";

//    public final static String REST_URL = "http://13.126.2.220:8082/goyoapi";
//    public static final String SOCKET_URL = "http://13.126.2.220:8082/";

    public final static String REST_URL = "http://192.168.43.10:8082/goyoapi";
    public static final String SOCKET_URL = "http://192.168.43.10:8082/";


    private final static String APIName="/mrcht";

 //  public  static final String Socket_Url= "http://35.154.230.244:8082/";
   //    public static final String SOCKET_URL = "http://35.154.230.244:8082/";
    //public  static final String Socket_URL="http://35.154.230.244:8082/";

    public static File ExternalPath = Environment.getExternalStorageDirectory();
    public final static String Image_Path = "/goyo_images";


    public enum urls {
        testurl("testurl", REST_URL),
        uploadimage("", Global.REST_URL + "/uploads"),
        getlogin("getlogin", REST_URL + "/getLogin"),
        getlogout("getlogout", REST_URL + "/getLogout"),
        savedriverinfo("savedriverinfo", REST_URL + "/savedriverinfo"),
        getmytrips("getmytrips", REST_URL + "/tripapi"),
        starttrip("starttrip", REST_URL + "/tripapi/start04"),
        stoptrip("stoptrip", REST_URL + "/tripapi/stop"),
        picdropcrew("picdropcrew", REST_URL + "/tripapi/picdropcrew"),
        storetripdelta("storetripdelta", REST_URL + "/tripapi/storedelta"),
        getmytripscrew("getmytripscrew", REST_URL + "/tripapi/crews"),
        getmykids("getmykids", REST_URL + "/cust/getmykids"),
        getlastknownloc("getlastknownloc", REST_URL + "/tripapi/getdelta"),
        sendreachingalert("sendreachingalert", REST_URL + "/tripapi/sendreachingalert"),
        getOrderDetails("getorderdetails", REST_URL + APIName+"/getOrderDetails"),
        getOrderDash("getorderdash", REST_URL + APIName+"/getOrderDash"),
        saveLiveBeat("saveLiveBeat", REST_URL + APIName+"/saveLiveBeat"),
        getOrders("getOrders", REST_URL + APIName+"/getOrders"),
        getStatus("getStatus", REST_URL + APIName+"/getStatus"),
        setStatus("setStatus", REST_URL + APIName+"/setStatus"),
        setTripAction("setTripAction", REST_URL + APIName+"/setTripAction"),
        getNotify("getNotify", REST_URL + APIName+"/getNotify");





        public String key;
        public String value;

        private urls(String toKey, String toValue) {
            key = toKey;
            value = toValue;
        }

    }

    public final static String start = "1";
    public final static String done = "2";
    public final static String pause = "pause";
    public final static String cancel = "3";
    public final static String pending = "0";
    public final static Integer RedAlert = 10;
    public final static String pickedupdrop = "1";
    public final static String absent = "2";

    public static model_crewdata crewDetails;

    public static model_loginusr loginusr;

    public static model_appsettings appsettings;
    public static ProgressDialog prgdialog;

    public static void showProgress(ProgressDialog prd) {
        prd.setCancelable(false);
        if (!prd.isShowing()) prd.show();
    }

    public static void hideProgress(ProgressDialog prd) {
        prd.dismiss();
    }
}
