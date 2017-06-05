package com.masaga.goyorider.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.masaga.goyorider.forms.OrderStatus;
import com.masaga.goyorider.forms.PendingModel;

import static com.masaga.goyorider.R.id.Cash;

/**
 * Created by fajar on 02-Jun-17.
 */


public class model_new_order {

        public Integer mstops;
        public String mOutlets;

    public Integer getMstops() {
        return mstops;
    }

    public String getmOutlets() {
        return mOutlets;
    }

    public Integer getmCash() {
        return mCash;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmDeliver_at() {
        return mDeliver_at;
    }

    public Integer mCash;
        public String mTime;
        public String mDeliver_at;

        public model_new_order(Integer mstops, Integer mCash, String mTime, String mDeliver_at, String mOutlets) {
            this.mstops = mstops;
            this.mCash = mCash;
            this.mTime=mTime;
            this.mDeliver_at=mDeliver_at;
            this.mOutlets = mOutlets;
        }
    }

