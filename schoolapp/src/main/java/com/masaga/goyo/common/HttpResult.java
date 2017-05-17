package com.masaga.goyo.common;

/**
 * Created by mTech on 21-Apr-2017.
 */
public class HttpResult {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean status) {
        this.isSuccess = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private boolean isSuccess;
    private String message;
}
