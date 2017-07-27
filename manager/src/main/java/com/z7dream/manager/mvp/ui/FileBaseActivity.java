package com.z7dream.manager.mvp.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.z7dream.lib.service.FileUpdatingService;
import com.z7dream.lib.tool.FileType;
import com.z7dream.lib.tool.Utils;
import com.z7dream.manager.R;
import com.z7dream.manager.R2;
import com.z7dream.manager.base.mvp.BaseActivity;
import com.z7dream.manager.mvp.contract.FileBaseContract;
import com.z7dream.manager.mvp.presenter.FileBasePresenter;
import com.z7dream.manager.mvp.ui.adapter.FileBaseListAdapter;
import com.z7dream.manager.mvp.ui.listener.FileBaseListListener;
import com.z7dream.manager.mvp.ui.model.FileBaseListModel;
import com.z7dream.manager.tool.recycler.RecyclerControl;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

import static com.z7dream.lib.tool.Utils.expandOCollapseAnim;


/**
 * 文件管理器
 * Created by Z7Dream on 2017/7/25 14:56.
 * Email:zhangxyfs@126.com
 */

public class FileBaseActivity extends BaseActivity<FileBaseContract.Presenter> implements FileBaseContract.View, SwipeRefreshLayout.OnRefreshListener,
        RecyclerControl.OnControlGetDataListListener, FileBaseListListener {
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerControl mRecyclerControl;

    private FileBaseListAdapter mFileBaseListAdapter;

    private int requestCode;
    private String titleName;
    private int picMaxSize;//最大图片数量
    private int fileMaxSize;//最大文件数量
    private int allMaxSize;//所有文件最多选择数量
    private boolean isToForward;//是否跳轉到轉發
    private boolean isNeedZip;//是否需要壓縮
    private boolean isNeedCrop;//是否需要裁剪

    public static final String FILE_REQUEST_CODE = "file_request_code";
    public static final String FILE_TITLE_NAME = "file_title_name";
    public static final String FILE_PIC_MAX = "file_pic_max";
    public static final String FILE_FILE_MAX = "file_file_max";
    public static final String FILE_ALL_MAX = "file_all_max";
    public static final String FILE_ALREADY_PIC_SELECT = "file_already_pic_select";
    public static final String FILE_ALREADY_FILE_SELECT = "file_already_file_select";
    public static final String FILE_IS_TOFORWARD = "file_is_toforward";
    public static final String FILE_IS_NEEDZIP = "file_is_needzip";
    public static final String FILE_IS_NEEDCROP = "file_is_needcrop";

    public static final int FILE_MANAGER_SELECT_REQUEST_CODE = 9166;
    public static final String PIC_INTENT_SELECT_DATA = "pic_select_data";
    public static final String FILE_INTENT_SELECT_DATA = "file_select_data";

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_filebase;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userToolbar(mToolbar, FileUpdatingService.getConfigCallback().getConfig().fileBaseTitle);
        if (getIntent() != null) {
            picMaxSize = getIntent().getIntExtra(FILE_PIC_MAX, 9);
            fileMaxSize = getIntent().getIntExtra(FILE_FILE_MAX, 5);
            allMaxSize = getIntent().getIntExtra(FILE_ALL_MAX, 14);
            isToForward = getIntent().getBooleanExtra(FILE_IS_TOFORWARD, false);
            isNeedZip = getIntent().getBooleanExtra(FILE_IS_NEEDZIP, false);
            isNeedCrop = getIntent().getBooleanExtra(FILE_IS_NEEDCROP, false);
            requestCode = getIntent().getIntExtra(FILE_REQUEST_CODE, FILE_MANAGER_SELECT_REQUEST_CODE);
        }

        mFileBaseListAdapter = new FileBaseListAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFileBaseListAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerControl = new RecyclerControl(mSwipeRefreshLayout, mLinearLayoutManager, this);
        mRecyclerControl.setSwipeRefreshLayoutEnable(false);
    }

    @Override
    protected void data() {
        super.data();
        onRefresh();
    }

    @Override
    protected FileBaseContract.Presenter createPresenter() {
        return new FileBasePresenter(this, this);
    }

    @Override
    public void onRefresh() {
        mRecyclerControl.onRefresh();
    }

    @Override
    public void onErrorClickListener() {
        onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerControl.destory();
    }

    @Override
    public void onChildTypeClickListener(int viewId) {
        FileBaseListModel model = mFileBaseListAdapter.getList().get(1);
        if (model.type != FileBaseListModel.CHILD_TYPE) return;
        int fileType = -1;
        String titleName = "";
        switch (viewId) {
            case R2.id.ll_ifbct_pic:
                fileType = FileType.PIC;
                titleName = getString(R.string.file_pic_title_str);
                break;
            case R2.id.ll_ifbct_voice:
                fileType = FileType.AUDIO;
                titleName = getString(R.string.file_audio_title_str);
                break;
            case R2.id.ll_ifbct_video:
                fileType = FileType.VIDEO;
                titleName = getString(R.string.file_video_title_str);
                break;
            case R2.id.ll_ifbct_txt:
                fileType = FileType.TXT;
                titleName = getString(R.string.file_txt_title_str);
                break;
            case R2.id.ll_ifbct_excel:
                fileType = FileType.EXCEL;
                titleName = getString(R.string.file_excel_title_str);
                break;
            case R2.id.ll_ifbct_ppt:
                fileType = FileType.PPT;
                titleName = getString(R.string.file_ppt_title_str);
                break;
            case R2.id.ll_ifbct_word:
                fileType = FileType.WORD;
                titleName = getString(R.string.file_word_title_str);
                break;
            case R2.id.ll_ifbct_pdf:
                fileType = FileType.PDF;
                titleName = getString(R.string.file_pdf_title_str);
                break;
            case R2.id.ll_ifbct_other:
                fileType = FileType.OTHER;
                titleName = getString(R.string.file_other_title_str);
                break;
        }
        if (fileType >= 0)
            FileManagerActivity.open(this, titleName, fileType, picMaxSize, fileMaxSize, allMaxSize, FileManagerActivity.FUN_NORMAL, isToForward, isNeedZip, requestCode);

    }

    @Override
    public void onParentClickListener(int position, ImageView rightArrow, View line) {
        FileBaseListModel model = mFileBaseListAdapter.getList().get(position);
        if (model.couldExpand) {
            boolean isExpand = model.isExpand;
            if (position == 0) {
                if (isExpand) {
                    mFileBaseListAdapter.getList().remove(1);
                    mFileBaseListAdapter.notifyItemRemoved(1);
                    line.setVisibility(View.GONE);
                } else {
                    mFileBaseListAdapter.getList().add(1, getPresenter().getChildTypeData());
                    mFileBaseListAdapter.notifyItemInserted(1);
                    line.setVisibility(View.VISIBLE);
                }
            } else {
                if (model.type == FileBaseListModel.PARENT_COMP) {
                    if (!isExpand) {
//                        mFileBaseListAdapter.getList().addAll(position + 1, getPresenter().getCompanyList());
//                        mFileBaseListAdapter.notifyItemRangeInserted(position + 1, getPresenter().getCompanyList().size());
                    } else {
                        int size = 0;
                        for (Iterator it = mFileBaseListAdapter.getList().iterator(); it.hasNext(); ) {
                            FileBaseListModel m = (FileBaseListModel) it.next();
                            if (m.type == FileBaseListModel.CHILD_COMP || m.type == FileBaseListModel.CHILD_COMP_ITEM) {
                                size++;
                                if (m.type == FileBaseListModel.CHILD_COMP) {
                                    m.isExpand = false;
                                }
                                it.remove();
                            }
                        }
//                        for (int i = 0; i < getPresenter().getCompanyList().size(); i++) {
//                            getPresenter().getCompanyList().get(i).isExpand = false;
//                        }
                        mFileBaseListAdapter.notifyItemRangeRemoved(position + 1, size);
                    }
                }
            }
            model.isExpand = !isExpand;
            expandOCollapseAnim(model.isExpand, rightArrow);
        } else {
            switch (model.realPos) {
                case 1://本机文件（目录）
                    FileManagerActivity.openFolder(this, getString(R.string.mine_file_sdcardfolder_str), picMaxSize, fileMaxSize, allMaxSize, isToForward, isNeedZip, requestCode);
                    break;
                case 2://星标
                    FileManagerActivity.openCollection(this, getString(R.string.mine_file_collectfile_str), picMaxSize, fileMaxSize,
                            allMaxSize, isToForward, isNeedZip, requestCode);
                    break;
                case 3://30天
                   FileManagerActivity.open30Days(this, getString(R.string.mine_file_near30day_str), picMaxSize, fileMaxSize, allMaxSize, isToForward, isNeedZip, requestCode);
                    break;
                case 4://30天
                    FileManagerActivity.openQQ(this, getString(R.string.mine_file_qq_str), picMaxSize, fileMaxSize, allMaxSize, isToForward, isNeedZip, requestCode);
                    break;
                case 5://qq
                    FileManagerActivity.openWPS(this, getString(R.string.mine_file_wps_str), picMaxSize, fileMaxSize, allMaxSize, isToForward, isNeedZip, requestCode);
                    break;
                case 6://wps
                    FileManagerActivity.openWX(this, getString(R.string.mine_file_wx_str), picMaxSize, fileMaxSize, allMaxSize, isToForward, isNeedZip, requestCode);
                    break;
            }
        }
    }

    @Override
    public void onChildCompanyClickListener(int position, ImageView rightArrow) {
        FileBaseListModel model = mFileBaseListAdapter.getList().get(position);
        if (model.couldExpand) {
            if (model.isExpand) {
                mFileBaseListAdapter.collapseCompanyItem(position);
            } else {
                mFileBaseListAdapter.expandCompanyItem(position);
            }
            Utils.expandOCollapseAnim(model.isExpand, rightArrow);
        }
    }

    @Override
    public void onChildCompanyItemClickListener(int position) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == this.requestCode) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onControlGetDataList(boolean isRef) {
        getPresenter().getDataList(isRef);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void getDataListSucc(List<FileBaseListModel> dataList, boolean isRef) {
        getHandler().post(() -> {
            if (isRef) {
                mFileBaseListAdapter.getList().clear();
            }
            mFileBaseListAdapter.getList().addAll(dataList);
            mFileBaseListAdapter.notifyDataSetChanged();

            mRecyclerControl.getDataComplete(isRef);
        });
    }

    @Override
    public void getDataListFail(String errorStr, boolean isRef) {
        getHandler().post(() -> {
            mRecyclerControl.getDataComplete(isRef);
        });
    }

    @Override
    public void setChildTypeData(FileBaseListModel childTypeModel) {
        getHandler().post(() -> {
            mFileBaseListAdapter.getList().get(1).dataJSON = childTypeModel.dataJSON;
            mFileBaseListAdapter.getList().get(1).titleName = childTypeModel.titleName;
            mFileBaseListAdapter.notifyItemChanged(1);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    /**
     * 我的里打开文件管理
     *
     * @param activity
     */
    public static void open(Activity activity) {
        open(activity, activity.getString(R.string.filebase_toolbar_title),
                3, 1, 4, true, true, false, FILE_MANAGER_SELECT_REQUEST_CODE);
    }

    /**
     * 聊天打开文件管理
     *
     * @param activity
     */
    public static void openIM(Activity activity) {
        open(activity, activity.getString(R.string.filebase_toolbar_title), 9, 5, 14, false, true, false, FILE_MANAGER_SELECT_REQUEST_CODE);
    }

    /**
     * 打开文件管理器
     *
     * @param activity
     * @param picMaxSize  选择图片最大数量
     * @param fileMaxSize 选择文件最大数量
     */
    public static void open(Activity activity, int picMaxSize, int fileMaxSize) {
        open(activity, picMaxSize, fileMaxSize, FILE_MANAGER_SELECT_REQUEST_CODE);
    }

    /**
     * 打开文件管理器
     *
     * @param activity
     * @param picMaxSize  选择图片最大数量
     * @param fileMaxSize 选择文件最大数量
     * @param requestCode
     */
    public static void open(Activity activity, int picMaxSize, int fileMaxSize, int requestCode) {
        open(activity, activity.getString(R.string.filebase_toolbar_title), picMaxSize, fileMaxSize, picMaxSize + fileMaxSize,
                false, false, false, requestCode);
    }

    /**
     * 打开文件管理器
     *
     * @param activity
     * @param titleName   标题（不可修改）
     * @param picMaxSize  选择图片最大数量
     * @param fileMaxSize 选择文件最大数量
     * @param allMaxSize  所有文件最多选择数量
     * @param isToForward 是否可以转发
     * @param isNeedZip   是否可以压缩
     * @param isNeedCrop  是否可以裁剪（只有选择一张图时候好用）
     * @param requestCode
     */
    public static void open(Activity activity, String titleName, int picMaxSize, int fileMaxSize, int allMaxSize
            , boolean isToForward, boolean isNeedZip, boolean isNeedCrop, int requestCode) {
        Intent intent = new Intent(activity, FileBaseActivity.class);
        intent.putExtra(FILE_TITLE_NAME, titleName);
        intent.putExtra(FILE_PIC_MAX, picMaxSize);
        intent.putExtra(FILE_FILE_MAX, fileMaxSize);
        intent.putExtra(FILE_ALL_MAX, allMaxSize);
        intent.putExtra(FILE_IS_TOFORWARD, isToForward);
        intent.putExtra(FILE_IS_NEEDZIP, isNeedZip);
        intent.putExtra(FILE_IS_NEEDCROP, isNeedCrop);
        intent.putExtra(FILE_REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
    }
}
