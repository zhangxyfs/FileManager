package com.z7dream.android_filemanager.mvp.ui;

import android.os.Bundle;

import com.z7dream.android_filemanager.base.mvp.BaseActivity;
import com.z7dream.android_filemanager.mvp.contract.FileSearchContract;
import com.z7dream.android_filemanager.mvp.presenter.FileSearchPresenter;
import com.z7dream.android_filemanager.mvp.ui.model.FileManagerListModel;

import java.util.List;

/**
 * Created by Z7Dream on 2017/8/3 13:31.
 * Email:zhangxyfs@126.com
 */

public class FileSearchActivity extends BaseActivity<FileSearchContract.Presenter> implements FileSearchContract.View {
    @Override
    public void getDataListSucc(List<FileManagerListModel> dataList, boolean isRef) {

    }

    @Override
    public void getDataListFail(String errorStr, boolean isRef) {

    }

    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected FileSearchContract.Presenter createPresenter() {
        return new FileSearchPresenter(this, this);
    }
}
