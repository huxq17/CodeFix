package com.huxq17.hotfix;

import android.app.Application;
import android.content.Context;

import com.andbase.tractor.listener.impl.LoadListenerImpl;
import com.andbase.tractor.utils.LogUtils;
import com.andbase.tractor.utils.Util;
import com.huxq17.hotfix.bean.DownloadInfo;
import com.huxq17.hotfix.http.OKHttp;
import com.huxq17.hotfix.utils.HttpSender;
import com.huxq17.hotfix.utils.Utils;

import java.io.File;

/**
 * Created by huxq17 on 2016/10/19.
 */
public class HotFix {
    public static final String PATCH_DIR_SUF = "/patch/";
    public static final String DOWNLOAD_PATCH_DIR_SUF = "/download_patch/";
    /**
     * 补丁下载文件路径
     */
    public static String DOWNLOAD_PATCH_DIR;
    /**
     * 补丁下载读取路径
     */
    public static String PATCH_DIR;
    private static final String OPTIMIZED_DEX_DIR_SUF = "/optimized_dex_dir/";
    private static String PATCH_VERSION;
    protected static final String OPTIMIZED_PREFIX = "optimized_";
    private static String OPTIMIZED_DEX_DIR;
    private static Context mContext;
    public static volatile ClassLoader mNowClassLoader = null;          //正在使用的ClassLoader
    public static volatile ClassLoader mBaseClassLoader = null;         //系统原始的ClassLoader


    public static void init(Application application) {
        mBaseClassLoader = application.getClassLoader();
        mContext = application.getBaseContext();
        OKHttp.init(mContext);
        createDir();
        install();
    }

    private static void createDir() {
        PATCH_DIR = mContext.getFilesDir() + PATCH_DIR_SUF;
        DOWNLOAD_PATCH_DIR = mContext.getFilesDir() + DOWNLOAD_PATCH_DIR_SUF;
        OPTIMIZED_DEX_DIR = mContext.getFilesDir() + OPTIMIZED_DEX_DIR_SUF;
        Utils.createDirIfNotExists(PATCH_DIR);
        Utils.createDirIfNotExists(DOWNLOAD_PATCH_DIR);
        Utils.createDirIfNotExists(OPTIMIZED_DEX_DIR);
    }

    /**
     * 安装本地的补丁
     */
    private static void install() {
        File patchDir = new File(PATCH_DIR);
        File optimizedDexDir = new File(OPTIMIZED_DEX_DIR);
        File[] files = patchDir.listFiles();
        if (files.length > 0) {
//            String patchFileName = files[0].getName();
//            ZClassLoader classLoader = new ZClassLoader(mBaseClassLoader.getParent(), files[0].getAbsolutePath(), OPTIMIZED_DEX_DIR + OPTIMIZED_PREFIX + patchFileName);
            ZClassLoader classLoader = new ZClassLoader(mBaseClassLoader.getParent(), patchDir, optimizedDexDir);
            classLoader.setOrgAPKClassLoader(mBaseClassLoader);
            LogUtils.d("test mBaseClassLoader.getParent().getClass().getSimpleName() =" + mBaseClassLoader.getParent().getClass().getSimpleName());
            PluginUtil.setField(mBaseClassLoader, "parent", classLoader);
        }
    }

    public static boolean hasPatch() {
        File patchDir = new File(PATCH_DIR);
        File[] files = patchDir.listFiles();
        return files.length > 0 ? true : false;
    }

    public static void downloadPatch(String patchUrl) {
        final String filename = Util.getFilename(patchUrl);
        int threadNum = 1;
        DownloadInfo info = new DownloadInfo(patchUrl, DOWNLOAD_PATCH_DIR, filename, threadNum);
        HttpSender.instance().download(info, mContext, new LoadListenerImpl() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                PluginUtil.rename(DOWNLOAD_PATCH_DIR + filename, PATCH_DIR + filename);
            }

            @Override
            public void onFail(Object result) {
                super.onFail(result);
            }
        }, HotFix.class);
    }
}
