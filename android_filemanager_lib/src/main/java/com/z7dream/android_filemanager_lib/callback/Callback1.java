package com.z7dream.android_filemanager_lib.callback;

/**
 * Created by Z7Dream on 2017/7/25 11:33.
 * Email:zhangxyfs@126.com
 */

public interface Callback1<T> {
    void callListener(T param);
    void callComplete();
}