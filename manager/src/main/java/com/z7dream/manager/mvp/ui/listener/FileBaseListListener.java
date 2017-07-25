package com.z7dream.manager.mvp.ui.listener;


import android.view.View;
import android.widget.ImageView;

import com.z7dream.lib.tool.recycler.OnBaseListener;

public interface FileBaseListListener extends OnBaseListener {
    void onChildTypeClickListener(int viewId);

    void onParentClickListener(int position, ImageView rightArrow, View line);

    void onChildCompanyClickListener(int position, ImageView rightArrow);

    void onChildCompanyItemClickListener(int position);
}
