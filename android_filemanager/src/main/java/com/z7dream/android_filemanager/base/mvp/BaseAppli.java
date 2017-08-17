package com.z7dream.android_filemanager.base.mvp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.z7dream.android_filemanager_lib.listener.FileConfigCallback;
import com.z7dream.android_filemanager_lib.service.FileUpdatingService;


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
        FileUpdatingService.startService(this, isStartFileUpdating(), getFileConfigCallback());
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
