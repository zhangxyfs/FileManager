package com.z7dream.manager.mvp.contract;

import com.z7dream.manager.base.mvp.presenter.BasePresenter;
import com.z7dream.manager.base.mvp.view.BaseView;
import com.z7dream.manager.mvp.ui.model.FileManagerListModel;

import java.util.List;

/**
 * Created by Z7Dream on 2017/8/3 13:31.
 * Email:zhangxyfs@126.com
 */

public interface FileSearchContract {
    interface Presenter extends BasePresenter {
        void getDataList(boolean isRef);
    }


    interface View extends BaseView {
        void getDataListSucc(List<FileManagerListModel> dataList, boolean isRef);

        void getDataListFail(String errorStr, boolean isRef);
    }
}
