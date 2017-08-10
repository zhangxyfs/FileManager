package com.z7dream.manager.mvp.contract;

import com.z7dream.lib.model.FileConfig;
import com.z7dream.manager.base.mvp.presenter.BasePresenter;
import com.z7dream.manager.base.mvp.view.BaseView;
import com.z7dream.manager.mvp.ui.model.FileManagerListModel;

import java.util.List;

/**
 * Created by Z7Dream on 2017/7/26 10:29.
 * Email:zhangxyfs@126.com
 */

public interface FileManagerContract {
    interface Presenter extends BasePresenter {
        void getDataList(boolean isRef);

        void toStarFiles(List<String> filePathList);

        void removeStarFiles(List<String> filePathList);

        void renameFile(int position, String editTextStr);

        void deleteFiles(List<String> filePathList);

        void getCollectionDataList(boolean isRef);

        void getNear30DaysDataList(boolean isRef);

        void getQQDataList(boolean isRef);

        void getWPSDataList(boolean isRef);

        void getWXDataList(boolean isRef);

        void getFolderDataList(String rootPath, boolean isRef);

        void getSearchDataList(int function, String searchKey, boolean isRef);

        void getStatisticalDataList();

        void setIsSearch(boolean isSearch);

        FileConfig getFileConfig();

        boolean isHasSearchData();

        boolean isUsedFileObserverDb();

        boolean isSearch();
    }


    interface View extends BaseView {
        void getDataListSucc(List<FileManagerListModel> dataList, boolean isRef);

        void getDataListFail(String errorStr, boolean isRef);

        void openOperate();

        String getNowPath();

        List<FileManagerListModel> getAdapterList();

        void notifyItemChanged(int position);

        int getType();

        boolean getIsNormal();

        boolean getIsCollection();

        boolean getIsNear30Days();

        boolean getIsQQ();

        boolean getIsWPS();

        boolean getIsWX();

        boolean getIsFolder();

        boolean getIsStatistical();
    }
}
