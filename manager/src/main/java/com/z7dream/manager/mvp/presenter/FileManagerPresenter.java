package com.z7dream.manager.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.z7dream.lib.callback.Callback1;
import com.z7dream.lib.db.FileDaoImpl;
import com.z7dream.lib.db.FileDaoManager;
import com.z7dream.lib.db.bean.FileInfo;
import com.z7dream.lib.db.bean.FileStarInfo;
import com.z7dream.lib.model.FileConfig;
import com.z7dream.lib.model.MagicFileInfo;
import com.z7dream.lib.service.FileUpdatingService;
import com.z7dream.lib.tool.CacheManager;
import com.z7dream.lib.tool.EnumFileType;
import com.z7dream.lib.tool.FileType;
import com.z7dream.lib.tool.FileUtils;
import com.z7dream.lib.tool.MagicExplorer;
import com.z7dream.manager.base.mvp.presenter.impl.BasePresenterImpl;
import com.z7dream.manager.mvp.contract.FileManagerContract;
import com.z7dream.manager.mvp.ui.model.FileManagerListModel;
import com.z7dream.manager.tool.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_COLLECTION;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_FOLDER;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_NEAR30DAY;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_NORMAL;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_QQ;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_WPS;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_WX;

/**
 * Created by Z7Dream on 2017/7/26 10:30.
 * Email:zhangxyfs@126.com
 */

public class FileManagerPresenter extends BasePresenterImpl<FileManagerContract.View> implements FileManagerContract.Presenter {
    private FileDaoImpl fileDaoManager;
    private int page = 0;
    private final int SIZE = 30;
    private boolean isHasSearchData = false;//是否有搜索后的数据

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
            newList.addAll(createFileModelList(list, starMap));
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
    public void getCollectionDataList(boolean isRef) {
        List<FileManagerListModel> newList = new ArrayList<>();
        if (fileDaoManager.isPutFileInStorageSucc()) {
            if (!isRef) {
                page++;
            }
            List<FileStarInfo> list = fileDaoManager.getStarList(page, SIZE);
            for (int i = 0; i < list.size(); i++) {
                FileManagerListModel model = new FileManagerListModel();
                model.type = getView().getType() == FileType.PIC ? FileManagerListModel.PIC : FileManagerListModel.OTHER;
                model.fileType = getView().getType();
                model.picPath = list.get(i).getFilePath();
                model.fileName = FileUtils.getFolderName(model.picPath);
                model.isStar = true;
                model.isSelect = false;
                String exc = FileUtils.getExtensionName(model.fileName);
                model.iconResId = FileType.createIconResId(FileType.createFileType(exc));
                File file = new File(model.picPath);
                model.modifyStr = DateUtils.formatDate(file.lastModified(), "yyyy-MM-dd HH:mm:ss");
                model.sizeStr = Formatter.formatFileSize(getContext(), file.length());
                newList.add(model);
            }
        }
        getView().getDataListSucc(newList, isRef);
    }

    @Override
    public void getNear30DaysDataList(boolean isRef) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> newList = new ArrayList<>();
        if (fileDaoManager.isPutFileInStorageSucc()) {
            if (!isRef) {
                page++;
            }
            List<FileInfo> list = fileDaoManager.get30DaysFileInfoList(EnumFileType.ALL, page, SIZE);
            newList.addAll(createFileModelList(list, starMap));
            getView().getDataListSucc(newList, isRef);
        } else {
            MagicExplorer.getFileList(null, (30 * 24 * 60 * 60) * 1000L, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    newList.addAll(createFileModelList(param));
                    getView().getDataListSucc(newList, true);
                }

                @Override
                public void callComplete() {

                }
            }, CacheManager.getSaveFilePath());
        }
    }

    @Override
    public void getQQDataList(boolean isRef) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> newList = new ArrayList<>();
        if (fileDaoManager.isPutFileInStorageSucc()) {
            if (!isRef) {
                page++;
            }
            List<FileInfo> list = fileDaoManager.getQQFileInfoList(EnumFileType.ALL, page, SIZE);
            newList.addAll(createFileModelList(list, starMap));
            getView().getDataListSucc(newList, isRef);
        } else {
            MagicExplorer.getFileList(null, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    newList.addAll(createFileModelList(param));
                    getView().getDataListSucc(newList, true);
                }

                @Override
                public void callComplete() {

                }
            }, MagicExplorer.QQ_PIC_PATH, MagicExplorer.QQ_FILE_PATH);
        }
    }

    @Override
    public void getWPSDataList(boolean isRef) {
        MagicExplorer.getWPSFileList(null, new Callback1<List<MagicFileInfo>>() {
            @Override
            public void callListener(List<MagicFileInfo> param) {
                getView().getDataListSucc(createFileModelList(param), true);
            }

            @Override
            public void callComplete() {

            }
        });
    }

    @Override
    public void getWXDataList(boolean isRef) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> newList = new ArrayList<>();
        if (fileDaoManager.isPutFileInStorageSucc()) {
            if (!isRef) {
                page++;
            }
            List<FileInfo> list = fileDaoManager.getWXFileInfoList(EnumFileType.ALL, page, SIZE);
            newList.addAll(createFileModelList(list, starMap));
            getView().getDataListSucc(newList, isRef);
        } else {
            MagicExplorer.getFileList(null, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    newList.addAll(createFileModelList(param));
                    getView().getDataListSucc(newList, true);
                }

                @Override
                public void callComplete() {

                }
            }, MagicExplorer.WX_PIC_PATH, MagicExplorer.WX_FILE_PATH);
        }
    }

    @Override
    public void getFolderDataList(String rootPath) {
        MagicExplorer.getFolderAndFileList(rootPath, param -> {
            getView().getDataListSucc(createFileModelList(param), true);
        });
    }

    @Override
    public void getSearchDataList(int function, String searchKey) {
        if (fileDaoManager.isPutFileInStorageSucc()) {
            if (function == FUN_NEAR30DAY) {
                List<FileInfo> list = fileDaoManager.get30DaysFileInfoList(EnumFileType.ALL, searchKey, -1, -1);
                getView().getDataListSucc(createFileModelList(list, getStarMap()), true);
            }

            return;
        }
        List<String> searchPathList = new ArrayList<>();
        long timeRange = 0;
        switch (function) {
            case FUN_NEAR30DAY:
                searchPathList.add(CacheManager.getSaveFilePath());
                timeRange = (30 * 24 * 60 * 60) * 1000L;
                break;
            case FUN_QQ:
                searchPathList.add(MagicExplorer.QQ_PIC_PATH);
                searchPathList.add(MagicExplorer.QQ_FILE_PATH);
                break;
            case FUN_WX:
                searchPathList.add(MagicExplorer.WX_PIC_PATH);
                searchPathList.add(MagicExplorer.WX_FILE_PATH);
                break;
            case FUN_FOLDER:
                searchPathList.add(CacheManager.getSaveFilePath());
                break;
            case FUN_NORMAL:

                break;
            case FUN_COLLECTION:

                break;
            case FUN_WPS:

                break;
        }
        if (searchPathList.size() > 0) {
            String[] paths = new String[searchPathList.size()];
            searchPathList.toArray(paths);
            getView().getAdapterList().clear();
            final boolean[] isRef = {true};

            MagicExplorer.getFileList(searchKey, timeRange, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    getView().getDataListSucc(createFileModelList(param), isRef[0]);
                    isRef[0] = false;
                }

                @Override
                public void callComplete() {

                }
            }, paths);
        }

    }

    @Override
    public void getStatisticalDataList() {

    }

    @Override
    public FileConfig getFileConfig() {
        return FileUpdatingService.getConfigCallback().getConfig();
    }

    @Override
    public boolean isHasSearchData() {
        return isHasSearchData;
    }

    private Map<String, String> getStarMap() {
        return fileDaoManager.getStarMap();
    }


    private List<FileManagerListModel> createFileModelList(List<MagicFileInfo> returnList) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> list = new ArrayList<>();
        for (int i = 0; i < returnList.size(); i++) {
            MagicFileInfo info = returnList.get(i);
            FileManagerListModel model = new FileManagerListModel();
            model.type = FileManagerListModel.OTHER;
            model.picPath = info.path;
            model.thumbnailPath = info.path;
            model.isStar = starMap.get(model.picPath) != null;
//            model.companyId = getView().getCompanyId();
            model.iconResId = FileType.createIconResId(info.position);
            model.fileType = FileType.getTypeFromResId(model.iconResId);

            String fileName = TextUtils.isEmpty(info.fileName) ? FileUtils.getFolderName(info.path) : info.fileName;
            if (fileName.split("\\/").length > 0) {
                fileName = FileUtils.getFolderName(fileName);
            }
            model.fileRealName = fileName;
            model.fileName = fileName;
            model.modifyStr = DateUtils.formatDate(info.modifyDate, "yyyy-MM-dd HH:mm:ss");
            model.sizeStr = Formatter.formatFileSize(getContext(), info.fileSize);
            model.isFile = info.isFile;
            list.add(model);
        }
        return list;
    }


    private List<FileManagerListModel> createFileModelList(List<FileInfo> list, Map<String, String> starMap) {
        List<FileManagerListModel> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            FileManagerListModel model = new FileManagerListModel();
            model.type = getView().getType() == FileType.PIC ? FileManagerListModel.PIC : FileManagerListModel.OTHER;
            model.fileType = EnumFileType.getOldType(list.get(i).getFileType());
            model.picPath = list.get(i).getFilePath();
            model.fileName = FileUtils.getFolderName(model.picPath);
            model.isStar = starMap.get(model.picPath) != null;
            model.isSelect = false;
            model.iconResId = EnumFileType.createIconResId(list.get(i).getFileType(), model.isFile);
            model.modifyStr = DateUtils.formatDate(list.get(i).getLastModifyTime(), "yyyy-MM-dd HH:mm:ss");
            model.sizeStr = Formatter.formatFileSize(getContext(), list.get(i).getFileSize());
            newList.add(model);
        }
        return newList;
    }

    @Override
    public void detachView() {
        super.detachView();
        fileDaoManager = null;
    }
}
