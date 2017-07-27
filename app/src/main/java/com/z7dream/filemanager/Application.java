package com.z7dream.filemanager;


import com.z7dream.lib.listener.FileConfigCallback;
import com.z7dream.lib.model.FileConfig;
import com.z7dream.manager.base.mvp.BaseAppli;

/**
 * Created by Z7Dream on 2017/7/25 11:59.
 * Email:zhangxyfs@126.com
 */

public class Application extends BaseAppli {
    private FileConfigCallback configCallback;
    private FileConfig fileConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        fileConfig = new FileConfig();
        configCallback = new FileConfigCallback() {
            @Override
            public FileConfig getConfig() {
                fileConfig.userToken = "123";
                fileConfig.fileBaseTitle = getString(R.string.mine_file_str);
                return fileConfig;
            }
        };
    }

    @Override
    public boolean isStartFileUpdating() {
        return true;
    }

    @Override
    public FileConfigCallback getFileConfigCallback() {
        return configCallback;
    }
}
