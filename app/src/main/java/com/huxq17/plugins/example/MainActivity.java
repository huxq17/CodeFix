package com.huxq17.plugins.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.andbase.tractor.listener.impl.LoadListenerImpl;
import com.andbase.tractor.utils.LogUtils;
import com.huxq17.hotfix.CodeFix;
import com.huxq17.hotfix.http.response.HttpResponse;
import com.huxq17.hotfix.utils.HttpSender;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    private static String URL = "http://m.config.2144.cn/patch/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.e("test MainActivity onCreate classloader=" + getClassLoader() + ";ClassLoader.getSystemClassLoader()=" + ClassLoader.getSystemClassLoader());
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("slug", "sjlp");
        params.put("os", "1");
        params.put("appVersion", "1.0.0");
        String patchVersion = CodeFix.getPatchVersion();
        if (!TextUtils.isEmpty(patchVersion)) {
            params.put("patchVersion", patchVersion);
        }

        HttpSender.instance().get(URL, null, params, new LoadListenerImpl() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                String response = HttpResponse.getString(result);
                Bean bean = JsonParser.instance().fromJson(response, Bean.class);
                try {
                    String sha1 = bean.getData().getSha1();
                    String sign = bean.getData().getSign();
                    int code = bean.getCode();
                    if (code == 304) {
                        return;
                    }
                    final String version = bean.getData().getVersion();
                    if (RSAEncrypt.verify(sha1, sign, RSAEncrypt.loadPublicKeyByFile(getApplicationContext(), "publicKey.pem"))) {
                        CodeFix.downloadPatch(bean.getData().getUrl(), new LoadListenerImpl() {
                            @Override
                            public void onSuccess(Object result) {
                                super.onSuccess(result);
                                CodeFix.install(version);
                            }
                        });
                    }
                } catch (Exception e) {
                    //ignore
                }
            }
        });
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
