package com.xiaojia.daniujia.provider;

import android.net.Uri;

import com.xiaojia.daniujia.utils.ApplicationUtil;

/**
 * Created by ZLOVE on 2016/4/8.
 */
public class DaniujiaUris{
    public static String AUTHORITY = ApplicationUtil.getApplicationContext().getPackageName();

    public static final String CONTENT_P = "content://";
    public static final String PATH_RAW_SQL = "raw/sql";
    public static final Uri URI_RAW_SQL = Uri.parse(CONTENT_P + AUTHORITY + "/raw/sql");

    public static final String PATH_CONVERSATION = "conversation";
    public static final Uri URI_CONVERSATION = Uri.parse(CONTENT_P + AUTHORITY + "/conversation");

    public static final String PATH_MESSAGE = "message";
    public static final Uri URI_MESSAGE = Uri.parse(CONTENT_P + AUTHORITY + "/message");
}
