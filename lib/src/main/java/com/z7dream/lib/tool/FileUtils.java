package com.z7dream.lib.tool;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.z7dream.lib.model.FileConfig;
import com.z7dream.lib.service.FileUpdatingService;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static com.z7dream.lib.tool.MagicExplorer.HW_SCREEN_SAVER_PATH;
import static com.z7dream.lib.tool.MagicExplorer.SCREENSHOTS_PATH;
import static com.z7dream.lib.tool.MagicExplorer.SYS_CAMERA_PATH;

/**
 * Created by Z7Dream on 2017/3/7 9:57.
 * Email:zhangxyfs@126.com
 */

public class FileUtils {
    /**
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1).trim();
            }
        }
        return "";
    }

    /**
     * 获取文件夹名
     *
     * @param path
     * @return
     */
    public static String getFolderName(String path) {
        if (!TextUtils.isEmpty(path)) {
            String[] paths = path.split("\\/");
            if (paths.length > 0)
                return paths[paths.length - 1];
        }
        return path;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path, Map<String, String> notDeleteMap) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory() && !temp.getPath().contains(".nomedia") && notDeleteMap.get(temp.getPath()) == null) {
                delAllFile(path + "/" + tempList[i], notDeleteMap);//先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    public static int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);

        res[0] = options.outWidth;
        res[1] = options.outHeight;
        return res;
    }

    public static String getPathByUri(Context context, Uri data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getPathByUri4BeforeKitkat(context, data);
        } else {
            return getPathByUri4AfterKitkat(context, data);
        }
    }

    //4.4以前通过Uri获取路径：data是Uri，filename是一个String的字符串，用来保存路径
    public static String getPathByUri4BeforeKitkat(Context context, Uri data) {
        String filename = null;
        if (data.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(data, new String[]{"_data"}, null, null, null);
            if (cursor.moveToFirst()) {
                filename = cursor.getString(0);
            }
        } else if (data.getScheme().toString().compareTo("file") == 0) {// file:///开头的uri
            filename = data.toString().replace("file://", "");// 替换file://
            if (!filename.startsWith("/mnt")) {// 加上"/mnt"头
                filename += "/mnt";
            }
        }
        return filename;
    }

    //4.4以后根据Uri获取路径：
    @SuppressLint("NewApi")
    public static String getPathByUri4AfterKitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void saveBitmap(Bitmap bitmap, String savePath) {
        File file = new File(savePath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描指定文件
     *
     * @param filePath
     */
    public static void scanFileAsync(Context context, String filePath) {
        MediaScannerConnection.scanFile(context, new String[]{filePath}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                Log.e("tag", "onMediaScannerConnected");
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.e("tag", path);
            }
        });
    }


    /**
     * 扫描指定目录
     *
     * @param dir
     */
    public static void scanDirAsync(Context context, String dir) {
        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
        scanIntent.setData(Uri.fromFile(new File(dir)));
        context.sendBroadcast(scanIntent);
    }

    /**
     * 重命名文件
     */
    public static boolean renameFile(String filePath, String newPath) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(newPath))
            return false;

        File file = new File(filePath);
        if (file != null) {
            return file.renameTo(new File(newPath));
        }

        return false;
    }

    /**
     * 写文件
     *
     * @param filePath
     * @param str
     */
    public static void writeFile(String filePath, String str) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {
            fileWriter = new FileWriter(filePath);
            printWriter = new PrintWriter(fileWriter);
            printWriter.write(str);
            printWriter.println();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
                if (printWriter != null)
                    printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(File file) {
        BufferedReader reader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                stringBuffer.append(tempString);
            }
            reader.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return stringBuffer.toString();
    }

    public static boolean isNeedToListener(File f) {
        if (f == null) return false;
        if (f.getName().lastIndexOf('.') == -1 && f.isFile()) return false;
        return isNeedToListener(f.getAbsolutePath());
    }

    public static boolean isNeedToListener(String path) {
        if (TextUtils.isEmpty(path)) return false;
        //以下返回必须为false
        String START_PATH = CacheManager.getStartPath();

        String fileName = FileUtils.getFolderName(path);
        boolean isHidden = fileName.startsWith("_") || fileName.startsWith(".") || fileName.endsWith(".0") || fileName.endsWith(".1") || fileName.endsWith(".2");
        boolean isSystem = path.startsWith(START_PATH + "Android") || path.startsWith(START_PATH + "backup") || path.startsWith(START_PATH + "backups") || path.startsWith(START_PATH + "CloudDrive")
                || path.startsWith(START_PATH + "huawei") || path.startsWith(START_PATH + "HuaweiBackup") || path.startsWith(START_PATH + "HWThemes") || path.startsWith(START_PATH + "msc") || path.startsWith(START_PATH + "Musiclrc");
        boolean isRxCache = path.startsWith(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "rxCache");
        boolean isSmiley = path.startsWith(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "smiley");
        boolean isGlide = path.startsWith(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "glide");
        boolean isOSS = path.startsWith(START_PATH + "com.eblog" + File.separator + "cache" + File.separator + "oss_record");
        boolean isMoji = path.startsWith(START_PATH + "moji");

        //以下返回必须为true
        boolean isQQ = path.contains(File.separator + "tencent" + File.separator + "QQ_Images") || path.contains(File.separator + "tencent" + File.separator + "QQfile_recv");
        boolean isWX = path.contains(File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "WeiXin") || path.contains(File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "Download");
        boolean isSysPic = path.equals(SYS_CAMERA_PATH) || path.equals(SCREENSHOTS_PATH) || path.equals(HW_SCREEN_SAVER_PATH);

        //以下部分返回为true
        boolean isTencentPath = path.contains("tencent" + File.separator);
        boolean isWXRoot = path.endsWith(File.separator + "tencent" + File.separator + "MicroMsg");

        if (isHidden || isSystem || isMoji || isRxCache || isSmiley || isGlide || isOSS)
            return false;
        else if (isTencentPath)
            return isQQ || isWXRoot || isWX;
        else if (isSysPic)
            return true;
        else
            return true;
    }

    public static boolean isCouldToListener(File file) {
        FileConfig.NeedToListener listener = FileUpdatingService.getConfigCallback().getConfig().needToListener;
        if (listener == null) {
            return isNeedToListener(file);
        } else {
            return listener.isNeedToListener(file.getAbsolutePath());
        }
    }
}
