package com.z7dream.lib.tool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.z7dream.lib.callback.Callback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;

/**
 * Created by ziyouxingdong on 2017/5/17.
 */

public class OpenFileUtils {
    public static void openFile(Context context, String filePath) {
        Intent intent = getFileIntent(context, filePath);
        if (intent != null)
            context.startActivity(intent);
        else Toast.makeText(context, "没有可以打开文件的软件！", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param filePath
     * @return
     */
    private static Intent getFileIntent(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        // XXX Sigi edited
        /* 取得扩展名 */
        String end = FileUtils.getExtensionName(filePath).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (FileType.isAudio(end)) {
            return getAudioFileIntent(context, filePath);
        } else if (FileType.isVideo(end)) {
            return getVideoFileIntent(context, filePath);
        } else if (FileType.isPic(end) || end.equals("gif")) {
            return getImageFileIntent(context, filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(context, filePath);
        } else if (end.equals("ppt") || (end.equals("pptx") || (end.endsWith(".zip")) || (end.endsWith(".rar")))) {
            return getAllIntent(context, filePath);
        } else if (end.equals("xls") || (end.equals("xlsx"))) {
            return getAllIntent(context, filePath);
        } else if (end.equals("doc") || (end.equals("docx"))) {
            return getAllIntent(context, filePath);
        } else if (end.equals("pdf")) {
            return getAllIntent(context, filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(context, filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(context, filePath, false);
        } else {
            return getAllIntent(context, filePath);
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(Context context, String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(Context context, String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }


    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(Context context, String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri;

        if (paramBoolean) {
            uri = Uri.parse(param);
        } else {
            uri = FileProvider7.getUriForFile(context, new File(param));
        }
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static void copyFile(File sourceFile, File targetFile) {
        try {
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);

            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        } catch (Exception e) {
        }
    }

    public static void openFile(Context context, String filePath, Callback<String> callback) {
        String finishExc = FileUtils.getExtensionName(filePath);
        int type = FileType.createFileType(finishExc);
        switch (type) {
            case FileType.TXT:
            case FileType.EXCEL:
            case FileType.PPT:
            case FileType.WORD:
            case FileType.PDF:
                WPSUtils.openWpsFile(context, filePath);
                break;
            case FileType.AUDIO:
                callback.callListener(filePath);
                break;
            default:
                openFile(context, filePath);
                break;
        }
    }
}
