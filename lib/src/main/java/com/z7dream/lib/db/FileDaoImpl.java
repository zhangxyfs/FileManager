package com.z7dream.lib.db;


import com.z7dream.lib.callback.Callback;
import com.z7dream.lib.db.bean.FileInfo;
import com.z7dream.lib.model.MagicPicEntity1;
import com.z7dream.lib.tool.EnumFileType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Z7Dream on 2017/7/5 16:22.
 * Email:zhangxyfs@126.com
 */

public interface FileDaoImpl {
    /**
     * 添加文件类型
     *
     * @param typeName 文件类型名字
     * @param format   格式
     */
    void addFileTypeInfo(String typeName, String format);

    /**
     * 文件入库操作
     *
     * @param folderSize 应该的最小文件数
     */
    void toPutFileInStorage(int folderSize);

    /**
     * 是否入库完成
     *
     * @return
     */
    boolean isPutFileInStorageSucc();

    /**
     * 根据文件路径查询文件信息
     *
     * @param filePath 文件路径
     * @return 文件信息
     */
    FileInfo findFileInfoByPath(String filePath);

    /**
     * 添加文件信息
     *
     * @param f 文件
     */
    void addFileInfo(File f);

    /**
     * 删除文件信息
     *
     * @param filePath 文件路径
     */
    void removeFileInfo(String filePath);

    /**
     * 更新文件信息
     *
     * @param f 文件
     */
    void updateFileInfo(File f);

    /**
     * 获取文件类型
     *
     * @param exc 文件扩展名
     * @return
     */
    String getFileType(String exc);

    /**
     * 是否为需要的文件
     *
     * @param exc 文件扩展名
     * @return
     */
    boolean isNeedFile(String exc);

    /**
     * 根据文件类型查找
     *
     * @param enumFileType 文件类型枚举
     * @return
     */
    List<FileInfo> getFileInfoList(EnumFileType enumFileType);


    /**
     * 根据文件类型查找
     *
     * @param enumFileType 文件类型枚举
     * @param likeStr  模糊查询
     * @return
     */
    List<FileInfo> getFileInfoList(EnumFileType enumFileType, String likeStr);

    /**
     * 获取qq文件列表
     *
     * @param enumFileType 文件类型枚举
     * @return
     */
    List<FileInfo> getQQFileInfoList(EnumFileType enumFileType);

    /**
     * 获取微信文件列表
     *
     * @param enumFileType 文件类型枚举
     * @return
     */
    List<FileInfo> getWXFileInfoList(EnumFileType enumFileType);

    /**
     * 获取最近30天文件列表
     *
     * @param enumFileType 文件类型枚举
     * @return
     */
    List<FileInfo> get30DaysFileInfoList(EnumFileType enumFileType);

    /**
     * 获取图片文件夹列表
     *
     * @param callback
     */
    void getFilePicFolderList(Callback<ArrayList<MagicPicEntity1>> callback);

    void destory();
}
