package com.z7dream.android_filemanager.tool.recycler;


import static android.drm.DrmManagerClient.ERROR_NONE;

/**
 * Created by xiaoyu.zhang on 2016/6/24.
 */

public class BaseModel {
    public static final int ERROR = 500;

    public int type;
    //Error
    public int noDataIvSize;
    public int noDataIvResId;
    public String noDataTvStr;
    public boolean isError;
    public boolean isHasHead;
    public int errorStatus = ERROR_NONE;

    public int itemSize;
}
