package com.travel.tracker.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajar on 29-May-17.
 */

public class model_push_order {



    @SerializedName("ordid")
    public String ordid;

    @SerializedName("ordno")
    public JsonArray ordno;

    @SerializedName("olnm")
    public String olnm;

    @SerializedName("city")
    public String city;

    @SerializedName("area")
    public String area;

    @SerializedName("pchtm")
    public String pchtm;

    @SerializedName("totamt")
    public Double totamt;

    public model_push_order() {
    }
}

