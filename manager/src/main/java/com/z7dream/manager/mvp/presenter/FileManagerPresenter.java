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

import io.reactivex.disposables.Disposable;

import static com.z7dream.lib.tool.MagicExplorer.getFileList;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_COLLECTION;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_FOLDER;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_NEAR30DAY;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_NORMAL;
import static com.z7dream.manager.mvp.ui.FileManagerActivity.FUN_QQ;
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
    private boolean isUsedFileObserverDb = false;//是否使用文件监听数据库
    private boolean isSearch = false;//是否搜索中
    private Disposable magicFileSearchDisponsable;

    public FileManagerPresenter(@NonNull Context context, @NonNull FileManagerContract.View view) {
        super(context, view);
        fileDaoManager = new FileDaoManager(context, FileUpdatingService.getBoxStore());
        isUsedFileObserverDb = fileDaoManager.isPutFileInStorageSucc();
    }

    @Override
    public void getDataList(boolean isRef) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> newList = new ArrayList<>();
        if (!isRef) {
            page++;
        }
        if (isUsedFileObserverDb) {
            List<FileInfo> list = fileDaoManager.getFileInfoList(EnumFileType.getType(getView().getType()), page, SIZE);
            newList.addAll(createFileModelList(list, null, starMap));
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
        if (isUsedFileObserverDb) {
            if (!isRef) {
                page++;
            }
            newList.addAll(createFileModelListByStar(fileDaoManager.getStarList(page, SIZE)));
        }
        getView().getDataListSucc(newList, isRef);
    }

    @Override
    public void getNear30DaysDataList(boolean isRef) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> newList = new ArrayList<>();
        if (isUsedFileObserverDb) {
            if (!isRef) {
                page++;
            }
            List<FileInfo> list = fileDaoManager.get30DaysFileInfoList(EnumFileType.ALL, page, SIZE);
            newList.addAll(createFileModelList(list, null, starMap));
            getView().getDataListSucc(newList, isRef);
        } else {
            getFileList(null, true, (30 * 24 * 60 * 60) * 1000L, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    newList.addAll(createFileModelList(param, null));
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
        if (isUsedFileObserverDb) {
            if (!isRef) {
                page++;
            }
            List<FileInfo> list = fileDaoManager.getQQFileInfoList(EnumFileType.ALL, page, SIZE);
            newList.addAll(createFileModelList(list, null, starMap));
            getView().getDataListSucc(newList, isRef);
        } else {
            getFileList(null, true, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    newList.addAll(createFileModelList(param, null));
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
                getView().getDataListSucc(createFileModelList(param, null), true);
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
        if (isUsedFileObserverDb) {
            if (isRef) {
                page = 0;
            } else {
                page++;
            }
            List<FileInfo> list = fileDaoManager.getWXFileInfoList(EnumFileType.ALL, page, SIZE);
            newList.addAll(createFileModelList(list, null, starMap));
            getView().getDataListSucc(newList, isRef);
        } else {
            getFileList(null, true, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    newList.addAll(createFileModelList(param, null));
                    getView().getDataListSucc(newList, true);
                }

                @Override
                public void callComplete() {

                }
            }, MagicExplorer.WX_PIC_PATH, MagicExplorer.WX_FILE_PATH);
        }
    }

    @Override
    public void getFolderDataList(String rootPath, boolean isRef) {
        Map<String, String> starMap = getStarMap();
        List<FileManagerListModel> newList = new ArrayList<>();
        if (isUsedFileObserverDb) {
            if (isRef) {
                page = 0;
            } else {
                page++;
            }
            List<FileInfo> list = fileDaoManager.getFolderFileInfoList(EnumFileType.ALL, rootPath, page, SIZE);
            newList.addAll(createFileModelList(list, null, starMap));
            getView().getDataListSucc(newList, isRef);
        } else {
            MagicExplorer.getFolderAndFileList(rootPath, param -> {
                getView().getDataListSucc(createFileModelList(param, null), true);
            });
        }
    }

    @Override
    public void getSearchDataList(int function, String searchKey, boolean isRef) {
        if (isRef) {
            page = 0;
        } else {
            page++;
        }
        List<String> searchPathList = new ArrayList<>();
        long timeRange = 0;

        List<FileInfo> needList = new ArrayList<>();

        if (isUsedFileObserverDb) {
            if (function == FUN_NEAR30DAY) {
                needList.addAll(fileDaoManager.get30DaysFileInfoList(EnumFileType.ALL, searchKey, page, SIZE));
            } else if (function == FUN_QQ) {
                needList.addAll(fileDaoManager.getQQFileInfoList(EnumFileType.ALL, searchKey, page, SIZE));
            } else if (function == FUN_WX) {
                needList.addAll(fileDaoManager.getWXFileInfoList(EnumFileType.ALL, searchKey, page, SIZE));
            } else if (function == FUN_FOLDER) {
                needList.addAll(fileDaoManager.getFolderFileInfoList(EnumFileType.ALL, searchKey, page, SIZE));
            } else if (function == FUN_NORMAL) {
                needList.addAll(fileDaoManager.getFileInfoList(EnumFileType.getType(getView().getType()), searchKey, page, SIZE));
            }
            getView().getDataListSucc(createFileModelList(needList, searchKey, getStarMap()), isRef);
        } else {
            if (!isRef) return;

            if (function == FUN_NEAR30DAY) {
                searchPathList.add(CacheManager.getSaveFilePath());
                timeRange = (30 * 24 * 60 * 60) * 1000L;
            } else if (function == FUN_QQ) {
                searchPathList.add(MagicExplorer.QQ_PIC_PATH);
                searchPathList.add(MagicExplorer.QQ_FILE_PATH);
            } else if (function == FUN_WX) {
                searchPathList.add(MagicExplorer.WX_PIC_PATH);
                searchPathList.add(MagicExplorer.WX_FILE_PATH);
            } else if (function == FUN_FOLDER) {
                searchPathList.add(CacheManager.getSaveFilePath());
            } else if (function == FUN_NORMAL) {

            }
        }
        if (searchPathList.size() > 0)
            toMagicSearch(searchPathList, searchKey, timeRange);

        if (function == FUN_COLLECTION) {//星标文件
            getView().getDataListSucc(createFileModelListByStar(fileDaoManager.getStarList(searchKey, page, SIZE)), isRef);
        }
    }

    @Override
    public void getStatisticalDataList() {

    }

    @Override
    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    @Override
    public FileConfig getFileConfig() {
        return FileUpdatingService.getConfigCallback().getConfig();
    }

    @Override
    public boolean isHasSearchData() {
        return isHasSearchData;
    }

    @Override
    public boolean isUsedFileObserverDb() {
        return isUsedFileObserverDb;
    }

    @Override
    public boolean isSearch() {
        return isSearch;
    }

    private Map<String, String> getStarMap() {
        return fileDaoManager.getStarMap();
    }

    private void toMagicSearch(List<String> searchPathList, String searchKey, long timeRange) {
        if (searchPathList.size() > 0) {
            String[] paths = new String[searchPathList.size()];
            searchPathList.toArray(paths);
            getView().getAdapterList().clear();
            final boolean[] newIsRef = {true};

            magicFileSearchDisponsable = MagicExplorer.getFileList(searchKey, isSearch, timeRange, new Callback1<List<MagicFileInfo>>() {
                @Override
                public void callListener(List<MagicFileInfo> param) {
                    if (!isSearch) {
                        if (!magicFileSearchDisponsable.isDisposed())
                            magicFileSearchDisponsable.dispose();
                        return;
                    }
                    getView().getDataListSucc(createFileModelList(param, searchKey), newIsRef[0]);
                    newIsRef[0] = false;
                }

                @Override
                public void callComplete() {

                }
            }, paths);
        }
    }

    private List<FileManagerListModel> createFileModelListByStar(List<FileStarInfo> list) {
        List<FileManagerListModel> newList = new ArrayList<>();
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
        return newList;
    }

    private List<FileManagerListModel> createFileModelList(List<MagicFileInfo> returnList, String keyWord) {
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
            if (TextUtils.isEmpty(keyWord)) {
                model.fileName = fileName;
            } else {
                model.fileName = TextUtils.replace(fileName, new String[]{keyWord}, new String[]{"<font color='red'>" + keyWord + "</font>"}).toString();
            }
            model.modifyStr = DateUtils.formatDate(info.modifyDate, "yyyy-MM-dd HH:mm:ss");
            model.sizeStr = Formatter.formatFileSize(getContext(), info.fileSize);
            model.isFile = info.isFile;
            list.add(model);
        }
        return list;
    }


    private List<FileManagerListModel> createFileModelList(List<FileInfo> list, String keyWord, Map<String, String> starMap) {
        List<FileManagerListModel> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            FileManagerListModel model = new FileManagerListModel();
            model.type = getView().getType() == FileType.PIC ? FileManagerListModel.PIC : FileManagerListModel.OTHER;
            model.fileType = EnumFileType.getOldType(list.get(i).getFileType());
            model.picPath = list.get(i).getFilePath();
            if (TextUtils.isEmpty(keyWord)) {
                model.fileName = FileUtils.getFolderName(model.picPath);
            } else {
                model.fileName = TextUtils.replace(FileUtils.getFolderName(model.picPath), new String[]{keyWord}, new String[]{"<font color='red'>" + keyWord + "</font>"}).toString();
            }
            model.isStar = starMap.get(model.picPath) != null;
            model.isSelect = false;
            model.isFile = list.get(i).getIsFile();
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
