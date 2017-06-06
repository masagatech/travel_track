package com.masaga.goyorider.model;

import com.google.gson.annotations.SerializedName;
import com.masaga.goyorider.forms.OrderStatus;

/**
 * Created by fajar on 27-May-17.
 */

public class model_pending {

    @SerializedName("orddid")
    public int orderdetailid;



    @SerializedName("ordid")
    public int ordid;

    @SerializedName("ordno")
    public int ordno;

    @SerializedName("rm")
    public String remark;

    @SerializedName("olnm")
    public String olnm;

    @SerializedName("cnm")
    public String custname;

    @SerializedName("mob")
    public String custmob;

    @SerializedName("cadr")
    public String custaddr;

    @SerializedName("orddate")
    public String orddate;

    @SerializedName("amt")
    public Double amtcollect;

    @SerializedName("pctm")
    public String picktime;

    @SerializedName("deldate")
    public String deldate;

    @SerializedName("dtm")
    public String deltime;

    @SerializedName("dltm")
    public String dltm;

    @SerializedName("trpid")
    public String tripid;

    @SerializedName("stsi")
    public String stats;


    public OrderStatus status = OrderStatus.ACTIVE;

    public model_pending()
    {}

}
