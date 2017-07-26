package com.z7dream.manager.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.z7dream.manager.base.mvp.presenter.impl.BasePresenterImpl;
import com.z7dream.manager.base.mvp.view.BaseView;
import com.z7dream.manager.mvp.contract.FileManagerContract;

/**
 * Created by Z7Dream on 2017/7/26 10:30.
 * Email:zhangxyfs@126.com
 */

public class FileManagerPresenter extends BasePresenterImpl implements FileManagerContract.Presenter {

    public FileManagerPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }
}
