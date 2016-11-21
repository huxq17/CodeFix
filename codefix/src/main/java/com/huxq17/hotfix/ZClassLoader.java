package com.huxq17.hotfix;

import com.andbase.tractor.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import dalvik.system.DexFile;

public class ZClassLoader extends ClassLoader {
    private Method findClassMethod = null;
    private Method findLoadedClassMethod = null;
    private ClassLoader mChild = null;
    private File mDexOutputPath;
    private File mDexPath;
    private DexFile[] mDexFile;

    public ZClassLoader(ClassLoader parent, File dexPath, File dexOutPath) {
        super(parent);
        mDexPath = dexPath;
        mDexOutputPath = dexOutPath;
        if (!mDexPath.isDirectory() || !mDexOutputPath.isDirectory()) {
            LogUtils.e("Either mDexPath or mDexOutputPath is not a directory");
            return;
        }
        File[] files = mDexPath.listFiles();
        mDexFile = new DexFile[files.length];
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            File optimizedDex = new File(mDexOutputPath, CodeFix.OPTIMIZED_PREFIX + file.getName());
            try {
                mDexFile[i] = DexFile.loadDex(file.getAbsolutePath(), optimizedDex.getAbsolutePath(), 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    protected void setOrgAPKClassLoader(ClassLoader child) {
        mChild = child;
        findLoadedClassMethod = PluginUtil.getMethod(mChild.getClass(), "findLoadedClass", String.class);
        findClassMethod = PluginUtil.getMethod(mChild.getClass(), "findClass", String.class);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        //先查找已经加载过的有没有
        Class<?> clazz = findLoadedClass(className);
        if (clazz == null) {
            try {
                //查查parent中有没有，也就是android系统中的
                clazz = getParent().loadClass(className);
            } catch (ClassNotFoundException ignored) {
            }

            if (clazz == null) {
                try {
                    //查查自己有没有，就是补丁中有没有
                    clazz = findClass(className);
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
        //查查child中有没有，child是设置进来的，实际就是宿主apk中有没有
        if (clazz == null && mChild != null) {
            try {
                if (findLoadedClassMethod != null) {
                    clazz = (Class<?>) findLoadedClassMethod.invoke(mChild, className);
                }
                if (clazz != null) {
                    return clazz;
                }
                if (findClassMethod != null) {
                    clazz = (Class<?>) findClassMethod.invoke(mChild, className);
                    return clazz;
                }
            } catch (Exception ignored) {
            }
        }
        if (clazz == null) {
            throw new ClassNotFoundException(className + " in loader " + this);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        if (mDexFile != null) {
            String slashName = className.replace('.', '/');
            for (int i = 0; i < mDexFile.length; i++) {
                DexFile dexFile = mDexFile[i];
                if (dexFile != null) {
                    Class clazz = dexFile.loadClass(slashName, this);
                    if (clazz != null) {
                        return clazz;
                    }
                }
            }
        }
        return super.findClass(className);
    }
}
