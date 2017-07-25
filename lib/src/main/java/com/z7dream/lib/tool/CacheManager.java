package com.z7dream.lib.tool;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.eblog.lib.Appli;
import com.eblog.lib.utils.rxjava.RxSchedulersHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * 缓存目录控制
 * Created by xiaoyu.zhang on 2016/11/10 14:57
 *  
 */
public class CacheManager {
    public static final int DEFAULT = 0x0000;
    public static final int APK = 0x00100;
    public static final int VOICE = 0x00200;
    public static final int VIDEO = 0x00300;
    public static final int PIC = 0x00400;
    public static final int DB = 0x00500;
    public static final int CONFIG = 0x00600;
    public static final int CACHE = 0x00700;
    public static final int USED = 0x00800;
    public static final int GIFT = 0x00900;
    public static final int GLIDE = 0x01000;
    public static final int CRASH = 0x01100;
    public static final int SMILEY = 0x01200;
    public static final int FILE = 0x01300;
    public static final int RES = 0x01400;
    public static final int OSS = 0x01500;
    public static final int IM = 0x01600;
    public static final int ES = 0x01700;//下属文件夹命名-公司id-文件类型-
    public static final int EB_PHOTO = 0x01800;
    public static final int COLLECT = 0x01900;
    public static final int PIC_TEMP = 0x02000;
    public static final int OFF_LINE_CACHE = 0x02001;

    public static final int TXT = 0x01710;
    public static final int EXCEL = 0x01720;
    public static final int PPT = 0x01730;
    public static final int WORD = 0x01740;
    public static final int PDF = 0x01750;
    public static final int OTHER = 0x01760;
    public static final int ES_ALL = 0x01799;


    private static final String STR_APK = "apk";
    private static final String STR_VOICE = "voice";
    private static final String STR_VIDEO = "video";
    private static final String STR_PIC = "picture";
    private static final String STR_DB = "database";
    private static final String STR_CFG = "config";
    private static final String STR_RX = "rxCache";
    private static final String STR_USED = "used";
    private static final String STR_GIFT = "gift";
    private static final String STR_GLIDE = "glide";
    private static final String STR_CRASH = "crash";
    private static final String STR_SMILEY = "smiley";
    private static final String STR_FILE = "file";
    private static final String STR_RES = "res";
    private static final String STR_OSS = "oss_record";
    private static final String STR_IM = "im";
    private static final String STR_ES = "es";
    private static final String STR_TXT = "txt";
    private static final String STR_EXCEL = "excel";
    private static final String STR_PPT = "ppt";
    private static final String STR_WORD = "word";
    private static final String STR_PDF = "pdf";
    private static final String STR_OTHER = "other";
    private static final String STR_EB_PHOTO = "EB_photo";
    private static final String STR_COLLECT = "collect";
    private static final String STR_PIC_TEMP = "picTemp";
    private static final String STR_OFF_LINE_CACHE = "offlinecache";

    private static final String NOMEDIA = ".nomedia";


    public static String getSystemPicCachePath() {
        File file = Appli.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (file != null) {
            return file.getAbsolutePath() + File.separator;
        }
        return "Android" + File.separator + "data" + File.separator + "com.eblog" + File.separator + "files" + File.separator + "Pictures" + File.separator;
    }

    public static File getSystemPicCachePathFile() {
        File file = Appli.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (file != null) {
            return file;
        }
        file = new File("Android" + File.separator + "data" + File.separator + "com.eblog" + File.separator + "files" + File.separator + "Pictures");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取缓存路径
     */
    public static String getCachePath(Context context) {
        String savePath = getSaveFilePath() + File.separator + context.getPackageName() + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    public static String getRelativePath(Context context) {
        String savePath = getSaveFilePath() + File.separator + context.getPackageName() + File.separator + "cache";
        String relatviePath = File.separator + context.getPackageName() + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }

        return relatviePath;
    }

    public static String getCachePath() {
        String savePath = getSaveFilePath() + File.separator + "com.eblog" + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    public static String getRelativePath() {
        String savePath = getSaveFilePath() + File.separator + "com.eblog" + File.separator + "cache";
        String relatviePath = File.separator + "com.eblog" + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return relatviePath;
    }

    public static String getCachePath(int which) {
        return getCachePath(null, which);
    }

    public static String getCachePathNoSepa(int which) {
        String path = getCachePath(null, which);
        if (path.endsWith(File.separator)) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static String getCachePath(Context context, int which) {
        String savePath = "";
        if (context == null) {
            savePath = getCachePath();
        } else {
            savePath = getCachePath(context);
        }
        savePath = getSavePath(savePath, which);
        String nomediaPath = savePath + NOMEDIA;

        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (!savePath.contains(STR_EB_PHOTO)) {
            if (!npDir.exists()) {
                npDir.mkdir();
            }
        } else {
            if (npDir.exists())
                npDir.delete();
        }
        return savePath;
    }

    public static void toHiddenFolder() {
        Flowable.create((FlowableOnSubscribe<File>) flowableEmitter -> {
            List<String> list = new ArrayList<>();
            MagicExplorer.getFolderList(getCachePath(), list);
            for (int i = 0; i < list.size(); i++) {
                flowableEmitter.onNext(new File(list.get(i)));
            }
        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.fio())
                .filter(f -> !f.isHidden())
                .subscribe(file -> {
                    if (!file.getPath().contains(STR_EB_PHOTO)) {

                    }
                }, error -> {
                });

    }

    public static String getRelativePath(Context context, int which) {
        String savePath = "";
        if (context == null) {
            savePath = getRelativePath();
        } else {
            savePath = getRelativePath(context);
        }
        savePath = getSavePath(savePath, which);
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    private static String getSavePath(String savePath, int which) {
        savePath += File.separator;
        if (which == APK) {
            savePath += STR_APK;
        } else if (which == VOICE) {
            savePath += STR_VOICE;
        } else if (which == VIDEO) {
            savePath += STR_VIDEO;
        } else if (which == PIC) {
            savePath += STR_PIC;
        } else if (which == DB) {
            savePath += STR_DB;
        } else if (which == CONFIG) {
            savePath += STR_CFG;
        } else if (which == CACHE) {
            savePath += STR_RX;
        } else if (which == USED) {
            savePath += STR_USED;
        } else if (which == GIFT) {
            savePath += STR_GIFT;
        } else if (which == GLIDE) {
            savePath += STR_GLIDE;
        } else if (which == CRASH) {
            savePath += STR_CRASH;
        } else if (which == SMILEY) {
            savePath += STR_SMILEY;
        } else if (which == FILE) {
            savePath += STR_FILE;
        } else if (which == RES) {
            savePath += STR_RES;
        } else if (which == OSS) {
            savePath += STR_OSS;
        } else if (which == IM) {
            savePath += STR_IM;
        } else if (which == ES) {
            savePath += STR_ES;
        } else if (which == TXT) {
            savePath += STR_TXT;
        } else if (which == EXCEL) {
            savePath += STR_EXCEL;
        } else if (which == PPT) {
            savePath += STR_PPT;
        } else if (which == WORD) {
            savePath += STR_WORD;
        } else if (which == PDF) {
            savePath += STR_PDF;
        } else if (which == OTHER) {
            savePath += STR_OTHER;
        } else if (which == ES_ALL) {
            savePath = savePath.substring(0, savePath.length() - 1);
        } else if (which == EB_PHOTO) {
            savePath += STR_EB_PHOTO;
        } else if (which == COLLECT) {
            savePath += STR_COLLECT;
        } else if (which == PIC_TEMP) {
            savePath += STR_PIC_TEMP;
        } else if (which == OFF_LINE_CACHE) {
            savePath += STR_OFF_LINE_CACHE;
        }
        savePath += File.separator;
        return savePath;
    }

    /**
     * 帮你创建个目录
     *
     * @param which           父目录
     * @param childFolderName 子目录名
     * @return
     */
    public static String getPath(int which, String childFolderName) {
        String path = getCachePath(Appli.getContext(), which);
        String needPath = path + childFolderName + File.separator;
        String nomediaPath = needPath + NOMEDIA;

        File fDir = new File(needPath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (!npDir.exists()) {
            npDir.mkdir();
        }
        return needPath;
    }

    /**
     * 获得 es 下的目录
     *
     * @param which
     * @param companyId
     * @return
     */
    public static String getEsCompanyPath(int which, Long companyId) {
        String rootPath = getPath(ES, String.valueOf(companyId));
        if (rootPath.endsWith(File.separator)) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
        rootPath = getSavePath(rootPath, which);
        String nomediaPath = rootPath + NOMEDIA;

        File fDir = new File(rootPath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (!npDir.exists()) {
            npDir.mkdir();
        }

        return rootPath;
    }

    /**
     * 获取im路径
     *
     * @param which
     * @return
     */
    public static String getIMPath(int which) {
        String rootPath = getCachePath(Appli.getContext(), IM);
        if (rootPath.endsWith(File.separator)) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
        rootPath = getSavePath(rootPath, which);
        String nomediaPath = rootPath + NOMEDIA;

        File fDir = new File(rootPath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (!npDir.exists()) {
            npDir.mkdir();
        }

        return rootPath;
    }

    private static String getAppCacheRoot(Context context) {
        String status = Environment.getExternalStorageState();
        if (TextUtils.equals(status, Environment.MEDIA_MOUNTED)) {
            return context.getExternalFilesDir("").getAbsolutePath();
        } else {
            return getSaveFilePath();
        }
    }

    /**
     * 生成下载文件保存路径
     *
     * @return
     */
    public static String getSaveFilePath() {
        File file = null;
        String rootPath = "";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            file = Environment.getExternalStorageDirectory();//获取跟目录
            rootPath = file.getPath();
        } else {
            Dev_MountInfo dev = Dev_MountInfo.getInstance();
            Dev_MountInfo.DevInfo info = dev.getInternalInfo();
            if (info != null) {
                rootPath = info.getPath();
            } else {
                return "";
            }
        }
        return rootPath;
    }

    public static String getDownloadFile(String downloadUrl, int which) {
        String[] strs = downloadUrl.split("/");
        String fileName = strs[strs.length - 1];
        if (fileName.split("\\.").length > 1) {
            String filePath = getCachePath(Appli.getContext(), which) + fileName;
            File file = new File(filePath);
            if (file.isFile()) {
                return filePath;
            }
        }
        return "";
    }

    /**
     * 获取离线路径
     *
     * @param which
     * @return
     */
    public static String getOffLineCachePath(int which) {
        String rootPath = getCachePath(Appli.getContext(), OFF_LINE_CACHE);
        if (rootPath.endsWith(File.separator)) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
//        rootPath = getSavePath(rootPath, which);
//        String nomediaPath = rootPath + NOMEDIA;
//
//        File fDir = new File(rootPath);
//        if (!fDir.exists()) {
//            fDir.mkdirs();
//        }
//        File npDir = new File(nomediaPath);
//        if (!npDir.exists()) {
//            npDir.mkdir();
//        }

        return rootPath;
    }

    public static void mkDir(File file) {
        if (file.getParentFile().exists()) {
            file.mkdir();
        } else {
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }

    public static String getParent(String child) {
        return new File(child).getParentFile().getAbsolutePath();
    }

}
