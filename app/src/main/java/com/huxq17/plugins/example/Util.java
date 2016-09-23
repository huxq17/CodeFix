package com.huxq17.plugins.example;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Util {
    private static final int BUF_SIZE = 2048;

    public static void restartApplication(Context context) {
//        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
        Toast.makeText(context, "有bug，快处理", Toast.LENGTH_SHORT).show();
    }

    public static boolean downloadfromAssets(Context context, String dexFile) {
        File patchs = new File(context.getFilesDir(), dexFile);// 存放补丁文件
        if (patchs.exists()) {
            return false;
        }
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;
        try {
            bis = new BufferedInputStream(context.getAssets().open(dexFile));
            dexWriter = new BufferedOutputStream(new FileOutputStream(patchs));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}