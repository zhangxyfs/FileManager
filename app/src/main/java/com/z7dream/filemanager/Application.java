package com.z7dream.filemanager;

import android.support.multidex.MultiDexApplication;

import com.z7dream.lib.service.FileUpdatingService;

/**
 * Created by Z7Dream on 2017/7/25 11:59.
 * Email:zhangxyfs@126.com
 */

public class Application extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FileUpdatingService.startService(this);
    }
}
