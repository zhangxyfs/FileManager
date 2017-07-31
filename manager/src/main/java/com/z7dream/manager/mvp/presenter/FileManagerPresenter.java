package com.z7dream.manager.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;

import com.z7dream.lib.db.FileDaoImpl;
import com.z7dream.lib.db.FileDaoManager;
import com.z7dream.lib.db.bean.FileInfo;
import com.z7dream.lib.service.FileUpdatingService;
import com.z7dream.lib.tool.EnumFileType;
import com.z7dream.lib.tool.FileType;
import com.z7dream.manager.base.mvp.presenter.impl.BasePresenterImpl;
import com.z7dream.manager.mvp.contract.FileManagerContract;
import com.z7dream.manager.mvp.ui.model.FileManagerListModel;
import com.z7dream.manager.tool.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Z7Dream on 2017/7/26 10:30.
 * Email:zhangxyfs@126.com
 */

public class FileManagerPresenter extends BasePresenterImpl<FileManagerContract.View> implements FileManagerContract.Presenter {
    private FileDaoImpl fileDaoManager;
    private int page = 0;
    private final int SIZE = 100;

    public FileManagerPresenter(@NonNull Context context, @NonNull FileManagerContract.View view) {
        super(context, view);
        fileDaoManager = new FileDaoManager(context, FileUpdatingService.getBoxStore());
    }

    @Override
    public void getDataList(boolean isRef) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> newList = new ArrayList<>();
        if (!isRef) {
            page++;
        }
        if (fileDaoManager.isPutFileInStorageSucc()) {
            List<FileInfo> list = fileDaoManager.getFileInfoList(EnumFileType.getType(getView().getType()), page, SIZE);

            for (int i = 0; i < list.size(); i++) {
                FileManagerListModel model = new FileManagerListModel();
                model.type = getView().getType() == FileType.PIC ? FileManagerListModel.PIC : FileManagerListModel.OTHER;
                model.fileType = getView().getType();
                model.picPath = list.get(i).getFilePath();
                model.fileName = list.get(i).getFileName();
                model.isStar = starMap.get(model.picPath) != null;
                model.isSelect = false;
                model.iconResId = FileType.createIconResId(getView().getType());
                model.modifyStr = DateUtils.formatDate(list.get(i).getLastModifyTime(), "yyyy-MM-dd HH:mm:ss");
                model.sizeStr = Formatter.formatFileSize(getContext(), list.get(i).getFileSize());
                newList.add(model);
            }
        }

        getView().getDataListSucc(newList, isRef);
    }

    @Override
    public void toStarFiles(List<String> filePathList) {
        fileDaoManager.addStar(filePathList);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < filePathList.size(); i++) {
            map.put(filePathList.get(i), filePathList.get(i));
        }
        List<FileManagerListModel> newList = new ArrayList<>();
        newList.addAll(getView().getAdapterList());
        int starSize = 0;
        for (int i = 0; i < newList.size(); i++) {
            if (map.get(newList.get(i).picPath) != null) {
                newList.get(i).isStar = true;
                starSize++;
            }
            if (starSize == map.size()) break;
        }
        getView().getDataListSucc(newList, true);
    }

    @Override
    public void removeStarFiles(List<String> filePathList) {
        fileDaoManager.removeStar(filePathList);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < filePathList.size(); i++) {
            map.put(filePathList.get(i), filePathList.get(i));
        }
        List<FileManagerListModel> newList = new ArrayList<>();
        newList.addAll(getView().getAdapterList());
        int starSize = 0;
        for (int i = 0; i < newList.size(); i++) {
            if (map.get(newList.get(i).picPath) != null) {
                newList.get(i).isStar = false;
                starSize++;
            }
            if (starSize == map.size()) break;
        }
        getView().getDataListSucc(newList, true);
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

    private Map<String, String> getStarMap() {
        return fileDaoManager.getStarMap();
    }

    @Override
    public void detachView() {
        super.detachView();
        fileDaoManager = null;
    }
}
