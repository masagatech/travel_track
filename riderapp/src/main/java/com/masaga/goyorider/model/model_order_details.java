package com.masaga.goyorider.model;

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


    public model_order_details()
    {
    }

}
