package com.z7dream.manager.mvp.ui.adapter;


import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.z7dream.manager.R;
import com.z7dream.manager.mvp.ui.holder.FileManagerOtherListHolder;
import com.z7dream.manager.mvp.ui.holder.FileManagerPicListHolder;
import com.z7dream.manager.mvp.ui.listener.FileManagerListListener;
import com.z7dream.manager.mvp.ui.model.FileManagerListModel;
import com.z7dream.manager.tool.recycler.BaseAdapter;

public class FileManagerListAdapter extends BaseAdapter<FileManagerListModel, FileManagerListListener, RecyclerView.ViewHolder> {
    private boolean openCheck;

    public FileManagerListAdapter(FileManagerListListener listener) {
        super(listener);
    }

    public void changeCheck() {
        openCheck = !openCheck;
        notifyDataSetChanged();
    }

    public void setCheck(boolean b) {
        if (openCheck == b) {
            return;
        }
        openCheck = b;
        notifyDataSetChanged();
    }

    public boolean getCheckStatus() {
        return openCheck;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FileManagerListModel.PIC) {
            return new FileManagerPicListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_pic_path_list, null), listener);
        } else {
            return new FileManagerOtherListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_other_path_list, null), listener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FileManagerListModel model = list.get(position);
        if (model.type == FileManagerListModel.PIC) {
            FileManagerPicListHolder fpplh = (FileManagerPicListHolder) holder;
            Glide.with(fpplh.context).load(model.picPath).into(fpplh.iv_ifppl);
            if (model.isStar) {
                fpplh.iv_ifppl_collection.setVisibility(View.VISIBLE);
            } else {
                fpplh.iv_ifppl_collection.setVisibility(View.GONE);
            }
            if (openCheck) {
                fpplh.rl_ifppl_cb.setVisibility(View.VISIBLE);
            } else {
                fpplh.rl_ifppl_cb.setVisibility(View.GONE);
            }
            fpplh.cb_ifppl_check.setChecked(model.isSelect, true, false);
        } else if (model.type == FileManagerListModel.OTHER) {
            FileManagerOtherListHolder fpolh = (FileManagerOtherListHolder) holder;
            fpolh.iv_ifopl_ico.setImageResource(model.iconResId);
            if (model.isStar) {
                fpolh.iv_ifopl_collect.setVisibility(View.VISIBLE);
            } else {
                fpolh.iv_ifopl_collect.setVisibility(View.GONE);
            }
            if (openCheck && model.isFile) {
//                fpolh.rl_ifopl_check.setVisibility(View.VISIBLE);
                fpolh.cb_ifopl_check.setVisibility(View.VISIBLE);
                fpolh.tv_ifopl_date.setVisibility(View.GONE);
            } else {
//                fpolh.rl_ifopl_check.setVisibility(View.GONE);
                fpolh.cb_ifopl_check.setVisibility(View.GONE);
                fpolh.tv_ifopl_date.setVisibility(View.VISIBLE);
            }
            fpolh.cb_ifopl_check.setChecked(model.isSelect, true, false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fpolh.tv_ifopl_title.setText(Html.fromHtml(model.fileName, Html.FROM_HTML_MODE_LEGACY));
            } else {
                fpolh.tv_ifopl_title.setText(Html.fromHtml(model.fileName));
            }
            fpolh.tv_ifopl_content.setText(model.modifyStr);
            if (model.isFile) {
                fpolh.tv_ifopl_date.setText(model.sizeStr);
            } else {
                fpolh.tv_ifopl_date.setText("");
            }


        }
    }
}
