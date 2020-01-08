package com.mobile.fkpn.model;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class EndCode {
    public String endCode(String value) {
        byte[] data = null;
        try {
            data = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }
}
