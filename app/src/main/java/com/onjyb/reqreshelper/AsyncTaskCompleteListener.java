package com.onjyb.reqreshelper;

public interface AsyncTaskCompleteListener<T> {
    void onTaskComplete(int i, T t, T t2, Object obj);
}
