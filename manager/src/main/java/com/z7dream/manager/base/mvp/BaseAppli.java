package com.z7dream.manager.base.mvp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.z7dream.lib.listener.FileConfigCallback;
import com.z7dream.lib.service.FileUpdatingService;


/**
 * Created by Z7Dream on 2017/7/25 14:36.
 * Email:zhangxyfs@126.com
 */

public abstract class BaseAppli extends MultiDexApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (isStartFileUpdating())
            FileUpdatingService.startService(this);
        FileUpdatingService.setConfig(getFileConfigCallback());
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 是否开启文件监听
     *
     * @return
     */
    public abstract boolean isStartFileUpdating();

    /**
     * 设置文件属性
     *
     * @return
     */
    public abstract FileConfigCallback getFileConfigCallback();
}
