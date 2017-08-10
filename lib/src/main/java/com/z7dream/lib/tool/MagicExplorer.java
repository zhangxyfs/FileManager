package com.z7dream.lib.tool;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.z7dream.lib.callback.Callback;
import com.z7dream.lib.callback.Callback1;
import com.z7dream.lib.model.MagicFileInfo;
import com.z7dream.lib.tool.collator.OrderingConstants;
import com.z7dream.lib.tool.rx.RxSchedulersHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by Z7Dream on 2017/7/21 16:59.
 * Email:zhangxyfs@126.com
 */

public class MagicExplorer {
    private static final String VOLUME_NAME = "external";

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

    /**
     * 获取各种类型的文件数量
     *
     * @param callback
     */
    public static void getAllCount(Context context, Callback<long[]> callback) {
        long[] newNumbers = new long[9];
        Observable.create((ObservableOnSubscribe<Long>) e -> {
            MagicExplorer.getAllPicCount(context, e::onNext);
        }).flatMap(num -> {
            newNumbers[0] = num;
            return Observable.create((ObservableOnSubscribe<Long>) e -> {
                MagicExplorer.getAllVoiceCount(context, e::onNext);
            });
        }).flatMap(num -> {
            newNumbers[1] = num;
            return Observable.create((ObservableOnSubscribe<Long>) e -> {
                MagicExplorer.getAllVideoCount(context, e::onNext);
            });
        }).flatMap(num -> {
            newNumbers[2] = num;
            return Observable.create((ObservableOnSubscribe<long[]>) e -> {
                MagicExplorer.getAllFileByType(context, e::onNext);
            });
        }).compose(RxSchedulersHelper.io())
                .subscribe(nums -> {
                    newNumbers[3] = nums[0];
                    newNumbers[4] = nums[1];
                    newNumbers[5] = nums[2];
                    newNumbers[6] = nums[3];
                    newNumbers[7] = nums[4];
                    newNumbers[8] = nums[5];
                    callback.callListener(newNumbers);
                }, error -> {
                });
    }

    /**
     * 获取所有图片数量
     *
     * @param callback
     */
    public static void getAllPicCount(Context context, Callback<Long> callback) {
        final String[] projectionPhotos = {
                MediaStore.Images.Media.DATA
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = MediaStore.Images.Media.query(
                    context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos
                    , MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " NOT IN ('picture','es','picTemp')"
                    , MediaStore.Images.Media.DATE_TAKEN + " DESC"
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    callback.callListener(Long.parseLong(String.valueOf(cursor.getCount())));
                    cursor.close();
                }, error -> {
                });
    }

    /**
     * 获取所有音频数量
     *
     * @param callback
     */
    public static void getAllVoiceCount(Context context, Callback<Long> callback) {
        final String[] projectionPhotos = {
                MediaStore.Audio.Media.DATA
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos
                    , null, null, null
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    callback.callListener(Long.parseLong(String.valueOf(cursor.getCount())));
                    cursor.close();
                }, error -> {
                });
    }

    /**
     * 获取所有视频数量
     *
     * @param callback
     */
    public static void getAllVideoCount(Context context, Callback<Long> callback) {
        final String[] projectionPhotos = {
                MediaStore.Video.Media.DATA
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos
                    , null, null, null
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    callback.callListener(Long.parseLong(String.valueOf(cursor.getCount())));
                    cursor.close();
                }, error -> {
                });
    }

    /**
     * 获取所有类型的文件数量
     *
     * @param callback
     */
    public static void getAllFileByType(Context context, Callback<long[]> callback) {
        final String[] projectionPhotos = {
                MediaStore.Files.FileColumns.DATA
        };
        Flowable.create((FlowableOnSubscribe<Cursor>) e -> {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Files.getContentUri(VOLUME_NAME)
                    , projectionPhotos
                    , null
                    , null, null
            );
            if (cursor != null) {
                e.onNext(cursor);
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER)
                .subscribe(cursor -> {
                    long[] fileNumbers = new long[6];
                    while (cursor.moveToNext()) {
                        final int pathColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                        final String path = cursor.getString(pathColumn);
                        String exc = FileUtils.getExtensionName(path);
                        switch (FileType.createFileType(exc)) {
                            case FileType.TXT:
                                fileNumbers[0]++;
                                break;
                            case FileType.EXCEL:
                                fileNumbers[1]++;
                                break;
                            case FileType.PPT:
                                fileNumbers[2]++;
                                break;
                            case FileType.WORD:
                                fileNumbers[3]++;
                                break;
                            case FileType.PDF:
                                fileNumbers[4]++;
                                break;
                            case FileType.OTHER:
                                fileNumbers[5]++;
                                break;
                        }
                    }
                    cursor.close();
                    callback.callListener(fileNumbers);
                }, error -> {
                });
    }


    /**
     * 获取本机文件（目录）
     *
     * @param rootPath
     * @param callback
     */
    public static Disposable getFolderAndFileList(String rootPath, Callback<List<MagicFileInfo>> callback) {
        if (TextUtils.isEmpty(rootPath)) {
            rootPath = CacheManager.getSaveFilePath();
        }
        File fileFolder = new File(rootPath);

        List<MagicFileInfo> folderList = new ArrayList<>();
        List<MagicFileInfo> fileList = new ArrayList<>();
        List<MagicFileInfo> retrunList = new ArrayList<>();
        return Flowable.create((FlowableOnSubscribe<String>) e -> {
            for (int i = 0; i < fileFolder.list().length; i++) {
                e.onNext(fileFolder.getPath() + File.separator + fileFolder.list()[i]);
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER)
                .compose(RxSchedulersHelper.fio())
                .flatMap(childFilePath -> {
                    MagicFileInfo info = new MagicFileInfo();
                    if (!childFilePath.contains("nomedia") && !childFilePath.startsWith(".")) {
                        File childFile = new File(childFilePath);
                        String exc = FileUtils.getExtensionName(childFile.getPath());
                        if (!childFile.isHidden()) {
                            info.fileName = childFile.getName();
                            info.path = childFilePath;
                            info.fileSize = childFile.length();
                            info.modifyDate = childFile.lastModified();
                            if (!TextUtils.isEmpty(exc)) {
                                info.position = FileType.createFileType(exc);
                            }
                            info.isFile = !childFile.isDirectory();
                            if (!info.isFile) {
                                info.position = FileType.FOLDER;
                            }
                        }
                    }
                    return Flowable.just(info);
                })
                .compose(RxSchedulersHelper.fio())
                .doOnComplete(() -> {
                    try {
                        Collections.sort(folderList, OrderingConstants.Model_NAME_ORDERING);
                        Collections.sort(fileList, OrderingConstants.Model_NAME_ORDERING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    retrunList.addAll(folderList);
                    retrunList.addAll(fileList);
                    callback.callListener(retrunList);
                })
                .subscribe(info -> {
                    if (!TextUtils.isEmpty(info.path)) {
                        if (info.isFile) {
                            fileList.add(info);
                        } else {
                            folderList.add(info);
                        }
                    }
                }, error -> {
                });

    }

    /**
     * 获取wps文件列表
     *
     * @param callback
     */
    public static Disposable getWPSFileList(String searchKey, Callback1<List<MagicFileInfo>> callback) {
        boolean isHasSearch = !TextUtils.isEmpty(searchKey);
        List<MagicFileInfo> returnList = new ArrayList<>();
        return Flowable.create((FlowableEmitter<String> e) -> {
            File file = new File(WPS_PATH);
            if (file.isFile() && file.exists()) {
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                FileInputStream fileInputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader reader = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                    reader = new BufferedReader(inputStreamReader);
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                } catch (IOException exce) {
                    exce.printStackTrace();
                } finally {
                    try {
                        if (reader != null)
                            reader.close();
                        if (inputStreamReader != null)
                            inputStreamReader.close();
                        if (fileInputStream != null)
                            fileInputStream.close();
                    } catch (IOException exce) {
                        exce.printStackTrace();
                    }
                }
                e.onNext(stringBuffer.toString());
            }
            e.onComplete();

        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.fio())
                .flatMap(json -> {
                    List<WPSUtils.WPSJSONBean> list = new Gson().fromJson(json, new TypeToken<List<WPSUtils.WPSJSONBean>>() {
                    }.getType());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    return Flowable.just(list);
                })
                .compose(RxSchedulersHelper.fio())
                .doOnComplete(() -> {
                    if (isHasSearch) {
                        callback.callComplete();
                    } else {
                        callback.callListener(returnList);
                    }
                })
                .subscribe(list -> {
                    for (int i = 0; i < list.size(); i++) {
                        WPSUtils.WPSJSONBean bean = list.get(i);
                        File itemFile = new File(bean.filePath);
                        if (itemFile.isFile()) {
                            String fileName = FileUtils.getFolderName(bean.filePath);
                            if (isHasSearch && !fileName.contains(searchKey)) {
                                continue;
                            }
                            MagicFileInfo info = new MagicFileInfo();
                            info.fileName = fileName;
                            info.path = bean.filePath;
                            String exc = FileUtils.getExtensionName(bean.filePath);
                            if (!TextUtils.isEmpty(exc)) {
                                info.position = FileType.createFileType(exc);
                            }
                            info.fileSize = itemFile.length();
                            info.isFile = true;
                            info.modifyDate = itemFile.lastModified();

                            if (isHasSearch) {
                                returnList.clear();
                                returnList.add(info);
                                callback.callListener(returnList);
                            } else
                                returnList.add(info);
                        }
                    }
                });
    }


    /**
     * 根据路径获取文件列表
     *
     * @param searchKey 关键字，模糊搜索用（可以为null）
     * @param isStop    是否停止
     * @param callback  回掉接口，泛型：List<MagicFileInfo>
     * @param rootPaths 路径，可以为多个
     * @return Disposable
     */
    public static Disposable getFileList(String searchKey, boolean isStop, Callback1<List<MagicFileInfo>> callback, String... rootPaths) {
        return getFileList(searchKey, isStop, 0, callback, rootPaths);
    }


    /**
     * 根据路径获取文件列表
     *
     * @param searchKey 关键字，模糊搜索用（可以为null）
     * @param timeRange 时间范围 （如果为0，不以此为判断条件）
     * @param callback  回掉接口，泛型：List<MagicFileInfo>
     * @param rootPaths 路径，可以为多个
     * @return Disposable
     */
    public static Disposable getFileList(String searchKey, boolean isStop, long timeRange, Callback1<List<MagicFileInfo>> callback, String... rootPaths) {
        boolean isHasSearch = !TextUtils.isEmpty(searchKey);
        boolean isHasTimeRange = timeRange >= 1000;//范围搜索最少为1秒

        long nowTime = System.currentTimeMillis();
        long lestTime = nowTime - timeRange;

        List<MagicFileInfo> returnList = new ArrayList<>();

        Stack<String> rootStack = new Stack<>();

        for (String rootPath : rootPaths) {//将根目录入栈
            rootStack.push(rootPath);
        }
        return Flowable.create((FlowableOnSubscribe<File>) e -> {
            while (!rootStack.isEmpty() && !isStop) {
                String temp = rootStack.pop();
                File path = new File(temp);
                File[] files = path.listFiles();
                if (null == files)
                    continue;
                for (File f : files) {
                    // 递归监听目录
                    if (isNeedPathName(f.getName()))
                        if (f.isDirectory()) {
                            rootStack.push(f.getAbsolutePath());
                        } else {
                            if (isHasTimeRange) {
                                if (f.lastModified() > lestTime && f.lastModified() < nowTime) {
                                    e.onNext(f);
                                }
                            } else {
                                e.onNext(f);
                            }
                        }
                }
            }
            e.onComplete();
        }, BackpressureStrategy.BUFFER).compose(RxSchedulersHelper.fio())
                .doOnComplete(() -> {
                    if (isHasSearch) {
                        callback.callComplete();
                    } else {
                        callback.callListener(returnList);
                    }
                })
                .subscribe(file -> {
                    String exc = FileUtils.getExtensionName(file.getPath());
                    if (!TextUtils.isEmpty(exc)) {
                        MagicFileInfo info = new MagicFileInfo();
                        info.fileName = file.getName();
                        info.path = file.getPath();
                        info.fileSize = file.length();
                        info.modifyDate = file.lastModified();
                        info.position = FileType.createFileType(exc);

                        if (isHasSearch) {//如果有查询条件
                            if (file.getPath().contains(searchKey)) {//匹配查询
                                returnList.clear();
                                returnList.add(info);
                                callback.callListener(returnList);
                            }
                        } else {
                            returnList.add(info);
                        }
                    }
                }, error -> {
                });
    }

    private static boolean isNeedPathName(String pathName) {
        return !pathName.contains("nomedia") && !pathName.startsWith(".") && !pathName.equals("-1");
    }
}
