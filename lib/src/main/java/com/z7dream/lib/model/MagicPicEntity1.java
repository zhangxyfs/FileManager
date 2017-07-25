package com.z7dream.lib.model;

import java.util.ArrayList;

/**
 * Created by Z7Dream on 2017/5/22 11:19.
 * Email:zhangxyfs@126.com
 */

public class MagicPicEntity1 {
    public String name;
    public String path;

    public String icon;
    public int childNum;

    public boolean isNull(){
        return name == null || path == null;
    }

    public ArrayList<MagicFileEntity> childList;
}
