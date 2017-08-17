package com.z7dream.android_filemanager.mvp.ui.listener;


import android.view.View;

import com.z7dream.android_filemanager.tool.recycler.OnBaseListener;

public interface FileManagerListListener extends OnBaseListener {
    void onItemClickListener(int position);

    void onCheckClickListener(View cb, int position, boolean isCheck);
}
