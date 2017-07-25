package com.z7dream.manager.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.z7dream.manager.base.mvp.presenter.impl.BasePresenterImpl;
import com.z7dream.manager.base.mvp.view.BaseView;
import com.z7dream.manager.mvp.contract.FileBaseContract;

/**
 * Created by Z7Dream on 2017/7/25 14:59.
 * Email:zhangxyfs@126.com
 */

public class FileBasePresenter extends BasePresenterImpl implements FileBaseContract.Presenter {

    public FileBasePresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }
}
