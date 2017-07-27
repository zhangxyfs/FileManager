package com.z7dream.lib.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.z7dream.lib.db.FileDaoImpl;
import com.z7dream.lib.db.FileDaoManager;
import com.z7dream.lib.db.bean.MyObjectBox;
import com.z7dream.lib.listener.FileConfigCallback;
import com.z7dream.lib.listener.RecursiveFileObserver;
import com.z7dream.lib.model.FileConfig;
import com.z7dream.lib.tool.CacheManager;
import com.z7dream.lib.tool.Utils;

import io.objectbox.BoxStore;

public class FileUpdatingService extends Service {
    private RecursiveFileObserver recursiveFileObserver;
    private FileDaoImpl fileDaoImpl;
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
        if (mBoxStore != null)
            fileDaoImpl = new FileDaoManager(this, mBoxStore);
        else {
            mBoxStore = MyObjectBox.builder().androidContext(getApplicationContext()).build();
            fileDaoImpl = new FileDaoManager(this, mBoxStore);
        }
        //全盘文件夹监听
        recursiveFileObserver = new RecursiveFileObserver(CacheManager.getSaveFilePath(), param -> {
            fileDaoImpl.toPutFileInStorage(param);
        }, fileDaoImpl);
        recursiveFileObserver.startWatching();
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

    public static BoxStore getBoxStore() {
        return mBoxStore;
    }

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
