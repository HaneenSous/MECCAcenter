package com.mecca_center.app.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;


import com.mecca_center.app.meccacenter.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by The_Dev on 1/15/2015.
 */
public class StatusBarColorUtil {

int color;

     private static SystemBarTintManager mSystemBarTintManager;
    public static void setStatusColor(Activity context){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true,context);
        }
        mSystemBarTintManager = new SystemBarTintManager(context);
        mSystemBarTintManager.setTintColor(context.getResources().getColor(R.color.primary_dark));
        mSystemBarTintManager.setStatusBarTintEnabled(true);
        mSystemBarTintManager.setNavigationBarTintEnabled(true);

    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on,Activity context) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
