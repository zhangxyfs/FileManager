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


    public FileConfig() {
        userToken = "";
        fileBaseTitle = "我的文件";
    }
}
