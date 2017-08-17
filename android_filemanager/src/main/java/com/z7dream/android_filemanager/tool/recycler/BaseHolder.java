package com.z7dream.android_filemanager.tool.recycler;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.z7dream.android_filemanager_lib.tool.Utils;

import butterknife.ButterKnife;

/**
 * Created by xiaoyu.zhang on 2016/6/28.
 */

public abstract class BaseHolder extends RecyclerView.ViewHolder {

    public Context context;
    public int screenWidth;
    public int photoSize;

    public BaseHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        screenWidth = Utils.getScreenWidth(context);
        ButterKnife.bind(this, itemView);
    }
}
