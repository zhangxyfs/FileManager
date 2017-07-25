package com.z7dream.lib.tool;

import java.io.File;

/**
 * Created by Z7Dream on 2017/7/21 16:59.
 * Email:zhangxyfs@126.com
 */

public class MagicExplorer {
    public static final String WPS_PATH = CacheManager.getSaveFilePath() + File.separator + "/Android/Data/cn.wps.moffice_eng/.cache/KingsoftOffice/.history/attach_mapping_v1.json";
    public static final String QQ_PIC_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "QQ_Images";
    public static final String QQ_FILE_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "QQfile_recv";
    public static final String WX_PIC_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "WeiXin";
    public static final String WX_FILE_PATH = CacheManager.getSaveFilePath() + File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "Download";
    public static final String SYS_CAMERA_PATH = CacheManager.getSaveFilePath() + File.separator + "DCIM" + File.separator + "Camera";
    public static final String SCREENSHOTS_PATH = CacheManager.getSaveFilePath() + File.separator + "Pictures";
    public static final String HW_SCREEN_SAVER_PATH = CacheManager.getSaveFilePath() + File.separator + "MagazineUnlock";
//    public static final String ES_PATH = CacheManager.getCachePath(Appli.getContext(), CacheManager.ES);
//    public static final String PIC_EBPHOTO_PATH = CacheManager.getCachePath(Appli.getContext(), CacheManager.EB_PHOTO);
}
