package com.hugeardor.vidit.autonomous;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by vidit on 9/4/17.
 */

public class file {

    private String f_n;
    //private Date date;
    private String fd = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();

    // constructor


    public file(String f_n, String fd) {
        this.f_n = f_n;
        this.fd = fd;
    }
    // setter , getter

    public String getF_n() {
        return f_n;
    }

    public void setF_n(String f_n) {
        this.f_n = f_n;
    }

    public String getfd() {
        return fd;
    }

    public void setfd(String fd) {
        this.fd = fd;
    }
}
