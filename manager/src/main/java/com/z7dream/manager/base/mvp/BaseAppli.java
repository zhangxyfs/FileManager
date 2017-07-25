package com.z7dream.manager.base.mvp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.z7dream.lib.service.FileUpdatingService;

/**
 * Created by Z7Dream on 2017/7/25 14:36.
 * Email:zhangxyfs@126.com
 */

public class BaseAppli extends MultiDexApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        FileUpdatingService.startService(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
