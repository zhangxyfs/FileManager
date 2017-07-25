package com.z7dream.manager.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.z7dream.manager.tool.recycler.BaseHolder;
import com.z7dream.manager.R2;
import com.z7dream.manager.mvp.ui.listener.FileBaseListListener;

import butterknife.BindView;

public class FileBaseParentHolder extends BaseHolder {
    @BindView(R2.id.iv_ifbp_photo)
    public ImageView iv_ifbp_photo;

    @BindView(R2.id.tv_ifbp_title)
    public TextView tv_ifbp_title;

    @BindView(R2.id.iv_ifbp_arrow)
    public ImageView iv_ifbp_arrow;

    @BindView(R2.id.v_ifbp_line)
    public View v_ifbp_line;

    @BindView(R2.id.v_ifbp_line1)
    public View v_ifbp_line1;

    public FileBaseParentHolder(View itemView, FileBaseListListener listener) {
        super(itemView);
        itemView.setOnClickListener(v -> listener.onParentClickListener(getAdapterPosition(), iv_ifbp_arrow, v_ifbp_line1));
    }
}
