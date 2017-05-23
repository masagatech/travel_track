package com.masaga.goyorider.forms;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fajar on 23-May-17.
 */

public class CashModel {

    private String mOrderID;
    private double mCashAmount;
    private String mHandover;
    private String mMarchant;


    public CashModel(String mMarchant,String mOrderID, double mCashAmount, String mHandover) {
        this.mOrderID = mOrderID;
        this.mMarchant = mMarchant;
        this.mCashAmount=mCashAmount;
        this.mHandover=mHandover;
        this.mMarchant = mMarchant;
    }


    protected CashModel(Parcel in) {
        mOrderID = in.readString();
        mCashAmount = in.readDouble();
        mHandover = in.readString();
        mMarchant = in.readString();
    }


    public String getmOrderID() {
        return mOrderID;
    }

    public void setmOrderID(String mOrderID) {
        this.mOrderID = mOrderID;
    }

    public double getmCashAmount() {
        return mCashAmount;
    }

    public void setmCashAmount(double mCashAmount) {
        this.mCashAmount = mCashAmount;
    }

    public String getmHandover() {
        return mHandover;
    }

    public void setmHandover(String mHandover) {
        this.mHandover = mHandover;
    }

    public String getmMarchant() {
        return mMarchant;
    }

    public void setmMarchant(String mMarchant) {
        this.mMarchant = mMarchant;
    }
}
