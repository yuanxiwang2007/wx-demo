package com.estatesoft.ris.wx.utils;

import java.util.UUID;

/**
 * Created by apple on 2017/10/16.
 */
public class UuidUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}