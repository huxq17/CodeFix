package com.huxq17.hotfix;

import android.app.Application;
import android.content.Context;

import com.andbase.tractor.listener.LoadListener;
import com.andbase.tractor.listener.impl.LoadListenerImpl;
import com.andbase.tractor.task.Task;
import com.andbase.tractor.task.TaskPool;
import com.andbase.tractor.utils.LogUtils;
import com.andbase.tractor.utils.Util;
import com.huxq17.hotfix.bean.DownloadInfo;
import com.huxq17.hotfix.http.OKHttp;
import com.huxq17.hotfix.utils.HttpSender;
import com.huxq17.hotfix.utils.Utils;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexFile;

import static android.R.attr.start;

/**
 * Created by huxq17 on 2016/10/19.
 */
public class CodeFix {
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
    private static Object mPackageInfo = null;
    public static final String PATCH_INFO = "patch.info";


    public static void init(Application application) {
        mBaseClassLoader = application.getClassLoader();
        LogUtils.e("test mBaseClassLoader=" + mBaseClassLoader);
        mContext = application.getBaseContext();
        mPackageInfo = PluginUtil.getField(mContext, "mPackageInfo");
        OKHttp.init(mContext);
        createDir();
        loadPatch();
        changeTopClassLoader();
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
     * 加载本地的补丁
     */
    private static void loadPatch() {
        File patchDir = new File(PATCH_DIR);
        File optimizedDexDir = new File(OPTIMIZED_DEX_DIR);
        File[] files = patchDir.listFiles();
        long start = System.currentTimeMillis();
        if (files != null && files.length > 0) {
//            String patchFileName = files[0].getName();
//            ZClassLoader classLoader = new ZClassLoader(mBaseClassLoader.getParent(), files[0].getAbsolutePath(), OPTIMIZED_DEX_DIR + OPTIMIZED_PREFIX + patchFileName);
            ZClassLoader classLoader = new ZClassLoader(mBaseClassLoader.getParent(), patchDir, optimizedDexDir);
            classLoader.setOrgAPKClassLoader(mBaseClassLoader);
            PluginUtil.setField(mBaseClassLoader, "parent", classLoader);
        }
        LogUtils.d("loadPatch spend time is" + (System.currentTimeMillis() - start));
    }

    private static void changeTopClassLoader() {
        ClassLoader TopLoader = ClassLoader.getSystemClassLoader();
        String classPath = System.getProperty("java.class.path", ".");
        LogUtils.d("test mBaseClassLoader.getParent().getClass().getSimpleName() =" + mBaseClassLoader.getParent().getClass().getSimpleName() + ";classPath=" + classPath);
        Object bootstrapClassLoader = PluginUtil.getField(TopLoader, "parent");
        MyPathClassLoader pathClassLoader = new MyPathClassLoader(classPath, (ClassLoader) bootstrapClassLoader);
        pathClassLoader.setChild(TopLoader);
        PluginUtil.setField(TopLoader, "parent", pathClassLoader);
        LogUtils.e("bootstrapClassLoader=" + bootstrapClassLoader + ";" + PluginUtil.getField(ClassLoader.getSystemClassLoader(), "parent"));
    }

    public static void install(String patchVersion) {
        File patchInfo = new File(PATCH_DIR, PATCH_INFO);
        PluginUtil.write2File(patchVersion, patchInfo);
    }

    public static String getPatchVersion() {
        File patchInfo = new File(PATCH_DIR, PATCH_INFO);
        String version = PluginUtil.readFromFile(patchInfo);
        return version;
    }

    public static void downloadPatch(String patchUrl, final LoadListener listener) {
        final String filename = Util.getFilename(patchUrl);
        int threadNum = 1;
        DownloadInfo info = new DownloadInfo(patchUrl, DOWNLOAD_PATCH_DIR, filename, threadNum);
        HttpSender.instance().download(info, mContext, new LoadListenerImpl() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                //同时只存在一个补丁包
                PluginUtil.delete(PATCH_DIR);
                PluginUtil.rename(DOWNLOAD_PATCH_DIR + filename, PATCH_DIR + filename);
                preDexOpt(PATCH_DIR + filename, OPTIMIZED_DEX_DIR + CodeFix.OPTIMIZED_PREFIX + filename, mBaseClassLoader.getParent());
                listener.onSuccess(result);
            }

            @Override
            public void onLoading(Object result) {
                super.onLoading(result);
                listener.onLoading(result);
            }

            @Override
            public void onFail(Object result) {
                super.onFail(result);
                listener.onFail(result);
            }
        }, CodeFix.class);
    }

    private static void preDexOpt(final String dexPath, final String optimizedDirectory, final ClassLoader parent) {
        TaskPool.getInstance().execute(new Task() {
            @Override
            public void onRun() {
                long start = System.currentTimeMillis();
                try {
                    DexFile.loadDex(dexPath, optimizedDirectory, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogUtils.d("preDexOpt spend time is " + (System.currentTimeMillis() - start));
            }

            @Override
            public void cancelTask() {

            }
        });
    }
}
