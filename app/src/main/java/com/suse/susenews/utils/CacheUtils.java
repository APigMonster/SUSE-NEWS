package com.suse.susenews.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class CacheUtils {
    public static final String READ_ARRAY_ID = "read_array_id";
    public static  String USER_ACCOUNT = "123456";//在登录后更新这个值

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(USER_ACCOUNT, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(USER_ACCOUNT, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(USER_ACCOUNT, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(USER_ACCOUNT, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }
}
