package com.masaga.goyorider.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fajar on 30-May-17.
 */

public class model_rider_list {


    public int RiderId;
    public String RiderName;

    public model_rider_list(String RiderName,int RiderId) {
        this.RiderId=RiderId;
        this.RiderName=RiderName;
    }
    @Override
    public String toString() {
        return RiderName;
    }


}
