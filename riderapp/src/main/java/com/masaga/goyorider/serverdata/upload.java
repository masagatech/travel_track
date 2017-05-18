package com.masaga.goyorider.serverdata;

import android.content.ContentValues;
import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.masaga.goyorider.Interface.DataUploadTaskListner;
import com.masaga.goyorider.common.DataUploadTask;
import com.masaga.goyorider.database.SQLBase;
import com.masaga.goyorider.database.Tables;
import com.masaga.goyorider.gloabls.Global;
import com.masaga.goyorider.utils.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mTech on 07-Mar-2017.
 */
public class upload implements DataUploadTaskListner<String> {
    SQLBase db;
    public void checkServerDataDriverInfo(final Context c){
        /*Database variable*/
        db = new SQLBase(c);
        try {

            List<HashMap<String, String>> data = db.DRIVER_INFO_GET("where " + Tables.tbl_driver_info.sentToserver + " = " + "'false'");

            if(data.size() > 0){
                for (int i = 0; i < data.size(); i++) {

                    final HashMap<String, String> _d = data.get(i);

                    ContentValues extra = new ContentValues();
                    extra.put("mod", "drvinfo");
                    extra.put("id", _d.get(Tables.tbl_driver_info.autoid));
                    extra.put("isfinal", data.size() == i ? true : false);

                    ContentValues _val = new ContentValues();
                    _val.put(Tables.tbl_driver_info.autoid,_d.get(Tables.tbl_driver_info.autoid));
                    _val.put(Tables.tbl_driver_info.mibuid,_d.get(Tables.tbl_driver_info.mibuid));
                    _val.put(Tables.tbl_driver_info.adharno,_d.get(Tables.tbl_driver_info.adharno));
                    _val.put(Tables.tbl_driver_info.sarthinm,_d.get(Tables.tbl_driver_info.sarthinm));
                    _val.put(Tables.tbl_driver_info.mob1,_d.get(Tables.tbl_driver_info.mob1));
                    _val.put(Tables.tbl_driver_info.mob2,_d.get(Tables.tbl_driver_info.mob2));
                    _val.put(Tables.tbl_driver_info.ownrship,_d.get(Tables.tbl_driver_info.ownrship));
                    _val.put(Tables.tbl_driver_info.howmny,_d.get(Tables.tbl_driver_info.howmny));
                    _val.put(Tables.tbl_driver_info.driving,_d.get(Tables.tbl_driver_info.driving));
                    _val.put(Tables.tbl_driver_info.vehtype,_d.get(Tables.tbl_driver_info.vehtype));
                    _val.put(Tables.tbl_driver_info.vehno,_d.get(Tables.tbl_driver_info.vehno));
                    _val.put(Tables.tbl_driver_info.yrsold,_d.get(Tables.tbl_driver_info.yrsold));
                    _val.put(Tables.tbl_driver_info.btchno,_d.get(Tables.tbl_driver_info.btchno));
                    _val.put(Tables.tbl_driver_info.alruseing,_d.get(Tables.tbl_driver_info.alruseing));
                    _val.put(Tables.tbl_driver_info.goyointr, _d.get(Tables.tbl_driver_info.goyointr));
                    _val.put(Tables.tbl_driver_info.doyohv, _d.get(Tables.tbl_driver_info.doyohv));
                    _val.put(Tables.tbl_driver_info.prefloc,_d.get(Tables.tbl_driver_info.prefloc));
                    _val.put(Tables.tbl_driver_info.servrid,_d.get(Tables.tbl_driver_info.servrid));
                    _val.put(Tables.tbl_driver_info.remarks,_d.get(Tables.tbl_driver_info.remarks));
                    _val.put(Tables.tbl_driver_info.createon,_d.get(Tables.tbl_driver_info.createon));
                    _val.put(Tables.tbl_driver_info.createdby,_d.get(Tables.tbl_driver_info.createdby));
                    _val.put(Tables.tbl_driver_info.lat,_d.get(Tables.tbl_driver_info.lat));
                    _val.put(Tables.tbl_driver_info.lon,_d.get(Tables.tbl_driver_info.lon));
                    _val.put(Tables.tbl_driver_info.profpic,_d.get(Tables.tbl_driver_info.profpic));
                    _val.put("ismob",true);
                    _val.put("devid", common.deviceId(c));

                    if(!_d.get(Tables.tbl_driver_info.profpic_upload).equalsIgnoreCase("true")){
                        Ion.with(c).load("POST", Global.urls.uploadimage.value ).uploadProgressHandler(new ProgressCallback() {

                            @Override
                            public void onProgress(long uploaded, long total) {
                                System.out.println("uploaded " + (int) uploaded + " Total: " + total);

                            }
                            }).setMultipartParameter("platform", "android")
                                .setMultipartParameter("id", _d.get(Tables.tbl_driver_info.autoid))
                                .setMultipartFile("image", new File(Global.ExternalPath.getAbsolutePath()+ Global.Image_Path +"/"+_d.get(Tables.tbl_driver_info.profpic))).asString().setCallback(
                                new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    try {
                                        if(result != null){
                                            JSONObject jsnobject = new JSONObject(result);
                                            SQLBase _d = new SQLBase(c);
                                            _d.DRIVER_INFO_UPDATE_File_OFFLINE(jsnobject.get("data").toString());
                                            _d.close();
                                        }
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                        });
                    }
                    new DataUploadTask(c, Global.urls.savedriverinfo, Integer.parseInt(_d.get(Tables.tbl_driver_info.autoid)),
                            upload.this, _val,
                            extra).execute();

                }
            }else{
                db.close();
            }

        }catch (Exception ex){
            if(db!=null){
                db.close();
            }
        }finally {

        }



    }




    @Override
    public void onPostSuccess(String result, int id, boolean isSucess) {

    }

    @Override
    public void onPostSuccess(String result, int id, boolean isSucess, ContentValues tag) {
        if(tag.get("mod").toString() == "drvinfo"){
            try {
                JSONObject jsnobject = new JSONObject(result);
                JSONArray _data = jsnobject.getJSONArray("data");
                JSONObject funsave_driverinfo = _data.getJSONObject(0).getJSONObject("funsave_driverinfo");
                String Serverid = funsave_driverinfo.get("keyid").toString();
                String mbautoid = funsave_driverinfo.get("mbautoid").toString();
                if(mbautoid.equals(tag.get("id").toString())) {
                    db.DRIVER_INFO_UPDATE_OFFLINE(tag.get("id").toString(), "true", Serverid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(tag.get("isfinal").equals(true)){
                db.close();
            }
        }

    }

    @Override
    public void onPostError(int id, int error) {

    }
}
