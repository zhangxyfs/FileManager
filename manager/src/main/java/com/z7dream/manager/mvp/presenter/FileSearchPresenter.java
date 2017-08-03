package com.z7dream.manager.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.z7dream.manager.base.mvp.presenter.impl.BasePresenterImpl;
import com.z7dream.manager.mvp.contract.FileSearchContract;

/**
 * Created by Z7Dream on 2017/8/3 13:33.
 * Email:zhangxyfs@126.com
 */

public class FileSearchPresenter extends BasePresenterImpl<FileSearchContract.View> implements FileSearchContract.Presenter {
    public FileSearchPresenter(@NonNull Context context, @NonNull FileSearchContract.View view) {
        super(context, view);
    }

    @Override
    public void getDataList(boolean isRef) {

    }
}
