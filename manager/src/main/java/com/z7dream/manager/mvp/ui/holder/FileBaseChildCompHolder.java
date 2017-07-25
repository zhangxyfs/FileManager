package com.z7dream.manager.mvp.ui.holder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.z7dream.lib.tool.Utils;
import com.z7dream.lib.tool.recycler.BaseHolder;
import com.z7dream.manager.R2;
import com.z7dream.manager.mvp.ui.listener.FileBaseListListener;

import butterknife.BindView;

public class FileBaseChildCompHolder extends BaseHolder {
    @BindView(R2.id.iv_ifbcc_photo)
    public ImageView iv_ifbcc_photo;

    @BindView(R2.id.tv_ifbcc_title)
    public TextView tv_ifbcc_title;

    @BindView(R2.id.iv_ifbcc_arrow)
    public ImageView iv_ifbcc_arrow;

    public FileBaseChildCompHolder(View itemView, FileBaseListListener listener) {
        super(itemView);
        photoSize = Utils.convertDipOrPx(itemView.getContext(), 35);
        itemView.setOnClickListener(v -> listener.onChildCompanyClickListener(getAdapterPosition(), iv_ifbcc_arrow));
    }
}
