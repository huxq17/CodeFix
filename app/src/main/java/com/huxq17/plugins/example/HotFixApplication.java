package com.huxq17.plugins.example;

import android.content.Context;

import com.huxq17.hotfix.ZApplication;

/**
 * Created by huxq17 on 2016/9/12.
 * dx  --dex --output dex.jar patch.jar
 */
public class HotFixApplication extends ZApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
