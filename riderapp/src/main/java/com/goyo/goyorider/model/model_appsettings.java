package com.goyo.goyorider.model;

/**
 * Created by mTech on 05-May-2017.
 */
public class model_appsettings {


    private int redious_user_alert = 1000; //in meter default
    private int check_location_when_my_pos = 100; //100 metere default



    public int getRedious_user_alert() {
        return redious_user_alert;
    }

    public void setRedious_user_alert(int redious_user_alert) {
        this.redious_user_alert = redious_user_alert;
    }

    public int getCheck_location_when_my_pos() {
        return check_location_when_my_pos;
    }

    public void setCheck_location_when_my_pos(int check_location_when_my_pos) {
        this.check_location_when_my_pos = check_location_when_my_pos;
    }



}
