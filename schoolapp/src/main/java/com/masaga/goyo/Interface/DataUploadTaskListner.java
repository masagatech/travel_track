package com.masaga.goyo.Interface;

import android.content.ContentValues;

/**
 * Created by mTech on 04-Mar-2017.
 */
public interface DataUploadTaskListner<T> {

    public void onPostSuccess(T result, int id, boolean isSucess);
    public void onPostSuccess(T result, int id, boolean isSucess,ContentValues tag);
    public void onPostError(int id, int error);
}
