package com.z7dream.filemanager;


import com.z7dream.android_filemanager_lib.listener.FileConfigCallback;
import com.z7dream.android_filemanager_lib.model.FileConfig;
import com.z7dream.android_filemanager.base.mvp.BaseAppli;

/**
 * Created by Z7Dream on 2017/7/25 11:59.
 * Email:zhangxyfs@126.com
 */

public class Application extends BaseAppli {
    private static FileConfig mFileConfig;

    private FileConfigCallback configCallback = new FileConfigCallback() {
        @Override
        public FileConfig getConfig() {
            return mFileConfig;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mFileConfig = new FileConfig();
    }

    @Override
    public boolean isStartFileUpdating() {
        return true;
    }

    @Override
    public FileConfigCallback getFileConfigCallback() {
        return configCallback;
    }

    public static void setFileConfig(FileConfig fileConfig){
        mFileConfig = fileConfig;
    }
}
