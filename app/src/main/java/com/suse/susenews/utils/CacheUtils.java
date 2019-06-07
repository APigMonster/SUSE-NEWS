package com.suse.susenews.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class CacheUtils {
    public static final String READ_ARRAY_ID = "READ_ARRAY_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_SEX = "USER_SEX";
    public static final String ISLOGIN = "ISLOGIN";
    public static final String SPNAME = "SUSE";

    public static  String USER_ACCOUNT = "123";//在登录后更新这个值

    public static boolean getBoolean(Context context,String spname, String key) {
        SharedPreferences sp = context.getSharedPreferences(spname, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String spname, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(spname, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }
    public static String getString(Context context, String spname, String key) {
        SharedPreferences sp = context.getSharedPreferences(spname, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void putString(Context context,  String spname,String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(spname, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }
}
