package com.z7dream.lib.tool;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Z7Dream on 2017/7/25 11:43.
 * Email:zhangxyfs@126.com
 */

public class Utils {
    public static boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() == 0) return false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(serviceName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 获取状态栏高度
     *
     * @param ctx activity
     * @return int
     */
    public static int getTop(Activity ctx) {
        Rect rect = new Rect();
        ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        if (rect.top == 0) {
            try {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                return ctx.getResources().getDimensionPixelSize(x);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return rect.top;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context context
     * @return int
     */
    public static int getScreenWidth(@NonNull final Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }


    /**
     * 转换dip为px
     *
     * @param context context
     * @param dip     dip
     * @return int
     */
    public static int convertDipOrPx(@NonNull Context context, float dip) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    //展开或缩回动画
    public static void expandOCollapseAnim(boolean isExpand, ImageView iv) {
        expandOCollapseAnim(isExpand, iv, 300);
    }

    public static void expandOCollapseAnim(boolean isExpand, ImageView iv, int duration) {
        float start, target;
        if (isExpand) {
            start = 0f;
            target = 90f;
        } else {
            start = 90f;
            target = 0f;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv, View.ROTATION, start, target);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
}
