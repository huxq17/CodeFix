package com.huxq17.plugins.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.andbase.tractor.utils.LogUtils;
import com.huxq17.hotfix.HotFix;

public class MainActivity extends AppCompatActivity {
    private static String PATCH_URL = "http://192.168.22.51:8080/test/export_patch.jar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.e("test MainActivity onCreate classloader=" + getClassLoader()+";ClassLoader.getSystemClassLoader()="+ClassLoader.getSystemClassLoader());
        if (!HotFix.hasPatch()) {
            HotFix.downloadPatch(PATCH_URL);
        }
    }

    public void click(View view) {
        if (view.getId() == R.id.fix_bug) {
            Util.restartApplication(this);
        } else {
            toast("bug已经解决");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void toast(String msg) {
        Toast.makeText(this, "bug", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
