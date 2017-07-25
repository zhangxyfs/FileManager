package com.z7dream.manager.base.mvp.view;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 *  * Created by xiaoyu.zhang on 2016/11/10 16:05
 *  
 */
public interface BaseView {
    <T> LifecycleTransformer<T> bindToLifecycle();
}
