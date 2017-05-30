package com.masaga.goyorider.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.PendingModel;

/**
 * Created by fajar on 29-May-17.
 */

public class model_push_order {

    public String mOrderId;
    public String mMarchant;
    public String mAddr;
    public String mTime;

    public model_push_order(String mOrderId, String mTime, String mMarchant, String mAddr) {
        this.mOrderId = mOrderId;
        this.mTime = mTime;
        this.mMarchant = mMarchant;
        this.mAddr=mAddr;
    }
//
//    public String getmTime() {
//        return mTime;
//    }

//    public String getmOrderId() {
//        return mOrderId;
//    }
//
//    public void setmOrderId(String mOrderId) {
//        this.mOrderId = mOrderId;
//    }
//
//    public String getmMarchant() {
//        return mMarchant;
//    }
//
//    public void setmMarchant(String mMarchant) {
//        this.mMarchant = mMarchant;
//    }
//
//    public String getmAddr() {
//        return mAddr;
//    }
//
//    public void setmAddr(String mAddr) {
//        this.mAddr = mAddr;
//    }
//
//    public static Creator<model_push_order> getCREATOR() {
//        return CREATOR;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.mOrderId);
//        dest.writeString(this.mMarchant);
//        dest.writeString(this.mAddr);
//    }
//
//    protected model_push_order(Parcel in) {
//        this.mOrderId = in.readString();
//        this.mMarchant = in.readString();
//        this.mAddr = in.readString();
//    }

//    public static final Parcelable.Creator<model_push_order> CREATOR = new Parcelable.Creator<model_push_order>() {
//        @Override
//        public model_push_order createFromParcel(Parcel source) {
//            return new model_push_order(source);
//        }

//        @Override
//        public model_push_order[] newArray(int size) {
//            return new model_push_order[size];
//        }
//    };
}

