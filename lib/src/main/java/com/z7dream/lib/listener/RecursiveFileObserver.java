package com.z7dream.lib.listener;

import android.os.FileObserver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.z7dream.lib.callback.Callback;
import com.z7dream.lib.db.FileDaoImpl;
import com.z7dream.lib.tool.CacheManager;
import com.z7dream.lib.tool.FileUtils;
import com.z7dream.lib.tool.rx.RxSchedulersHelper;

import java.io.File;
import java.util.Map;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

import static com.z7dream.lib.tool.MagicExplorer.HW_SCREEN_SAVER_PATH;
import static com.z7dream.lib.tool.MagicExplorer.QQ_FILE_PATH;
import static com.z7dream.lib.tool.MagicExplorer.QQ_PIC_PATH;
import static com.z7dream.lib.tool.MagicExplorer.SCREENSHOTS_PATH;
import static com.z7dream.lib.tool.MagicExplorer.SYS_CAMERA_PATH;
import static com.z7dream.lib.tool.MagicExplorer.WX_FILE_PATH;
import static com.z7dream.lib.tool.MagicExplorer.WX_PIC_PATH;


/**
 * 文件增删改查监听
 * <p>
 * Created by Z7Dream on 2017/7/3 16:39.
 * Email:zhangxyfs@126.com
 */

public class RecursiveFileObserver extends FileObserver {
    private Map<String, SingleFileObserver> mObservers;
    private String mPath;
    private int mMask;
    private static final String START_PATH = CacheManager.getSaveFilePath() + File.separator;
    private Callback<Integer> callback;
    private FileDaoImpl fileDaoUtils;

    private Disposable startDisposable, stopDisposable, createDisposable, deleteDisposable;

    public RecursiveFileObserver(String path, Callback<Integer> callback, FileDaoImpl fileDaoUtils) {
        this(path, ALL_EVENTS, callback, fileDaoUtils);
    }

    public RecursiveFileObserver(String path, int mask, Callback<Integer> callback, FileDaoImpl fileDaoUtils) {
        super(path, mask);
        mPath = path;
        mMask = mask;
        this.callback = callback;
        this.fileDaoUtils = fileDaoUtils;
    }

    @Override
    public void startWatching() {
        if (mObservers == null) {
            startDisposable = Observable.create((ObservableOnSubscribe<String>) e -> {
                mObservers = new ArrayMap<>();
                Stack<String> stack = new Stack<>();
                stack.push(mPath);
                while (!stack.isEmpty()) {
                    String temp = stack.pop();
                    mObservers.put(temp, new SingleFileObserver(temp, mMask));
                    File path = new File(temp);
                    File[] files = path.listFiles();
                    if (null == files)
                        continue;
                    for (File f : files) {
                        // 递归监听目录
                        if (f.isDirectory() && isNeedToListener(f.getPath())) {
                            stack.push(f.getAbsolutePath());
                        }
                    }
                }
                for (String key : mObservers.keySet()) {
                    e.onNext(key);
                }
                e.onComplete();

            }).compose(RxSchedulersHelper.io()).doOnComplete(() -> {
                startDisposable.dispose();
                startDisposable = null;
                callback.callListener(mObservers.size());
            }).subscribe(key -> {
                mObservers.get(key).startWatching();
            }, error -> {
            });
        }
    }

    @Override
    public void stopWatching() {
        if (mObservers != null) {
            stopDisposable = Observable.create((ObservableOnSubscribe<String>) e -> {
                for (String key : mObservers.keySet()) {
                    e.onNext(key);
                }
                e.onComplete();
            }).compose(RxSchedulersHelper.io())
                    .doOnComplete(() -> {
                        mObservers.clear();
                        mObservers = null;
                        stopDisposable.dispose();
                        stopDisposable = null;

                        fileDaoUtils = null;
                    })
                    .subscribe(key -> {
                        mObservers.get(key).stopWatching();
                    }, error -> {
                    });
        }
    }

    @Override
    public void onEvent(int event, String path) {
        File file = new File(path);
        int el = event & FileObserver.ALL_EVENTS;
        switch (el) {
            case FileObserver.ATTRIB:
                Log.i("RecursiveFileObserver", "ATTRIB: " + path);
                break;
            case FileObserver.CREATE:
                createDisposable = Observable.create((ObservableOnSubscribe<SingleFileObserver>) e -> {
                    if (file.isDirectory()) {
                        Stack<String> stack = new Stack<>();
                        stack.push(path);
                        while (!stack.isEmpty()) {
                            String temp = stack.pop();
                            if (mObservers.containsKey(temp)) {
                                continue;
                            } else {
                                SingleFileObserver sfo = new SingleFileObserver(temp, mMask);
                                e.onNext(sfo);
                                mObservers.put(temp, sfo);
                            }
                            File tempPath = new File(temp);
                            File[] files = tempPath.listFiles();
                            if (null == files)
                                continue;
                            for (File f : files) {
                                // 递归监听目录
                                if (f.isDirectory() && isNeedToListener(f.getPath())) {
                                    stack.push(f.getAbsolutePath());
                                }
                            }
                        }
                    }
                    e.onComplete();
                }).compose(RxSchedulersHelper.io())
                        .doOnComplete(() -> {
                            createDisposable.dispose();
                            createDisposable = null;
                        }).subscribe(FileObserver::startWatching, error -> {
                        });

                fileDaoUtils.addFileInfo(file);
                Log.i("RecursiveFileObserver", "CREATE: " + path);
                break;
            case FileObserver.DELETE:
                if (file.isDirectory()) {
                    mObservers.get(path).stopWatching();
                    mObservers.remove(path);
                }
                fileDaoUtils.removeFileInfo(file.getAbsolutePath());

                Log.i("RecursiveFileObserver", "DELETE: " + path);
                break;
            case FileObserver.DELETE_SELF:
                if (file.isDirectory()) {
                    mObservers.get(path).stopWatching();
                    mObservers.remove(path);
                }
                fileDaoUtils.removeFileInfo(file.getAbsolutePath());

                Log.i("RecursiveFileObserver", "DELETE_SELF: " + path);
                break;
            case FileObserver.MODIFY:
                fileDaoUtils.updateFileInfo(file);
                Log.i("RecursiveFileObserver", "MODIFY: " + path);
                break;
            case FileObserver.MOVE_SELF:
                Log.i("RecursiveFileObserver", "MOVE_SELF: " + path);
                break;
            case FileObserver.MOVED_FROM:
                Log.i("RecursiveFileObserver", "MOVED_FROM: " + path);
                break;
            case FileObserver.MOVED_TO:
                Log.i("RecursiveFileObserver", "MOVED_TO: " + path);
                break;
        }
    }

    private class SingleFileObserver extends FileObserver {
        String mPath;

        public SingleFileObserver(String path) {
            this(path, ALL_EVENTS);
            mPath = path;
        }

        SingleFileObserver(String path, int mask) {
            super(path, mask);
            mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            if (path != null) {
                String newPath = mPath + "/" + path;
                RecursiveFileObserver.this.onEvent(event, newPath);
            }
        }
    }

    private boolean isNeedToListener(String path) {
        if (TextUtils.isEmpty(path)) return false;
        //以下返回必须为false
        String fileName = FileUtils.getFolderName(path);
        boolean isHidden = fileName.startsWith("_") || fileName.startsWith(".");
        boolean isSystem = path.equals(START_PATH + "Android") || path.equals(START_PATH + "backup") || path.equals(START_PATH + "backups") || path.equals(START_PATH + "CloudDrive")
                || path.equals(START_PATH + "huawei") || path.equals(START_PATH + "HuaweiBackup") || path.equals(START_PATH + "HWThemes") || path.equals(START_PATH + "msc") || path.endsWith(START_PATH + "Musiclrc");
        boolean isRxCache = path.equals(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "rxCache");
        boolean isSmiley = path.equals(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "smiley");
        boolean isGlide = path.equals(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "glide");
        boolean isOSS = path.equals(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "oss_record");

        //以下返回必须为true
        boolean isQQ = path.startsWith(QQ_PIC_PATH) || path.startsWith(QQ_FILE_PATH);
        boolean isWX = path.startsWith(WX_PIC_PATH) || path.startsWith(WX_FILE_PATH);
        boolean isSysPic = path.equals(SYS_CAMERA_PATH) || path.equals(SCREENSHOTS_PATH) || path.equals(HW_SCREEN_SAVER_PATH);

        //以下部分返回为true
        boolean isTencentPath = path.startsWith(START_PATH + "tencent" + File.separator);

        if (isHidden || isSystem || isRxCache || isSmiley || isGlide || isOSS)
            return false;
        else if (isTencentPath)
            if (isQQ || isWX)
                return true;
            else
                return false;

        else if (isSysPic)
            return true;
        else
            return true;
    }
}
