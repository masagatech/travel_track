package com.masaga.goyorider.model;

import com.google.gson.annotations.SerializedName;
import com.masaga.goyorider.forms.OrderStatus;

/**
 * Created by fajar on 06-Jun-17.
 */

public class model_completed {

    @SerializedName("ordid")
    public String ordid;

    @SerializedName("ordno")
    public String ordno;

    @SerializedName("olnm")
    public String olnm;

    @SerializedName("orddid")
    public String orderdetailid;

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

    @SerializedName("dltm")
    public String dltm;

    public OrderStatus status = OrderStatus.ACTIVE;
}
