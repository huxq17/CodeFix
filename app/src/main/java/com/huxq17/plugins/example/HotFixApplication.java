package com.huxq17.plugins.example;

import com.andbase.tractor.utils.LogUtils;
import com.huxq17.hotfix.ZApplication;

/**
 * Created by huxq17 on 2016/9/12.
 * dx  --dex --output dex.jar patch.jar
 */
public class HotFixApplication extends ZApplication {

    @Override
    public void onCreate() {
        LogUtils.e("HotFixApplication onCreate");
        super.onCreate();
    }
}
