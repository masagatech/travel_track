package com.goyo.goyorider.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fajar on 03-Jun-17.
 */

public class model_notification {


    public Integer autoid;
    public Date createdon;
    public long remaintime;

    @SerializedName("ordid")
    public String ordid;
    @SerializedName("olnm")
    public String olnm;
    @SerializedName("stops")
    public String stops;
    @SerializedName("pcktm")
    public String pcktm;
    @SerializedName("amt")
    public String amt;

    @SerializedName("expmin")
    public int expmin;

    public boolean isExpired = false;



}
