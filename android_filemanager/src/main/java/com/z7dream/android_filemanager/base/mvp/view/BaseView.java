package com.z7dream.android_filemanager.base.mvp.view;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 *  * Created by xiaoyu.zhang on 2016/11/10 16:05
 *  
 */
public interface BaseView {
    <T> LifecycleTransformer<T> bindToLifecycle();
}
