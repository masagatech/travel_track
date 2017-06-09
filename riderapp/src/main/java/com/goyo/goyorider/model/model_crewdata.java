package com.goyo.goyorider.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mTech on 22-Apr-2017.
 */
public class model_crewdata {

    @SerializedName("id")
    public int id;

    @SerializedName("stdid")
    public String stdid;

    @SerializedName("stdnm")
    public String stdnm;

    @SerializedName("parname")
    public String parname;

    @SerializedName("fthrmob")
    public String fthrmob;

    @SerializedName("mthrmob")
    public String mthrmob;

    @SerializedName("addr")
    public String addr;

    @SerializedName("loc")
    public geo loc;

    @SerializedName("typ")
    public String typ;

    @SerializedName("stsi")
    public String stsi;

    public int isNotify;

    public model_crewdata()
    {}

    public class geo{
        @SerializedName("x")
        public String lat;

        @SerializedName("y")
        public String lon;
    }

}
