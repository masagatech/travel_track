package com.masaga.goyorider.model;

import com.google.gson.annotations.SerializedName;
import com.masaga.goyorider.forms.OrderStatus;

/**
 * Created by fajar on 06-Jun-17.
 */

public class model_completed {

    @SerializedName("ordid")
    public Double ordid;

    @SerializedName("ordno")
    public Double ordno;

    @SerializedName("olnm")
    public String olnm;

    @SerializedName("orddid")
    public int orderdetailid;

    @SerializedName("amtrec")
    public Double amtrec;

    @SerializedName("stsi")
    public int stsi;

    @SerializedName("cnm")
    public String custname;

    @SerializedName("mob")
    public String custmob;

    @SerializedName("cadr")
    public String custaddr;

    @SerializedName("amt")
    public Double amtcollect;

    @SerializedName("trpid")
    public String tripid;

    @SerializedName("rm")
    public String remark;

    @SerializedName("dtm")
    public String deltime;

    public OrderStatus status = OrderStatus.ACTIVE;
}
