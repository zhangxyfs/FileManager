package com.z7dream.android_filemanager_lib.tool;

import android.text.TextUtils;

import com.z7dream.android_filemanager_lib.R;


/**
 * Created by Z7Dream on 2017/7/5 13:15.
 * Email:zhangxyfs@126.com
 */

public enum EnumFileType {
    PIC,
    TXT,
    EXCEL,
    PPT,
    WORD,
    PDF,
    AUDIO,
    VIDEO,
    ZIP,
    OTHER,
    ALL;

    public static int getOldType(String type) {
        int oldType = FileType.OTHER;
        if (TextUtils.equals(type, EnumFileType.PIC.name())) {
            oldType = FileType.PIC;
        } else if (TextUtils.equals(type, EnumFileType.TXT.name())) {
            oldType = FileType.TXT;
        } else if (TextUtils.equals(type, EnumFileType.EXCEL.name())) {
            oldType = FileType.EXCEL;
        } else if (TextUtils.equals(type, EnumFileType.PPT.name())) {
            oldType = FileType.PPT;
        } else if (TextUtils.equals(type, EnumFileType.WORD.name())) {
            oldType = FileType.WORD;
        } else if (TextUtils.equals(type, EnumFileType.PDF.name())) {
            oldType = FileType.PDF;
        } else if (TextUtils.equals(type, EnumFileType.AUDIO.name())) {
            oldType = FileType.AUDIO;
        } else if (TextUtils.equals(type, EnumFileType.VIDEO.name())) {
            oldType = FileType.VIDEO;
        } else if (TextUtils.equals(type, EnumFileType.ZIP.name())) {
            oldType = FileType.OTHER;
        }
        return oldType;
    }

    public static EnumFileType getType(int oldFileType) {
        EnumFileType enumFileType;
        switch (oldFileType) {
            case FileType.PIC:
                enumFileType = PIC;
                break;
            case FileType.AUDIO:
                enumFileType = AUDIO;
                break;
            case FileType.VIDEO:
                enumFileType = VIDEO;
                break;
            case FileType.TXT:
                enumFileType = TXT;
                break;
            case FileType.EXCEL:
                enumFileType = EXCEL;
                break;
            case FileType.PPT:
                enumFileType = PPT;
                break;
            case FileType.WORD:
                enumFileType = WORD;
                break;
            case FileType.PDF:
                enumFileType = PDF;
                break;
            default:
                enumFileType = OTHER;
                break;
        }
        return enumFileType;
    }

    public static int getCacheWhich(EnumFileType type) {
        int fileType = CacheManager.OTHER;
        if (type == EnumFileType.PIC) {
            fileType = CacheManager.PIC;
        } else if (type == EnumFileType.AUDIO) {
            fileType = CacheManager.VOICE;
        } else if (type == EnumFileType.VIDEO) {
            fileType = CacheManager.VIDEO;
        } else if (type == EnumFileType.TXT) {
            fileType = CacheManager.TXT;
        } else if (type == EnumFileType.EXCEL) {
            fileType = CacheManager.EXCEL;
        } else if (type == EnumFileType.PPT) {
            fileType = CacheManager.PPT;
        } else if (type == EnumFileType.WORD) {
            fileType = CacheManager.WORD;
        } else if (type == EnumFileType.PDF) {
            fileType = CacheManager.PDF;
        }
        return fileType;
    }

    public static EnumFileType getEnum(String string) {
        if (string != null) {
            try {
                return Enum.valueOf(EnumFileType.class, string.trim());
            } catch (IllegalArgumentException ex) {
            }
        }
        return EnumFileType.OTHER;
    }


    public static int createIconResId(String type, boolean isFile) {
        int resId = R.drawable.ic_file_other;
        if (isFile) {
            if (TextUtils.equals(type, EnumFileType.PIC.name())) {
                resId = R.drawable.ic_file_pic;
            } else if (TextUtils.equals(type, EnumFileType.AUDIO.name())) {
                resId = R.drawable.ic_file_audio;
            } else if (TextUtils.equals(type, EnumFileType.VIDEO.name())) {
                resId = R.drawable.ic_file_video;
            } else if (TextUtils.equals(type, EnumFileType.TXT.name())) {
                resId = R.drawable.ic_file_txt;
            } else if (TextUtils.equals(type, EnumFileType.EXCEL.name())) {
                resId = R.drawable.ic_file_excel;
            } else if (TextUtils.equals(type, EnumFileType.PPT.name())) {
                resId = R.drawable.ic_file_ppt;
            } else if (TextUtils.equals(type, EnumFileType.WORD.name())) {
                resId = R.drawable.ic_file_word;
            } else if (TextUtils.equals(type, EnumFileType.PDF.name())) {
                resId = R.drawable.ic_file_pdf;
            }
        } else {
            resId = R.drawable.ic_file_folder;
        }
        return resId;
    }
}
