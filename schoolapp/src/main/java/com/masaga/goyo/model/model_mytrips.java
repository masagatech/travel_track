package com.masaga.goyo.model;
import com.google.gson.annotations.SerializedName;
/**
 * Created by mTech on 22-Apr-2017.
 */
public class model_mytrips {

    @SerializedName("id")
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

    @SerializedName("trpid")
    public String tripid;

    public model_mytrips()
    {}
}
