package com.ccj.client.android.analytics.bean;


/**
 * Created by chenchangjun on 18/2/9.
 */


public class ResultBean {

    private int  error_code;
    private String error_msg;
    private String s;


    @Override
    public String toString() {
        return "ResultBean{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                ", s='" + s + '\'' +
                '}';
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }



    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
