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

    @SerializedName("enttnm")
    public String enttnm;

    @SerializedName("olnm")
    public String olnm;

    @SerializedName("custname")
    public String custname;

    @SerializedName("custaddr")
    public String custaddr;

    @SerializedName("orddate")
    public String orddate;

    @SerializedName("amtcollect")
    public Double amtcollect;

    @SerializedName("picktime")
    public String picktime;

    @SerializedName("deldate")
    public String deldate;

    @SerializedName("deltime")
    public String deltime;


    public OrderStatus status = OrderStatus.ACTIVE;

    public model_pending()
    {}

}
