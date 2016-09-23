package com.huxq17.plugins.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.e("test classloader "+getClassLoader());
    }

    public void click(View view) {
        if (view.getId() == R.id.fix_bug) {
            Util.restartApplication(this);
//            String patch = "patch.jar";
//            String patch_deal = "patch_deal";
//            Log.e("tag", "down patch.jar is " + Util.downloadfromAssets(this, patch));
//            File patchFile = new File(this.getFilesDir(), patch);
//            File patchdealFile = new File(this.getFilesDir(), patch_deal);
//            if (!patchdealFile.exists()) {
//                patchdealFile.mkdirs();
//            }
//            try {
//                FixBugManage.injectDexAtFirst(patchFile.getAbsolutePath(), patchdealFile.getAbsolutePath(),this);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        DexClassLoader cl = new DexClassLoader(patchFile.getAbsolutePath(),
//                patchdealFile.getAbsolutePath(), patchFile.getAbsolutePath(), getClassLoader());
//        try {
//            Class cls = cl.loadClass("com.huxq17.example.library.Utils");
//            utils = (IUtils) cls.newInstance();
//            utils.toast(this," fixed");
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
        } else {
           toast("bug已经解决");
        }
    }
    private void toast(String msg){
        Toast.makeText(this,"bug",Toast.LENGTH_SHORT).show();
    }
}
