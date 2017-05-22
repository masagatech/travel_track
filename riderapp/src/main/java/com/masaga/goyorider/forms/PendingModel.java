package com.masaga.goyorider.forms;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fajar on 22-May-17.
 */

public class PendingModel implements Parcelable {

    private String mOrder;
    private String mDate;
    private String mMarchant;
    private String mTime;
    private String mDeliver_at;
    private OrderStatus mStatus;


    private double Cash;

    public PendingModel(String mOrder, String mMarchant, String mTime, String mDeliver_at, String mDate, OrderStatus mStatus, double Cash) {
        this.mOrder = mOrder;
        this.mMarchant = mMarchant;
        this.mTime=mTime;
        this.mDeliver_at=mDeliver_at;
        this.mDate = mDate;
        this.mStatus = mStatus;
        this.Cash=Cash;
    }

    public String getmOrder() {
        return mOrder;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmMarchant() {
        return mMarchant;
    }

    public String getmTime() {
        return mTime;
    }

    public double getCash() {
        return Cash;
    }

    public String getmDeliver_at() {
        return mDeliver_at;
    }

    public OrderStatus getmStatus() {
        return mStatus;
    }

    public static Creator<PendingModel> getCREATOR() {
        return CREATOR;
    }

    public void setmOrder(String mOrder) {
        this.mOrder = mOrder;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmMarchant(String mMarchant) {
        this.mMarchant = mMarchant;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setmDeliver_at(String mDeliver_at) {
        this.mDeliver_at = mDeliver_at;
    }

    public void setmStatus(OrderStatus mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mOrder);
        dest.writeString(this.mDeliver_at);
        dest.writeString(this.mMarchant);
        dest.writeString(this.mTime);
        dest.writeString(this.mDate);
        dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
        dest.writeDouble(this.Cash);
    }

    protected PendingModel(Parcel in) {
        this.mOrder = in.readString();
        this.mMarchant = in.readString();
        this.mTime = in.readString();
        this.mDeliver_at = in.readString();
        this.mDate = in.readString();
        this.Cash = in.readInt();
        int tmpMStatus = in.readInt();
        this.mStatus = tmpMStatus == -1 ? null : OrderStatus.values()[tmpMStatus];
    }

    public static final Parcelable.Creator<PendingModel> CREATOR = new Parcelable.Creator<PendingModel>() {
        @Override
        public PendingModel createFromParcel(Parcel source) {
            return new PendingModel(source);
        }

        @Override
        public PendingModel[] newArray(int size) {
            return new PendingModel[size];
        }
    };
}

