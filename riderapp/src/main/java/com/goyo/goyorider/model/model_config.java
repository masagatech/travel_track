package com.goyo.goyorider.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fajar on 08-Jun-17.
 */

public class model_config {


    @SerializedName("notifymin")
    public Integer notifymin = 3;


    @SerializedName("ordpendingct")
    public Integer ordpendingct = 20;
}
