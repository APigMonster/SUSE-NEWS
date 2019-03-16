package com.suse.susenews.utils;

import android.app.Application;
import android.content.Context;

import org.xutils.BuildConfig;
import org.xutils.x;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
