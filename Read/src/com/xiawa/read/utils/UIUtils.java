package com.xiawa.read.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by 陈鹏州 on 2016/3/17 21:26.
 * <p/>
 * 学校： 闽南师范大学
 */
public class UIUtils
{
    public static void showToast(final Activity context, final String msg)
    {
        if ("main".equals(Thread.currentThread().getName()))
        {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } else
        {
            context.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
