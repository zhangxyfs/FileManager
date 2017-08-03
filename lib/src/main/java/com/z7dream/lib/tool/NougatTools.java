package com.z7dream.lib.tool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * Created by Z7Dream on 2017/8/1 18:11.
 * Email:zhangxyfs@126.com
 */

public class NougatTools {
    /**
     * 将普通uri转化成适应7.0的content://形式  针对文件格式
     *
     * @param context    上下文
     * @param file       文件路径
     * @param intent     intent
     * @param intentType intent.setDataAndType
     * @return
     */
    public static Intent formatFileProviderIntent(
            Context context, File file, Intent intent, String intentType) {

        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        // 表示文件类型
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, intentType);

        return intent;
    }

    /**
     * 将普通uri转化成适应7.0的content://形式  针对图片格式
     *
     * @param context 上下文
     * @param file    文件路径
     * @param intent  intent
     * @return
     */
    public static Intent formatFileProviderPicIntent(
            Context context, File file, Intent intent) {

        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 表示图片类型
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    /**
     * 将普通uri转化成适应7.0的content://形式
     *
     * @return
     */
    public static Uri formatFileProviderUri(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        return uri;
    }
}
