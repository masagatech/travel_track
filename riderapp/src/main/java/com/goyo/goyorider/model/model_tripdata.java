package com.goyo.goyorider.model;

    import com.google.gson.annotations.SerializedName;

    /**
     * Created by mTech on 03-May-2017.
     */
    public class model_tripdata {

        @SerializedName("tripid")
        public String tripid;

        @SerializedName("loc")
        public String[] loc;

        @SerializedName("bearing")
        public String bearing;

        @SerializedName("sertm")
        public String sertm;

        @SerializedName("speed")
        public String speed;
    }
