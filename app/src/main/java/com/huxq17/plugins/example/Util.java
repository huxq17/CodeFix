package com.huxq17.plugins.example;

import android.content.Context;
import android.widget.Toast;

import com.andbase.tractor.utils.LogUtils;

public class Util {
    public static void restartApplication(Context context) {
//        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        Toast.makeText(context,"restartApplication",Toast.LENGTH_SHORT).show();
        Toast.makeText(context,"有bug需要处理",Toast.LENGTH_SHORT).show();
        LogUtils.e("test classLoader="+Util.class.getClassLoader());
    }
    public static void init(){
        LogUtils.e("Util init");
    }
}