package com.liuh.androidclipboardlearn;

import android.app.Application;

/**
 * Created by huan on 2018/5/30.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ClipboardUtil.init(this);
    }
}
