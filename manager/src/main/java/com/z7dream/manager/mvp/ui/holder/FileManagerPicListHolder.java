package com.z7dream.manager.mvp.ui.holder;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.z7dream.lib.tool.Utils;
import com.z7dream.manager.R2;
import com.z7dream.manager.mvp.ui.listener.FileManagerListListener;
import com.z7dream.manager.tool.recycler.BaseHolder;
import com.z7dream.manager.widget.TCheckBox;

import butterknife.BindView;

public class FileManagerPicListHolder extends BaseHolder {
    @BindView(R2.id.iv_ifppl)
    public ImageView iv_ifppl;

    @BindView(R2.id.iv_ifppl_collection)
    public ImageView iv_ifppl_collection;

    @BindView(R2.id.rl_ifppl_cb)
    public RelativeLayout rl_ifppl_cb;

    @BindView(R2.id.cb_ifppl_check)
    public TCheckBox cb_ifppl_check;

    @BindView(R2.id.rl_ifppl_check)
    public RelativeLayout rl_ifppl_check;

    public FileManagerPicListHolder(View itemView, FileManagerListListener listener) {
        super(itemView);
        int itemSize = Utils.getScreenWidth(itemView.getContext()) / 3;
        itemView.setLayoutParams(new ViewGroup.LayoutParams(itemSize, itemSize));

        itemView.setOnClickListener(v -> listener.onItemClickListener(getAdapterPosition()));

        rl_ifppl_cb.setOnClickListener(v -> listener.onCheckClickListener(cb_ifppl_check, getAdapterPosition(), cb_ifppl_check.isChecked()));
        rl_ifppl_check.setOnClickListener(v -> listener.onCheckClickListener(cb_ifppl_check, getAdapterPosition(), cb_ifppl_check.isChecked()));
//        cb_ifppl_check.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onCheckClickListener(cb_ifppl_check, getAdapterPosition(), isChecked));
//        rl_ifppl_cb.setOnClickListener(v -> cb_ifppl_check.setChecked(!cb_ifppl_check.isChecked()));

        iv_ifppl.setOnClickListener(v -> listener.onItemClickListener(getAdapterPosition()));
    }
}
