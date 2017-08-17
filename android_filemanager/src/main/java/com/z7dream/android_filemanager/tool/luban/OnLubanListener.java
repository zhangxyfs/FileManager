package com.z7dream.android_filemanager.tool.luban;

import java.io.File;

/**
 * Created by Z7Dream on 2017/5/16 17:02.
 * Email:zhangxyfs@126.com
 */

public abstract class OnLubanListener implements OnCompressListener {
    @Override
    public void onStart() {

    }

    public abstract void onStart(String createPath);

    @Override
    public void onSuccess(File file) {
        onComplete(file, null);
    }

    @Override
    public void onError(Throwable e) {
        onComplete(null, e);
    }

    public abstract void onComplete(File file, Throwable throwable);
}
