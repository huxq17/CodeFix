package com.huxq17.plugins.example;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void restartApplication(Context context) {
//        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
        Toast.makeText(context, "有bug，快处理", Toast.LENGTH_SHORT).show();
    }
}