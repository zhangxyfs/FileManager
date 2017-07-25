package com.z7dream.lib.model;

/**
 * Created by Z7Dream on 2017/2/13 17:23.
 * Email:zhangxyfs@126.com
 */

public class Extension {
    public static final String[] PIC = {"jpg", "jpeg", "jpe", "bmp", "png"};
    public static final String[] TXT = {"txt"};
    public static final String[] EXCEL = {"xls", "xlt", "xlm", "xlsx"};
    public static final String[] PPT = {"dps", "dpt", "ppt", "pot", "pps", "pptx"};
    public static final String[] WORD = {"wps", "wpt", "doc", "dot", "rtf", "docx", "dotx"};
    public static final String[] PDF = {"pdf"};

    public static final String[] AUDIO = {"aac", "mp3", "mid", "wav", "flac", "amr", "m4a", "xmf", "ogg"};
    public static final String[] VIDEO = {"3gp", "mp4", "mkv", "ts", "rmvb"};

    public static final String[] ZIP = {"rar", "zip", "7z", "z", "iso", "gz", "tar", "cab", "ace", "apk"};


    public static void addLike(String which, StringBuilder sb, String... strs) {
        for (int i = 0; i < strs.length; i++) {
            sb.append(which);
            sb.append(" LIKE ");
            sb.append("'%.").append(strs[i]).append("'");
            sb.append(" OR ");
        }
        sb.delete(sb.length() - 4, sb.length());
    }

    public static void addNotLike(String which, StringBuilder sb, String... strs) {
        for (int i = 0; i < strs.length; i++) {
            sb.append(which);
            sb.append(" NOT LIKE ");
            sb.append("'%.").append(strs[i]).append("'");
            sb.append(" AND ");
        }
        sb.delete(sb.length() - 4, sb.length());
    }
}
