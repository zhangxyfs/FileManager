package com.z7dream.manager.mvp.ui.model;


import android.text.TextUtils;

import com.z7dream.lib.tool.recycler.BaseModel;

public class FileBaseListModel extends BaseModel {
    public static final int PARENT = 1;
    public static final int PARENT_COMP = 2;
    public static final int CHILD_TYPE = 3;
    public static final int CHILD_COMP = 4;
    public static final int CHILD_COMP_ITEM = 5;

    public boolean isExpand = false;//是否展开
    public boolean couldExpand = false;//是否可以展开

    public int icoResId;//资源图片
    public String icoUrl;//图片url

    public String titleName;

    public int parentPos = -1;
    public Long id;
    public int realPos;

    public String dataJSON;//用于CHILD_TYPE


    public boolean isNull() {
        return TextUtils.isEmpty(titleName);
    }
}
