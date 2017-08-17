package com.z7dream.android_filemanager.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.z7dream.android_filemanager.base.mvp.BaseAppli;

/**
 * Created by Z7Dream on 2017/7/26 11:26.
 * Email:zhangxyfs@126.com
 */

public class SPreference {
    private static String USER_SETTING = "user_setting";

    private static SharedPreferences getBase() {
        return BaseAppli.getContext().getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getBase(@NonNull String key) {
        return BaseAppli.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public static void putString(@NonNull String key, @NonNull String value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(@NonNull String key) {
        return getBase().getString(key, null);
    }

    public static void putInt(@NonNull String key, @NonNull int value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /**
     * 默认值为-1
     *
     * @param key
     * @return
     */
    public static int getInt(@NonNull String key) {
        return getBase().getInt(key, -1);
    }

    public static void putBoolean(@NonNull String key, @NonNull boolean value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(@NonNull String key) {
        return getBase().getBoolean(key, false);
    }

    public static void putLong(@NonNull String key, @NonNull long value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static long getLong(@NonNull String key) {
        return getBase().getLong(key, 0L);
    }
}
