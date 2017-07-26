package com.z7dream.manager.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.z7dream.lib.tool.FileType;
import com.z7dream.lib.tool.MagicExplorer;
import com.z7dream.lib.tool.Utils;
import com.z7dream.manager.R;
import com.z7dream.manager.base.mvp.presenter.impl.BasePresenterImpl;
import com.z7dream.manager.mvp.contract.FileBaseContract;
import com.z7dream.manager.mvp.ui.model.FileBaseListModel;
import com.z7dream.manager.tool.SPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Z7Dream on 2017/7/25 14:59.
 * Email:zhangxyfs@126.com
 */

public class FileBasePresenter extends BasePresenterImpl<FileBaseContract.View> implements FileBaseContract.Presenter {
    private FileBaseListModel childTypeModel;
    private List<FileBaseListModel> parentModelList;
    private List<Integer> childTypeList;
    private int icoSize;

    public FileBasePresenter(@NonNull Context context, @NonNull FileBaseContract.View view) {
        super(context, view);
        childTypeModel = new FileBaseListModel();
        parentModelList = new ArrayList<>();
        childTypeList = new ArrayList<>();
        icoSize = Utils.convertDipOrPx(context, 35);
    }

    @Override
    public void getDataList(boolean isRef) {
        if (parentModelList.size() == 0) {
            FileBaseListModel parent = new FileBaseListModel();
            parent.type = FileBaseListModel.PARENT;
            parent.icoResId = R.drawable.ic_file_mytypes;
            parent.titleName = getContext().getString(R.string.mine_file_sdcardtype_str);
            parent.id = Long.parseLong(String.valueOf(parentModelList.size()));
            parent.realPos = parentModelList.size();
            parent.parentPos = -1;
            parent.isExpand = true;
            parent.couldExpand = true;
            parentModelList.add(parent);

            parent = new FileBaseListModel();
            parent.type = FileBaseListModel.PARENT;
            parent.icoResId = R.drawable.ic_file_myfiles;
            parent.titleName = getContext().getString(R.string.mine_file_sdcardfolder_str);
            parent.id = Long.parseLong(String.valueOf(parentModelList.size()));
            parent.realPos = parentModelList.size();
            parent.parentPos = -1;
            parent.isExpand = false;
            parent.couldExpand = false;
            parentModelList.add(parent);

            parent = new FileBaseListModel();
            parent.type = FileBaseListModel.PARENT;
            parent.icoResId = R.drawable.ic_file_star;
            parent.titleName = getContext().getString(R.string.mine_file_collectfile_str);
            parent.id = Long.parseLong(String.valueOf(parentModelList.size()));
            parent.realPos = parentModelList.size();
            parent.parentPos = -1;
            parent.isExpand = false;
            parent.couldExpand = false;
            parentModelList.add(parent);

            parent = new FileBaseListModel();
            parent.type = FileBaseListModel.PARENT;
            parent.icoResId = R.drawable.ic_file_near30day;
            parent.titleName = getContext().getString(R.string.mine_file_near30day_str);
            parent.id = Long.parseLong(String.valueOf(parentModelList.size()));
            parent.realPos = parentModelList.size();
            parent.parentPos = -1;
            parent.isExpand = false;
            parent.couldExpand = false;
            parentModelList.add(parent);

            parent = new FileBaseListModel();
            parent.type = FileBaseListModel.PARENT;
            parent.icoResId = R.drawable.ic_file_qq;
            parent.titleName = getContext().getString(R.string.mine_file_qq_str);
            parent.id = Long.parseLong(String.valueOf(parentModelList.size()));
            parent.realPos = parentModelList.size();
            parent.parentPos = -1;
            parent.isExpand = false;
            parent.couldExpand = false;
            parentModelList.add(parent);

            parent = new FileBaseListModel();
            parent.type = FileBaseListModel.PARENT;
            parent.icoResId = R.drawable.ic_file_wps;
            parent.titleName = getContext().getString(R.string.mine_file_wps_str);
            parent.id = Long.parseLong(String.valueOf(parentModelList.size()));
            parent.realPos = parentModelList.size();
            parent.parentPos = -1;
            parent.isExpand = false;
            parent.couldExpand = false;
            parentModelList.add(parent);

            parent = new FileBaseListModel();
            parent.type = FileBaseListModel.PARENT;
            parent.icoResId = R.drawable.ic_file_wx;
            parent.titleName = getContext().getString(R.string.mine_file_wx_str);
            parent.id = Long.parseLong(String.valueOf(parentModelList.size()));
            parent.realPos = parentModelList.size();
            parent.parentPos = -1;
            parent.isExpand = false;
            parent.couldExpand = false;
            parentModelList.add(parent);

            //默认添加第一个child
            childTypeModel.type = FileBaseListModel.CHILD_TYPE;
            childTypeModel.dataJSON = getCacheChildTypeNumJSON();
            childTypeModel.parentPos = 0;
            childTypeModel.couldExpand = false;
            childTypeModel.isExpand = false;

            parentModelList.add(1, childTypeModel);

            getView().getDataListSucc(parentModelList, true);
        }

        if (childTypeModel.isNull())
            MagicExplorer.getAllCount(getContext(), param -> {
                String[] strs = new String[param.length];
                for (int i = 0; i < strs.length; i++) {
                    strs[i] = "(" + param[i] + ")";
                    SPreference.putString("other_" + childTypeList.get(i), strs[i]);
                }
                childTypeModel.dataJSON = new Gson().toJson(strs);
                childTypeModel.titleName = "childType";

                getView().setChildTypeData(childTypeModel);
            });
    }

    private String getCacheChildTypeNumJSON() {
        String[] contentStrs = new String[9];
        childTypeList.add(FileType.PIC);
        childTypeList.add(FileType.AUDIO);
        childTypeList.add(FileType.VIDEO);

        childTypeList.add(FileType.TXT);
        childTypeList.add(FileType.EXCEL);
        childTypeList.add(FileType.PPT);

        childTypeList.add(FileType.PDF);
        childTypeList.add(FileType.WORD);
        childTypeList.add(FileType.OTHER);

        for (int i = 0; i < contentStrs.length; i++) {
            contentStrs[i] = getContent(childTypeList.get(i));
        }

        return new Gson().toJson(contentStrs);
    }

    private String getContent(int fileType) {
        String content = SPreference.getString("other_" + fileType);
        if (!TextUtils.isEmpty(content)) {
            return content;
        }
        return "";
    }

    @Override
    public FileBaseListModel getChildTypeData() {
        return childTypeModel;
    }
}
