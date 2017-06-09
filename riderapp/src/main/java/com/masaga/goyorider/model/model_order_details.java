package com.masaga.goyorider.model;

/**
 * Created by fajar on 08-Jun-17.
 */

public class model_order_details {
    public String mDate;
    public int mReturn;
    public int mReject;
    public int mDeliver;
    public int mTotal;

    public model_order_details(String mDate,int mReturn,int mReject,int mDeliver,int mTotal)
    {
        this.mDate=mDate;
        this.mReturn=mReturn;
        this.mReject=mReject;
        this.mDeliver=mDeliver;
        this.mTotal=mTotal;
    }

}
