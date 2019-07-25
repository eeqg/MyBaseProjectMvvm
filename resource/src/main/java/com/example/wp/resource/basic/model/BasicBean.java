package com.example.wp.resource.basic.model;

/**
 * Created by wp on 2018/6/25.
 */

public class BasicBean {
    public StatusInfo statusInfo = new StatusInfo();

    public String resultData;

    public static boolean isNull(BasicBean basicBean) {
        if (basicBean == null) {
            return true;
        }
        if (basicBean.resultData == null) {
            return true;
        }
        if ("null".equals(basicBean.resultData)) {
            return true;
        }
        return false;
    }

    public static String getNullMessage(BasicBean basicBean) {
        if (basicBean == null) {
            return "";
        } else {
            StatusInfo statusInfo = basicBean.statusInfo;
            if (statusInfo == null) {
                return "";
            } else {
                return statusInfo.statusMessage + "";
            }
        }
    }
}
