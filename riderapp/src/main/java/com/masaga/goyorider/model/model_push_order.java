package com.masaga.goyorider.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.PendingModel;

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

