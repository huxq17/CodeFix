package com.huxq17.plugins.example;

import com.huxq17.hotfix.HotFix;
import com.huxq17.hotfix.ZApplication;

/**
 * Created by huxq17 on 2016/9/12.
 * dx  --dex --output dex.jar patch.jar
 */
public class HotFixApplication extends ZApplication {
    private static String PATCH_URL = "http://192.168.22.51:8080/test/patch.jar";
    @Override
    public void onCreate() {
        super.onCreate();
        HotFix.downloadPatch(PATCH_URL);
    }
}
