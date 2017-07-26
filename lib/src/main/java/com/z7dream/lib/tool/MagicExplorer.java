package com.z7dream.lib.tool;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.z7dream.lib.callback.Callback;
import com.z7dream.lib.tool.rx.RxSchedulersHelper;

import java.io.File;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

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
                MagicExplorer.getAllVoiceCount(context,e::onNext);
            });
        }).flatMap(num -> {
            newNumbers[1] = num;
            return Observable.create((ObservableOnSubscribe<Long>) e -> {
                MagicExplorer.getAllVideoCount(context,e::onNext);
            });
        }).flatMap(num -> {
            newNumbers[2] = num;
            return Observable.create((ObservableOnSubscribe<long[]>) e -> {
                MagicExplorer.getAllFileByType(context,e::onNext);
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
                            case FileType.PDF:
                                fileNumbers[3]++;
                                break;
                            case FileType.WORD:
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
}
