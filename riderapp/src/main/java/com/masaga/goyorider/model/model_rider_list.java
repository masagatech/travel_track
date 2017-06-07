package com.masaga.goyorider.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fajar on 30-May-17.
 */

public class model_rider_list {


    public int RiderId;
    public String RiderName;
    public String RiderNumber;
    public String RiderKM;
    public String RiderBtry;
    boolean selected = false;

    public model_rider_list(String RiderName,int RiderId,String RiderNumber,String RiderKM,String RiderBtry) {
        this.RiderNumber=RiderNumber;
        this.RiderKM=RiderKM;
        this.RiderBtry=RiderBtry;
        this.RiderId=RiderId;
        this.RiderName=RiderName;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public String toString() {
        return RiderName;
    }


}
