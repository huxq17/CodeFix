package com.huxq17.hotfix;

import com.andbase.tractor.utils.LogUtils;

import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;

/**
 * Created by 2144 on 2016/10/27.
 */
public class MyPathClassLoader extends PathClassLoader {
    private ClassLoader mChild = null;
    private Method findClassMethod = null;
    private Method findLoadedClassMethod = null;
    public MyPathClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);
    }

    public MyPathClassLoader(String dexPath, String libraryPath, ClassLoader parent) {
        super(dexPath, libraryPath, parent);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        LogUtils.e("MyPathClassLoader resolve loadClass name="+className);
        return super.loadClass(className, resolve);
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        LogUtils.e("MyPathClassLoader loadClass name="+className);
        return super.loadClass(className);
    }

    public void setChild(ClassLoader child){
        mChild = child;
        findLoadedClassMethod = PluginUtil.getMethod(mChild.getClass(), "findLoadedClass", String.class);
        findClassMethod = PluginUtil.getMethod(mChild.getClass(), "findClass", String.class);
    }
}
