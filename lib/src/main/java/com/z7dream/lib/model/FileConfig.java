package com.z7dream.lib.model;

/**
 * Created by Z7Dream on 2017/7/27 9:40.
 * Email:zhangxyfs@126.com
 */

public class FileConfig {
    /**
     * 用于区别当前登录用户
     */
    public String userToken;

    /**
     * 文件管理器入口标题
     */
    public String fileBaseTitle;

    /**
     * 搜索按钮是否显示在toolbar上
     */
    public boolean isToolbarSearch;

    /**
     * 是否显示搜索按钮
     */
    public boolean isVisableSearch;

    /**
     * 是否监听某种类型的文件（可自定义）
     */
    public NeedToListener needToListener;


    public FileConfig() {
        userToken = "";
        fileBaseTitle = "我的文件";
    }

    public interface NeedToListener {
        boolean isNeedToListener(String filePath);
    }
}
