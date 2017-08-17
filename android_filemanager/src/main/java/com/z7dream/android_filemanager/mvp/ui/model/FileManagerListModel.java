package com.z7dream.android_filemanager.mvp.ui.model;


import com.z7dream.android_filemanager.tool.recycler.BaseModel;

public class FileManagerListModel extends BaseModel {
    public static final int PIC = 1;
    public static final int OTHER = 2;

    public String picPath;//图片路径
    public String thumbnailPath;//缩略图路径
    public String fileName;
    public String fileRealName;
    public boolean isStar;//是否星标
    public boolean isSelect;//是否选中
    public Long companyId;
    public int iconResId;
    public String modifyStr;
    public String sizeStr;
    public int fileType;

    public boolean isFile = true;

}
