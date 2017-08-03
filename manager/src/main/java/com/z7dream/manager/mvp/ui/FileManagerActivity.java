package com.z7dream.manager.mvp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.z7dream.lib.callback.Callback;
import com.z7dream.lib.tool.CacheManager;
import com.z7dream.lib.tool.FileType;
import com.z7dream.lib.tool.FileUtils;
import com.z7dream.lib.tool.MedaPlayUtil;
import com.z7dream.lib.tool.OpenFileUtils;
import com.z7dream.lib.tool.Utils;
import com.z7dream.lib.tool.WPSUtils;
import com.z7dream.manager.R;
import com.z7dream.manager.R2;
import com.z7dream.manager.base.mvp.BaseActivity;
import com.z7dream.manager.mvp.contract.FileManagerContract;
import com.z7dream.manager.mvp.model.FileManagerModel;
import com.z7dream.manager.mvp.presenter.FileManagerPresenter;
import com.z7dream.manager.mvp.ui.adapter.FileManagerListAdapter;
import com.z7dream.manager.mvp.ui.listener.FileManagerListListener;
import com.z7dream.manager.mvp.ui.model.FileManagerListModel;
import com.z7dream.manager.tool.luban.Luban;
import com.z7dream.manager.tool.luban.OnCompressListener;
import com.z7dream.manager.tool.recycler.RecyclerControl;
import com.z7dream.manager.tool.smoothCompound.SmoothCheckBox;
import com.z7dream.manager.widget.FileManagerDialog;
import com.z7dream.manager.widget.TCheckBox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.z7dream.manager.mvp.ui.FileBaseActivity.FILE_ALL_MAX;
import static com.z7dream.manager.mvp.ui.FileBaseActivity.FILE_FILE_MAX;
import static com.z7dream.manager.mvp.ui.FileBaseActivity.FILE_INTENT_SELECT_DATA;
import static com.z7dream.manager.mvp.ui.FileBaseActivity.FILE_IS_NEEDZIP;
import static com.z7dream.manager.mvp.ui.FileBaseActivity.FILE_IS_TOFORWARD;
import static com.z7dream.manager.mvp.ui.FileBaseActivity.FILE_PIC_MAX;
import static com.z7dream.manager.mvp.ui.FileBaseActivity.FILE_TITLE_NAME;
import static com.z7dream.manager.mvp.ui.FileBaseActivity.PIC_INTENT_SELECT_DATA;

/**
 * Created by Z7Dream on 2017/7/26 10:28.
 * Email:zhangxyfs@126.com
 */

public class FileManagerActivity extends BaseActivity<FileManagerContract.Presenter> implements FileManagerContract.View
        , SwipeRefreshLayout.OnRefreshListener, RecyclerControl.OnControlGetDataListListener, FileManagerListListener, Toolbar.OnMenuItemClickListener {
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @BindView(R2.id.ll_afm_search)
    LinearLayout ll_afm_search;

    @BindView(R2.id.ll_search)
    LinearLayout ll_search;

    @BindView(R2.id.ll_afmo)
    LinearLayout ll_afmo;

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private GridLayoutManager mGridLayoutManager;
    private RecyclerControl mRecyclerControl;

    private FileManagerListAdapter fileManagerListAdapter;
    private FileManagerDialog fileManagerDialog;

    private String titleName;
    private int maxFileNum = 9, maxPicNum = 9, allMaxNum = 18;
    private int function; //功能选择（0：正常，1：收藏，2：最近，3：qq，4：wps，5：wx，6：目录，7：统计导出文件）
    private String rootPath, nowPath;
    private boolean isNeedForward;

    private Map<Integer, Integer> checkPicMap, checkFileMap, checkMap;
    private MenuItem searchItem, choiceItem;
    private boolean isOpenCheck = false;
    private boolean isAllCheckClick = false;
    private boolean isCheckAllStar = true;//选中的是否全是星标

    private boolean isNormal = false;//是否正常的
    private boolean isCollection = false;//是否显示收藏列表
    private boolean isNear30Days = false;//是否显示最近30天的数据
    private boolean isQQ = false;//是否显示qq数据
    private boolean isWPS = false;//是否显示wps数据
    private boolean isWX = false;//是否显示wx数据
    private boolean isFolder = false;//是否显示带目录的数据列表
    private boolean isStatistical = false;//是否为统计导出文件
    private int fileType = FileType.ALL;//文件类型
    private boolean isNeedLoadMore = true;//是否要加载更多

    private boolean isNeedZip;//是否压缩

    private int marginBottom;
    private ArrayList<String> needPicList;
    private MedaPlayUtil medaPlayUtil;

    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog progressDialog;

    public static final String FUNCTION_VALUE = "function_value";
    public static final String FILE_TYPE = "file_type";
    public static final String IS_NEED_LOADMORE = "is_need_loadmore";

    public static final int FUN_PIC = -1;
    public static final int FUN_NORMAL = 0;
    public static final int FUN_COLLECTION = 1;
    public static final int FUN_NEAR30DAY = 2;
    public static final int FUN_QQ = 3;
    public static final int FUN_WPS = 4;
    public static final int FUN_WX = 5;
    public static final int FUN_FOLDER = 6;
    public static final int FUN_STATISTICAL = 7;

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_filemanager;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getIntent() != null) {
            titleName = getIntent().getStringExtra(FILE_TITLE_NAME);
            fileType = getIntent().getIntExtra(FILE_TYPE, FileType.ALL);
            maxPicNum = getIntent().getIntExtra(FILE_PIC_MAX, 9);
            maxFileNum = getIntent().getIntExtra(FILE_FILE_MAX, 9);
            allMaxNum = getIntent().getIntExtra(FILE_ALL_MAX, 18);
            function = getIntent().getIntExtra(FUNCTION_VALUE, 0);
            isNeedForward = getIntent().getBooleanExtra(FILE_IS_TOFORWARD, false);
            isNeedZip = getIntent().getBooleanExtra(FILE_IS_NEEDZIP, true);
            isNeedLoadMore = getIntent().getBooleanExtra(IS_NEED_LOADMORE, true);
        }

        isNormal = function == 0;
        isCollection = function == 1;
        isNear30Days = function == 2;
        isQQ = function == 3;
        isWPS = function == 4;
        isWX = function == 5;
        isFolder = function == 6;
        isStatistical = function == 7;

        if (fileType == FileType.PIC || getPresenter().getFileConfig().isToolbarSearch) {
            ll_afm_search.setVisibility(View.GONE);
        }

        if (!getPresenter().getFileConfig().isVisableSearch) {
            ll_afm_search.setVisibility(View.GONE);
        }

        needPicList = new ArrayList<>();

        if (TextUtils.isEmpty(titleName)) {
            titleName = getString(R.string.mine_file_sdcardfolder_str);
        }

        userToolbar(mToolbar, titleName, v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        mToolbar.setOnMenuItemClickListener(this);
        fileManagerListAdapter = new FileManagerListAdapter(this);
        mGridLayoutManager = new GridLayoutManager(this, fileType == FileType.PIC ? 3 : 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(fileManagerListAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerControl = new RecyclerControl(mSwipeRefreshLayout, mGridLayoutManager, this);
        mRecyclerControl.setSwipeRefreshLayoutEnable(false);
        if (isNeedLoadMore)
            mRecyclerView.addOnScrollListener(mRecyclerControl.getOnScrollListener());

        checkPicMap = new HashMap<>();
        checkFileMap = new HashMap<>();
        checkMap = new HashMap<>();

        fileManagerDialog = new FileManagerDialog(this, 0);
        fileManagerDialog.setRenameFocus(false);

        rootPath = CacheManager.getSaveFilePath();
        nowPath = rootPath;
        marginBottom = Utils.convertDipOrPx(this, 70);
        medaPlayUtil = new MedaPlayUtil();

        alertDialogBuilder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setMessage("请稍后…");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void data() {
        super.data();
        fileManagerDialog.setOnFileSendListener(view -> {
            if (checkMap.size() == 0)
                return;
            String[] paths = new String[checkMap.size()];
            List<String> dataList = new ArrayList<>();
            List<String> picList = new ArrayList<>();

            for (int pos : checkMap.keySet()) {
                FileManagerListModel model = fileManagerListAdapter.getList().get(pos);
                if (model.fileType == FileType.PIC) {
                    picList.add(model.picPath);
                } else {
                    dataList.add(model.picPath);
                }
            }
            if (picList.size() > 0 && isNeedZip) {
                progressDialog.show();
                toCompressPic(picList, 0, param -> {
                    dataList.addAll(needPicList);
                    getHandler().post(() -> {
                        sendDataInfos(dataList, paths);
                    });
                });
            } else {
                sendDataInfos(dataList, paths);
            }
        });

        fileManagerDialog.setOnFileStarListener((view, b) -> {
            if (checkMap.size() == 0)
                return;

            List<String> filePathList = new ArrayList<>();
            for (Integer key : checkMap.keySet()) {
                FileManagerListModel model = fileManagerListAdapter.getList().get(key);
                if (isCheckAllStar) {//如果全部星标了
                    if (model.isStar) {
                        filePathList.add(model.picPath);
                    }
                } else {
                    if (!model.isStar) {
                        filePathList.add(model.picPath);
                    }
                }
            }
            if (isCheckAllStar) {//如果全部星标了
                getPresenter().removeStarFiles(filePathList);
            } else {
                getPresenter().toStarFiles(filePathList);
            }
            if (fileManagerDialog.isShow()) {
                choiceItem.setTitle(R.string.select_all_str);
                clearCheck();
            }
            setToolbarTitle(titleName);
        });
        fileManagerDialog.setOnFileDeleteListener(view -> {
            if (checkMap.size() == 0)
                return;

            List<String> filePathList = new ArrayList<>();
            for (Integer key : checkMap.keySet()) {
                FileManagerListModel model = fileManagerListAdapter.getList().get(key);
                filePathList.add(model.picPath);
            }
            getPresenter().deleteFiles(filePathList);

            if (fileManagerDialog.isShow()) {
                choiceItem.setTitle(R.string.select_all_str);
                clearCheck();
            }
            setToolbarTitle(titleName);
        });

        fileManagerDialog.setOnFileRenameListener(view -> {
            if (checkMap.size() != 1)
                return;
            int position = 0;
            for (int i : checkMap.keySet()) {
                position = i;
            }
            createRenameDialog(position);
            setToolbarTitle(titleName);
        });
        onRefresh();
    }

    @Override
    protected FileManagerContract.Presenter createPresenter() {
        return new FileManagerPresenter(this, this);
    }

    private void toCompressPic(List<String> picList, int pos, Callback<String> callback) {
        if (pos < picList.size())
            Luban.get(this).load(picList.get(pos)).putGear(Luban.THIRD_GEAR).setBitFormat(false).setCompressListener(new OnCompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(File file) {
                    needPicList.add(file.getPath());
                    toCompressPic(picList, pos + 1, callback);
                }

                @Override
                public void onError(Throwable e) {
                    needPicList.add(picList.get(pos));
                    toCompressPic(picList, pos + 1, callback);
                }
            }).launch();
        else
            callback.callListener("succ");
    }

    private void sendDataInfos(List<String> dataList, String[] paths) {

        if (!isNeedForward) {
            dataList.toArray(paths);
//            RxBus.get().post(fileRxKey, paths);

            ArrayList<FileManagerModel> fileList = new ArrayList<>();
            ArrayList<FileManagerModel> picList = new ArrayList<>();

            for (int i = 0; i < paths.length; i++) {
                String exc = FileUtils.getExtensionName(paths[i]);
                if (FileType.createFileType(exc) == FileType.PIC) {
                    FileManagerModel model = new FileManagerModel();
                    model.fileName = FileUtils.getFolderName(paths[i]);
                    model.filePath = paths[i];
                    model.resId = R.drawable.ic_file_pic;
                    picList.add(model);
                } else {
                    FileManagerModel model = new FileManagerModel();
                    model.fileName = FileUtils.getFolderName(paths[i]);
                    model.filePath = paths[i];
                    int type = FileType.createFileType(exc);
                    model.resId = FileType.createIconResId(type);
                    fileList.add(model);
                }
            }

            FileManagerModel[] models = new FileManagerModel[fileList.size()];
            String[] pics = new String[picList.size()];
            fileList.toArray(models);
            picList.toArray(pics);

            Intent intent = getIntent();
            intent.putExtra(PIC_INTENT_SELECT_DATA, pics);
            intent.putExtra(FILE_INTENT_SELECT_DATA, models);

            setResult(RESULT_OK, intent);

            close();
            finish();
        } else {
            //todo forward
        }
    }

    private void createRenameDialog(int position) {
        FileManagerListModel model = fileManagerListAdapter.getList().get(position);
        View renameChildView = LayoutInflater.from(this).inflate(R.layout.widget_alert_edit, null);
        EditText renameEt = renameChildView.findViewById(R.id.et_wae);
        renameEt.setText(model.fileName);
        renameEt.setSelection(0, model.fileName.length());
        AlertDialog.Builder renameDialogBuilder = new AlertDialog.Builder(this).setView(renameChildView)
                .setPositiveButton(R.string.confirm_str, (dialog, which) -> {
                    getPresenter().renameFile(position, renameEt.getText().toString().trim());
                }).setNegativeButton(R.string.cancel_str, (dialog, which) -> {

                });
        renameEt.setHint(FileUtils.getFileNameNoEx(model.fileRealName));
        renameDialogBuilder.show();
    }


    @Override
    public void finish() {
        if (isOpenCheck) {
            close();
        } else {
            super.finish();
            medaPlayUtil.destory();
            medaPlayUtil = null;
            progressDialog.dismiss();
            progressDialog = null;
            alertDialogBuilder = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.equals(nowPath, rootPath)) {
            File file = new File(nowPath);
            nowPath = file.getParent();
            onRefresh();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }


    private void close() {
        setToolbarTitle(titleName);
        choiceItem.setTitle(R.string.choice_str);
        isOpenCheck = false;
        fileManagerListAdapter.setCheck(false);
        fileManagerDialog.dismiss();
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) ll_afmo.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        ll_afmo.setLayoutParams(lp);

        clearCheck();
    }

    private void clearCheck() {
        isAllCheckClick = false;
        checkMap.clear();
        checkPicMap.clear();
        checkFileMap.clear();
        List<FileManagerListModel> list = fileManagerListAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isSelect = false;
        }
        fileManagerListAdapter.notifyDataSetChanged();
    }


    @OnClick(R2.id.ll_search)
    void ll_search_click() {
//        FileManagerSearchActivity.openActivity(this, companyId, fileType, function);
    }

    @Override
    public void onRefresh() {
        mRecyclerControl.onRefresh();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.firstBtn) {

        } else if (item.getItemId() == R.id.secondBtn) {
            if (!isOpenCheck) {
                choiceItem.setTitle(R.string.select_all_str);
                isOpenCheck = true;
                fileManagerListAdapter.setCheck(true);
                fileManagerDialog.showPopup();
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) ll_afmo.getLayoutParams();
                lp.setMargins(0, 0, 0, marginBottom);
                ll_afmo.setLayoutParams(lp);
            } else {
                List<FileManagerListModel> list = fileManagerListAdapter.getList();
                if (isAllCheckClick) {
                    choiceItem.setTitle(R.string.select_all_str);
                    isAllCheckClick = false;
                    checkMap.clear();
                    checkPicMap.clear();
                    checkFileMap.clear();
                    for (int i = 0; i < list.size(); i++) {//取消全选
                        if (list.get(i).isSelect)
                            list.get(i).isSelect = false;
                    }
                } else {//全选
                    choiceItem.setTitle(R.string.clear_select_all_str);
                    isAllCheckClick = true;

                    if (allMaxNum == 1) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isFile) {
                                checkMap.put(i, i);
                                if (list.get(i).fileType == FileType.PIC) {
                                    checkPicMap.put(i, i);
                                } else {
                                    checkFileMap.put(i, i);
                                }
                                list.get(i).isSelect = true;
                                break;
                            }
                        }
                    } else {
                        int alreadySelectPicNum = checkPicMap.size(), alreadySelectFileNum = checkFileMap.size();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isFile) {
                                if (list.get(i).fileType == FileType.PIC) {
                                    if (alreadySelectPicNum < maxPicNum) {
                                        list.get(i).isSelect = true;
                                        checkPicMap.put(i, i);
                                        alreadySelectPicNum++;
                                    }
                                } else {
                                    if (alreadySelectFileNum < maxFileNum) {
                                        list.get(i).isSelect = true;
                                        checkFileMap.put(i, i);
                                        alreadySelectFileNum++;
                                    }
                                }
                            }
                            if (alreadySelectFileNum + alreadySelectPicNum == maxPicNum + maxFileNum)
                                break;
                        }
                        checkMap.putAll(checkPicMap);
                        checkMap.putAll(checkFileMap);
                    }
                }
                if (checkMap.size() > 0) {
                    if (allMaxNum < Integer.MAX_VALUE)
                        setToolbarTitle(getString(R.string.file_manager_already_num_str, checkMap.size()));
                    fileManagerDialog.setRenameFocus(checkMap.size() == 1);
                } else {
                    setToolbarTitle(titleName);
                    fileManagerDialog.setRenameFocus(false);
                }
                fileManagerListAdapter.notifyDataSetChanged();

                if (checkMap.size() > 0) {
                    for (Integer key : checkMap.keySet()) {
                        FileManagerListModel model = fileManagerListAdapter.getList().get(key);
                        isCheckAllStar = model.isStar;
                        if (!isCheckAllStar) {
                            break;
                        }
                    }
                    if (!isCheckAllStar) {
                        fileManagerDialog.setStarState(true);
                    } else {
                        fileManagerDialog.setStarState(false);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onErrorClickListener() {
        onRefresh();
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onCheckClickListener(View cb, int position, boolean isCheck) {
        getHandler().post(() -> {
            FileManagerListModel model = fileManagerListAdapter.getList().get(position);
            if (!model.isFile) {
                nowPath = model.picPath;
                onRefresh();
                return;
            }
            if (!isOpenCheck) {
                switch (model.fileType) {
                    case FileType.PIC:
//                    FilePicDisplayActivity.onlyDisplay(this, 1, 0, model.picPath);
                        break;
                    case FileType.TXT:
                    case FileType.EXCEL:
                    case FileType.PPT:
                    case FileType.WORD:
                    case FileType.PDF:
                        WPSUtils.openWpsFile(this, model.picPath);
                        break;
                    default:
                        OpenFileUtils.openFile(this, model.picPath);
                        break;
                }
                return;
            }

            if (allMaxNum >= 0 && checkMap.size() >= allMaxNum && isCheck) {
                if (cb instanceof SmoothCheckBox) {
                    ((SmoothCheckBox) cb).setChecked(false);
                } else if (cb instanceof TCheckBox) {
                    ((TCheckBox) cb).setChecked(false);
                }
                alertDialogBuilder.setMessage(getString(R.string.olny_choice_n_file_str, allMaxNum))
                        .setPositiveButton(R.string.ensure_str, (dialog, which) -> dialog.dismiss());
                return;
            }
            if (model.fileType == FileType.PIC) {
                if (maxPicNum >= 0 && checkPicMap.size() >= maxPicNum && isCheck) {
                    if (cb instanceof SmoothCheckBox) {
                        ((SmoothCheckBox) cb).setChecked(false);
                    } else if (cb instanceof TCheckBox) {
                        ((TCheckBox) cb).setChecked(false);
                    }
                    alertDialogBuilder.setMessage(getString(R.string.olny_choice_n_file_str, maxPicNum))
                            .setPositiveButton(R.string.ensure_str, (dialog, which) -> dialog.dismiss());
                    return;
                }
            } else {
                if (maxFileNum >= 0 && checkFileMap.size() >= maxFileNum && isCheck) {
                    if (cb instanceof SmoothCheckBox) {
                        ((SmoothCheckBox) cb).setChecked(false);
                    } else if (cb instanceof TCheckBox) {
                        ((TCheckBox) cb).setChecked(false);
                    }
                    alertDialogBuilder.setMessage(getString(R.string.olny_choice_n_file_str, maxFileNum))
                            .setPositiveButton(R.string.ensure_str, (dialog, which) -> dialog.dismiss());
                    return;
                }
            }

            if (isCheck) {//处理选中状态
                checkMap.put(position, position);
                if (model.fileType == FileType.PIC) {
                    checkPicMap.put(position, position);
                } else {
                    checkFileMap.put(position, position);
                }
                fileManagerListAdapter.getList().get(position).isSelect = true;
                if (checkMap.size() == allMaxNum) {
                    choiceItem.setTitle(R.string.clear_select_all_str);
                    isAllCheckClick = true;
                }
            } else {
                checkMap.remove(position);
                if (model.fileType == FileType.PIC) {
                    checkPicMap.remove(position);
                } else {
                    checkFileMap.remove(position);
                }
                fileManagerListAdapter.getList().get(position).isSelect = false;
                if (checkMap.size() == 0) {
                    choiceItem.setTitle(R.string.select_all_str);
                    isAllCheckClick = false;
                }
            }


            if (checkMap.size() > 0) {
                if (allMaxNum < Integer.MAX_VALUE)
                    setToolbarTitle(getString(R.string.file_manager_already_num_str, checkMap.size()));
                fileManagerDialog.setRenameFocus(checkMap.size() == 1);
            } else {
                setToolbarTitle(titleName);
                fileManagerDialog.setRenameFocus(true);
                fileManagerDialog.setStarState(true);
            }
            fileManagerListAdapter.notifyItemChanged(position);

            if (checkMap.size() > 0) {
                for (Integer key : checkMap.keySet()) {
                    isCheckAllStar = fileManagerListAdapter.getList().get(key).isStar;
                    if (!isCheckAllStar) {
                        break;
                    }
                }
                if (!isCheckAllStar) {
                    fileManagerDialog.setStarState(true);
                } else {
                    fileManagerDialog.setStarState(false);
                }
            } else {
                fileManagerDialog.setStarState(true);
            }
        });
    }


    @Override
    public void onControlGetDataList(boolean isRef) throws InterruptedException {
        if (isNormal) {
            getPresenter().getDataList(isRef);
        } else if (isCollection) {
            getPresenter().getCollectionDataList(isRef);
        } else if (isNear30Days) {
            getPresenter().getNear30DaysDataList(isRef);
        } else if (isQQ) {
            getPresenter().getQQDataList(isRef);
        } else if (isWPS) {
            getPresenter().getWPSDataList(isRef);
        } else if (isWX) {
            getPresenter().getWXDataList(isRef);
        } else if (isFolder) {
            getPresenter().getFolderDataList(nowPath);
        } else if (isStatistical) {
            getPresenter().getStatisticalDataList();
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }


    @Override
    public void getDataListSucc(List<FileManagerListModel> dataList, boolean isRef) {
        getHandler().post(() -> {
            if (isRef) {
                fileManagerListAdapter.getList().clear();
                fileManagerListAdapter.getList().addAll(dataList);
                fileManagerListAdapter.notifyDataSetChanged();
            } else {
                fileManagerListAdapter.appendToList(dataList);
            }
            mRecyclerControl.getDataComplete(isRef);
        });
    }

    @Override
    public void getDataListFail(String errorStr, boolean isRef) {
        mRecyclerControl.getDataComplete(isRef);
    }

    @Override
    public void openOperate() {
        if (!isOpenCheck) {
            choiceItem.setTitle(R.string.select_all_str);
            isOpenCheck = true;
            fileManagerListAdapter.setCheck(true);
            fileManagerDialog.showPopup();
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) ll_afmo.getLayoutParams();
            lp.setMargins(0, 0, 0, marginBottom);
            ll_afmo.setLayoutParams(lp);

            if (checkMap.size() > 0) {
                if (allMaxNum < Integer.MAX_VALUE)
                    setToolbarTitle(getString(R.string.file_manager_already_num_str, checkMap.size()));
                fileManagerDialog.setRenameFocus(checkMap.size() == 1);
            } else {
                setToolbarTitle(titleName);
                fileManagerDialog.setRenameFocus(false);
            }
            fileManagerListAdapter.notifyDataSetChanged();

            if (checkMap.size() > 0) {
                for (Integer key : checkMap.keySet()) {
                    FileManagerListModel model = fileManagerListAdapter.getList().get(key);
                    isCheckAllStar = model.isStar;
                    if (!isCheckAllStar) {
                        break;
                    }
                }
                if (!isCheckAllStar) {
                    fileManagerDialog.setStarState(true);
                } else {
                    fileManagerDialog.setStarState(false);
                }
            }
        }
    }

    @Override
    public String getNowPath() {
        return nowPath;
    }

    @Override
    public List<FileManagerListModel> getAdapterList() {
        return fileManagerListAdapter.getList();
    }

    @Override
    public void notifyItemChanged(int position) {
        fileManagerListAdapter.notifyItemChanged(position);
    }

    @Override
    public int getType() {
        return fileType;
    }

    @Override
    public boolean getIsNormal() {
        return isNormal;
    }

    @Override
    public boolean getIsCollection() {
        return isCollection;
    }

    @Override
    public boolean getIsNear30Days() {
        return isNear30Days;
    }

    @Override
    public boolean getIsQQ() {
        return isQQ;
    }

    @Override
    public boolean getIsWPS() {
        return isWPS;
    }

    @Override
    public boolean getIsWX() {
        return isWX;
    }

    @Override
    public boolean getIsFolder() {
        return isFolder;
    }

    @Override
    public boolean getIsStatistical() {
        return isStatistical;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu, menu);
        searchItem = menu.findItem(R.id.firstBtn);
        choiceItem = menu.findItem(R.id.secondBtn);
        choiceItem.setTitle(R.string.choice_str);
        searchItem.setIcon(R.drawable.ic_search);
        searchItem.setVisible(getPresenter().getFileConfig().isToolbarSearch);

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerControl.destory();
        fileManagerDialog.destory();
    }

    public static void openCollection(Activity context, String titleName
            , int picMax, int fileMax, int allMax
            , boolean isOpenForward, boolean isNeedZip, int requestCode) {
        open(context, titleName, FileType.ALL
                , picMax, fileMax, allMax
                , FUN_COLLECTION, isOpenForward, isNeedZip, true, requestCode);
    }

    public static void open30Days(Activity context, String titleName
            , int picMax, int fileMax, int allMax
            , boolean isOpenForward, boolean isNeedZip, int requestCode) {
        open(context, titleName, FileType.ALL
                , picMax, fileMax, allMax
                , FUN_NEAR30DAY, isOpenForward, isNeedZip, true, requestCode);
    }

    public static void openQQ(Activity context, String titleName
            , int picMax, int fileMax, int allMax
            , boolean isOpenForward, boolean isNeedZip, int requestCode) {
        open(context, titleName, FileType.ALL
                , picMax, fileMax, allMax
                , FUN_QQ, isOpenForward, isNeedZip, false, requestCode);
    }

    public static void openWPS(Activity context, String titleName
            , int picMax, int fileMax, int allMax
            , boolean isOpenForward, boolean isNeedZip, int requestCode) {
        open(context, titleName, FileType.ALL
                , picMax, fileMax, allMax
                , FUN_WPS, isOpenForward, isNeedZip, false, requestCode);
    }

    public static void openWX(Activity context, String titleName
            , int picMax, int fileMax, int allMax
            , boolean isOpenForward, boolean isNeedZip, int requestCode) {
        open(context, titleName, FileType.ALL
                , picMax, fileMax, allMax
                , FUN_WX, isOpenForward, isNeedZip, false, requestCode);
    }

    public static void openFolder(Activity context, String titleName
            , int picMax, int fileMax, int allMax
            , boolean isOpenForward, boolean isNeedZip, int requestCode) {
        open(context, titleName, FileType.ALL
                , picMax, fileMax, allMax, FUN_FOLDER
                , isOpenForward, isNeedZip, false, requestCode);
    }

    /**
     * 文件管理器
     *
     * @param activity
     * @param title          标题
     * @param fileType       文件类型
     * @param picMax         图片最大值
     * @param fileMax        文件最大值
     * @param allMax         所有最大值
     * @param function       类别
     * @param isToForward    是否转发
     * @param isNeedZip      是否压缩
     * @param isNeedLoadMore 是否有加载更多
     * @param requestCode
     */
    public static void open(Activity activity, String title, int fileType, int picMax, int fileMax, int allMax, int function, boolean isToForward, boolean isNeedZip, boolean isNeedLoadMore, int requestCode) {
        Intent intent = new Intent(activity, FileManagerActivity.class);
        intent.putExtra(FILE_TITLE_NAME, title);
        intent.putExtra(FILE_TYPE, fileType);
        intent.putExtra(FILE_PIC_MAX, picMax);
        intent.putExtra(FILE_FILE_MAX, fileMax);
        intent.putExtra(FILE_ALL_MAX, allMax);
        intent.putExtra(FUNCTION_VALUE, function);
        intent.putExtra(FILE_IS_TOFORWARD, isToForward);
        intent.putExtra(FILE_IS_NEEDZIP, isNeedZip);
        intent.putExtra(IS_NEED_LOADMORE, isNeedLoadMore);
        activity.startActivityForResult(intent, requestCode);
    }
}
