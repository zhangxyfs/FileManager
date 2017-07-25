package com.z7dream.manager.mvp.ui.holder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.z7dream.manager.tool.recycler.BaseHolder;
import com.z7dream.manager.R2;
import com.z7dream.manager.mvp.ui.listener.FileBaseListListener;

import butterknife.BindView;

public class FileBaseChildCompItemHolder extends BaseHolder {
    @BindView(R2.id.iv_ifbcci_dot)
    public ImageView tv_ifbcci_dot;

    @BindView(R2.id.tv_ifbcci_title)
    public TextView tv_ifbcci_title;

    public FileBaseChildCompItemHolder(View itemView, FileBaseListListener listener) {
        super(itemView);
        itemView.setOnClickListener(v -> listener.onChildCompanyItemClickListener(getAdapterPosition()));
    }
}
