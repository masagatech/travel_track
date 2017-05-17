package com.masaga.goyo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mTech on 02-May-2017.
 */
public class model_mykids {

    @SerializedName("pid")
    public int id;

    @SerializedName("nm")
    public String nm;

    @SerializedName("date")
    public String date;

    @SerializedName("time")
    public String time;

    @SerializedName("btch")
    public String btch;

    @SerializedName("pd")
    public String pd;

    @SerializedName("sts")
    public String sts;

    @SerializedName("stsi")
    public String stsi;

    @SerializedName("stdsi")
    public String stdsi;

    @SerializedName("trpid")
    public String tripid;

    public model_mykids()
    {}
}
