package com.z7dream.android_filemanager_lib.tool.collator;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Z7Dream on 2017/3/31 14:53.
 * Email:zhangxyfs@126.com
 */

public abstract class Ordering<T> implements Comparator<T> {
    @Override
    public abstract int compare(@NonNull T left, @NonNull T right);
}
