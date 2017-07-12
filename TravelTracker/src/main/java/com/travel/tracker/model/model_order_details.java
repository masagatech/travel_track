package com.travel.tracker.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fajar on 08-Jun-17.
 */

public class model_order_details {


    @SerializedName("dt")
    public String dt;

    @SerializedName("del")
    public int del;

    @SerializedName("ret")
    public int ret;

    public String mDate;
    public String mStops;
    public String mKM;
    public String mCheckIn;
    public String mCheckOut;
    public String mRemark;


    public model_order_details(String mDate,String mStops,String mKM,String mCheckIn,String mCheckOut,String mRemark)
    {
        this.mDate = mDate;
        this.mStops = mStops;
        this.mKM = mKM;
        this.mCheckIn = mCheckIn;
        this.mCheckOut = mCheckOut;
        this.mRemark = mRemark;
    }

}
