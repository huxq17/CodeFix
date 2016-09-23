package com.huxq17.example.library;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huxq17 on 2016/7/28.
 */
public class Utils implements IUtils{
    public void toast(Context context, String msg) {
        Toast.makeText(context,"bug", Toast.LENGTH_SHORT).show();
    }
}
