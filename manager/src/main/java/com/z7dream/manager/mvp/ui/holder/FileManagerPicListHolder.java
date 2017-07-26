package com.z7dream.manager.mvp.ui.holder;


import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.z7dream.lib.tool.Utils;
import com.z7dream.manager.R;
import com.z7dream.manager.mvp.ui.listener.FileManagerListListener;
import com.z7dream.manager.tool.recycler.BaseHolder;

import butterknife.BindView;

public class FileManagerPicListHolder extends BaseHolder {
    @BindView(R.id.iv_ifppl)
    public ImageView iv_ifppl;

    @BindView(R.id.iv_ifppl_collection)
    public ImageView iv_ifppl_collection;

    @BindView(R.id.rl_ifppl_cb)
    public RelativeLayout rl_ifppl_cb;

    @BindView(R.id.cb_ifppl_check)
    public CheckBox cb_ifppl_check;

    public FileManagerPicListHolder(View itemView, FileManagerListListener listener) {
        super(itemView);
        int itemSize = Utils.getScreenWidth(itemView.getContext()) / 3;
        itemView.setLayoutParams(new ViewGroup.LayoutParams(itemSize, itemSize));

        itemView.setOnClickListener(v -> listener.onItemClickListener(getAdapterPosition()));


        cb_ifppl_check.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onCheckClickListener(cb_ifppl_check, getAdapterPosition(), isChecked));

        rl_ifppl_cb.setOnClickListener(v -> cb_ifppl_check.setChecked(!cb_ifppl_check.isChecked()));

        iv_ifppl.setOnClickListener(v -> listener.onItemClickListener(getAdapterPosition()));
    }
}
