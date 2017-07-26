package com.z7dream.manager.mvp.ui.listener;


import android.view.View;

import com.z7dream.manager.tool.recycler.OnBaseListener;

public interface FileManagerListListener extends OnBaseListener {
    void onItemClickListener(int position);

    void onCheckClickListener(View cb, int position, boolean isCheck);
}
