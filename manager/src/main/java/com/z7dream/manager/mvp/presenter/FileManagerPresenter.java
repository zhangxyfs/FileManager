package com.z7dream.manager.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.z7dream.lib.db.FileDaoManager;
import com.z7dream.lib.service.FileUpdatingService;
import com.z7dream.manager.base.mvp.presenter.impl.BasePresenterImpl;
import com.z7dream.manager.base.mvp.view.BaseView;
import com.z7dream.manager.mvp.contract.FileManagerContract;

import java.util.List;

/**
 * Created by Z7Dream on 2017/7/26 10:30.
 * Email:zhangxyfs@126.com
 */

public class FileManagerPresenter extends BasePresenterImpl implements FileManagerContract.Presenter {
    private FileDaoManager fileDaoManager;

    public FileManagerPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
        fileDaoManager = new FileDaoManager(FileUpdatingService.getBoxStore());
    }

    @Override
    public void getDataList(boolean isRef) {
        
    }

    @Override
    public void toStarFiles(List<String> filePathList) {

    }

    @Override
    public void removeStarFiles(List<String> filePathList) {

    }

    @Override
    public void renameFile(int position, String editTextStr) {

    }

    @Override
    public void deleteFiles(List<String> filePathList) {

    }

    @Override
    public void getCollectionDataList() {

    }

    @Override
    public void getNear30DaysDataList() {

    }

    @Override
    public void getQQDataList() {

    }

    @Override
    public void getWPSDataList() {

    }

    @Override
    public void getWXDataList() {

    }

    @Override
    public void getFolderDataList(String rootPath) {

    }

    @Override
    public void getStatisticalDataList() {

    }

    @Override
    public void detachView() {
        super.detachView();
        fileDaoManager = null;
    }
}
