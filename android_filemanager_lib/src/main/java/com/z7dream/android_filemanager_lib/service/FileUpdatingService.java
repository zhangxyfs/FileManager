package com.z7dream.android_filemanager_lib.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import com.z7dream.android_filemanager_lib.db.FileDaoImpl;
import com.z7dream.android_filemanager_lib.db.FileDaoManager;
import com.z7dream.android_filemanager_lib.db.bean.MyObjectBox;
import com.z7dream.android_filemanager_lib.listener.FileConfigCallback;
import com.z7dream.android_filemanager_lib.listener.RecursiveFileObserver;
import com.z7dream.android_filemanager_lib.model.FileConfig;
import com.z7dream.android_filemanager_lib.tool.CacheManager;
import com.z7dream.android_filemanager_lib.tool.Utils;

import io.objectbox.BoxStore;

public class FileUpdatingService extends Service {
    private static RecursiveFileObserver recursiveFileObserver;
    private static FileDaoImpl fileDaoImpl;
    private static BoxStore mBoxStore;
    private static FileConfigCallback mFileConfigCallback;

    public FileUpdatingService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startObserver(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recursiveFileObserver != null)
            recursiveFileObserver.stopWatching();
        if (fileDaoImpl != null)
            fileDaoImpl.destory();
    }

    public void toUpdateFileInfos() {
        if (fileDaoImpl == null) return;
        if (fileDaoImpl.isPutFileInStorageSucc()) {

        }
    }

    /**
     * 开始监听（调用前需要开启权限，如果权限已开启，再打开应用时就不用再次调用）
     *
     * @param applicationContext
     */
    public static void startObserver(Context applicationContext) {
        boolean isNeedPermission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        if (isNeedPermission) return;

        if (fileDaoImpl == null)
            if (mBoxStore != null)
                fileDaoImpl = new FileDaoManager(applicationContext, mBoxStore);
            else {
                mBoxStore = MyObjectBox.builder().androidContext(applicationContext).build();
                fileDaoImpl = new FileDaoManager(applicationContext, mBoxStore);
            }
        //全盘文件夹监听
        if (recursiveFileObserver == null) {
            recursiveFileObserver = new RecursiveFileObserver(CacheManager.getSaveFilePath(), param -> {
                fileDaoImpl.toPutFileInStorage(param);
            }, fileDaoImpl);
            recursiveFileObserver.startWatching();
        }
    }

    public static BoxStore getBoxStore() {
        return mBoxStore;
    }

    /**
     * 打开服务
     *
     * @param boxStore
     * @param context
     */
    public static void startService(BoxStore boxStore, Context context) {
        if (mBoxStore == null && boxStore != null) {
            mBoxStore = boxStore;
        }

        if (!Utils.isServiceRunning(context, FileUpdatingService.class.getName())) {
            context.startService(new Intent(context, FileUpdatingService.class));
        }
    }

    public static void startService(Context context) {
        startService(null, context);
    }

    public static void startService(Context context, boolean isStartFileUpdating, FileConfigCallback fileConfigCallback) {
        setConfig(fileConfigCallback);
        if (isStartFileUpdating)
            startService(context);
    }

    public static void setConfig(FileConfigCallback fileConfigCallback) {
        mFileConfigCallback = fileConfigCallback;
    }

    public static FileConfigCallback getConfigCallback() {
        if (mFileConfigCallback == null) {
            mFileConfigCallback = FileConfig::new;
        }
        return mFileConfigCallback;
    }
}
