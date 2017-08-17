package com.z7dream.android_filemanager.mvp.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.z7dream.android_filemanager_lib.tool.Utils;
import com.z7dream.android_filemanager.R;
import com.z7dream.android_filemanager.mvp.ui.holder.FileBaseChildCompHolder;
import com.z7dream.android_filemanager.mvp.ui.holder.FileBaseChildCompItemHolder;
import com.z7dream.android_filemanager.mvp.ui.holder.FileBaseChildTypeHolder;
import com.z7dream.android_filemanager.mvp.ui.holder.FileBaseParentHolder;
import com.z7dream.android_filemanager.mvp.ui.listener.FileBaseListListener;
import com.z7dream.android_filemanager.mvp.ui.model.FileBaseListModel;
import com.z7dream.android_filemanager.tool.recycler.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBaseListAdapter extends BaseAdapter<FileBaseListModel, FileBaseListListener, RecyclerView.ViewHolder> {
    private Map<Integer, List<FileBaseListModel>> companyChildMap;
    private RequestOptions requestOptions;

    public FileBaseListAdapter(FileBaseListListener listener) {
        super(listener);
        companyChildMap = new HashMap<>();
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_default_company).error(R.drawable.ic_default_company);
    }

    public void setChildData(Map<Integer, List<FileBaseListModel>> map) {
        companyChildMap = map;
    }

    /**
     * 展开 公司item
     *
     * @param position
     */
    public void expandCompanyItem(int position) {
        if (position < 0 || position > list.size() - 1)
            return;
        FileBaseListModel model = list.get(position);
        if (model.parentPos == 5 && !model.isExpand && model.couldExpand) {//如果是父节点并且是收缩并且可以展开状态那么是可以展开的。
            List<FileBaseListModel> childItemList = companyChildMap.get(model.realPos);
            list.addAll(position + 1, childItemList);
            model.isExpand = true;
            notifyItemRangeInserted(position + 1, childItemList.size());
        }
    }

    /**
     * 收回  公司item
     *
     * @param position
     */
    public void collapseCompanyItem(int position) {
        if (position < 0 || position > list.size() - 1)
            return;
        FileBaseListModel model = list.get(position);
        if (model.parentPos == 5 && model.isExpand) {//如果是父节点并且是展开状态那么是可以收缩的。
            List<FileBaseListModel> childItemList = companyChildMap.get(model.realPos);
            list.removeAll(childItemList);
            model.isExpand = false;
            notifyItemRangeRemoved(position + 1, childItemList.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FileBaseListModel.PARENT || viewType == FileBaseListModel.PARENT_COMP) {
            return new FileBaseParentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filebase_parent, null), listener);
        } else if (viewType == FileBaseListModel.CHILD_TYPE) {
            return new FileBaseChildTypeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filebase_child_type, null), listener);
        } else if (viewType == FileBaseListModel.CHILD_COMP) {
            return new FileBaseChildCompHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filebase_child_comp, null), listener);
        } else {
            return new FileBaseChildCompItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filebase_child_comp_item, null), listener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FileBaseListModel model = list.get(position);
        if (model.type == FileBaseListModel.PARENT || model.type == FileBaseListModel.PARENT_COMP) {
            FileBaseParentHolder fh = (FileBaseParentHolder) holder;
            fh.iv_ifbp_photo.setImageResource(model.icoResId);
            fh.tv_ifbp_title.setText(model.titleName);
            if (model.couldExpand) {
                Utils.expandOCollapseAnim(model.isExpand, fh.iv_ifbp_arrow);
            } else {
                Utils.expandOCollapseAnim(model.isExpand, fh.iv_ifbp_arrow, 0);
            }
            if (position == 0) {
                fh.v_ifbp_line1.setVisibility(model.isExpand ? View.VISIBLE : View.GONE);
            }

        } else if (model.type == FileBaseListModel.CHILD_TYPE) {
            FileBaseChildTypeHolder fh = (FileBaseChildTypeHolder) holder;
            String[] numbersStr = new Gson().fromJson(model.dataJSON, new TypeToken<String[]>() {
            }.getType());
            fh.tv_ifbct_pic_num.setText(numbersStr[0]);
            fh.tv_ifbct_voice_num.setText(numbersStr[1]);
            fh.tv_ifbct_video_num.setText(numbersStr[2]);
            fh.tv_ifbct_txt_num.setText(numbersStr[3]);
            fh.tv_ifbct_excel_num.setText(numbersStr[4]);
            fh.tv_ifbct_ppt_num.setText(numbersStr[5]);
            fh.tv_ifbct_word_num.setText(numbersStr[6]);
            fh.tv_ifbct_pdf_num.setText(numbersStr[7]);
            fh.tv_ifbct_other_num.setText(numbersStr[8]);

        } else if (model.type == FileBaseListModel.CHILD_COMP) {
            FileBaseChildCompHolder fh = (FileBaseChildCompHolder) holder;
            requestOptions.override(fh.photoSize, fh.photoSize);
            Glide.with(fh.context).load(model.icoUrl).apply(requestOptions).into(fh.iv_ifbcc_photo);
            fh.tv_ifbcc_title.setText(model.titleName);

        } else if (model.type == FileBaseListModel.CHILD_COMP_ITEM) {
            FileBaseChildCompItemHolder fh = (FileBaseChildCompItemHolder) holder;
            fh.tv_ifbcci_title.setText(model.titleName);
        }
    }
}
