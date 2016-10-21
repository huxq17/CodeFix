package com.huxq17.hotfix;

import android.app.Application;
import android.content.Context;

import com.andbase.tractor.utils.LogUtils;

import java.io.File;

/**
 * Created by huxq17 on 2016/9/12.
 * dx  --dex --output dex.jar patch.jar
 */
public class ZApplication extends Application {
    public static volatile ClassLoader mNowClassLoader = null;          //正在使用的ClassLoader
    public static volatile ClassLoader mBaseClassLoader = null;         //系统原始的ClassLoader
    private static Object mPackageInfo = null;                          //ContextImpl中的LoadedAPK对象mPackageInfo

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mBaseClassLoader = this.getClassLoader();
        mPackageInfo = PluginUtil.getField(getBaseContext(), "mPackageInfo");
//        setClassLoader();
        HotFix.init(this);
    }

    private void setClassLoader() {
//        Util.downloadfromAssets(this, "patch.jar");
        File patchFile = new File(this.getFilesDir(), "patch.jar");
        File patchdealFile = new File(this.getFilesDir(), "patch_deal");
        ZClassLoader classLoader = new ZClassLoader(mBaseClassLoader.getParent(),patchFile.getAbsolutePath(),patchdealFile.getAbsolutePath());
        classLoader.setOrgAPKClassLoader(mBaseClassLoader);
        LogUtils.i("test mBaseClassLoader.getParent().getClass().getSimpleName() ="+mBaseClassLoader.getParent().getClass().getSimpleName());
        PluginUtil.setField(mBaseClassLoader, "parent", classLoader);
//        PluginUtil.setField(mPackageInfo, "mClassLoader", classLoader);
//        Thread.currentThread().setContextClassLoader(classLoader);
        mNowClassLoader = classLoader;
    }
    public String getDexOutPath(){
        return getFilesDir().getPath()+"/patchs/";
    }
}
