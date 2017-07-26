package com.z7dream.manager.mvp.contract;


import com.z7dream.manager.base.mvp.presenter.BasePresenter;
import com.z7dream.manager.base.mvp.view.BaseView;
import com.z7dream.manager.mvp.ui.model.FileBaseListModel;

import java.util.List;

/**
 * Created by Z7Dream on 2017/7/25 14:58.
 * Email:zhangxyfs@126.com
 */

public interface FileBaseContract {
    interface Presenter extends BasePresenter {
        void getDataList(boolean isRef);

        FileBaseListModel getChildTypeData();
    }


    interface View extends BaseView {
        void getDataListSucc(List<FileBaseListModel> dataList, boolean isRef);

        void getDataListFail(String errorStr, boolean isRef);

        void setChildTypeData(FileBaseListModel childTypeModel);
    }
}
