package com.z7dream.manager.mvp.ui.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.z7dream.lib.tool.Utils;
import com.z7dream.lib.tool.recycler.BaseHolder;
import com.z7dream.manager.R2;
import com.z7dream.manager.mvp.ui.listener.FileBaseListListener;

import butterknife.BindView;

public class FileBaseChildTypeHolder extends BaseHolder {
    @BindView(R2.id.tr_ifbct_1)
    public TableRow tr_ifbct_1;

    @BindView(R2.id.ll_ifbct_pic)
    public LinearLayout ll_ifbct_pic;

    @BindView(R2.id.tv_ifbct_pic_num)
    public TextView tv_ifbct_pic_num;

    @BindView(R2.id.ll_ifbct_voice)
    public LinearLayout ll_ifbct_voice;

    @BindView(R2.id.tv_ifbct_voice_num)
    public TextView tv_ifbct_voice_num;

    @BindView(R2.id.ll_ifbct_video)
    public LinearLayout ll_ifbct_video;

    @BindView(R2.id.tv_ifbct_video_num)
    public TextView tv_ifbct_video_num;

    @BindView(R2.id.tr_ifbct_2)
    public TableRow tr_ifbct_2;

    @BindView(R2.id.ll_ifbct_txt)
    public LinearLayout ll_ifbct_txt;

    @BindView(R2.id.tv_ifbct_txt_num)
    public TextView tv_ifbct_txt_num;

    @BindView(R2.id.ll_ifbct_excel)
    public LinearLayout ll_ifbct_excel;

    @BindView(R2.id.tv_ifbct_excel_num)
    public TextView tv_ifbct_excel_num;

    @BindView(R2.id.ll_ifbct_ppt)
    public LinearLayout ll_ifbct_ppt;

    @BindView(R2.id.tv_ifbct_ppt_num)
    public TextView tv_ifbct_ppt_num;

    @BindView(R2.id.tr_ifbct_3)
    public TableRow tr_ifbct_3;

    @BindView(R2.id.ll_ifbct_word)
    public LinearLayout ll_ifbct_word;

    @BindView(R2.id.tv_ifbct_word_num)
    public TextView tv_ifbct_word_num;

    @BindView(R2.id.ll_ifbct_pdf)
    public LinearLayout ll_ifbct_pdf;

    @BindView(R2.id.tv_ifbct_pdf_num)
    public TextView tv_ifbct_pdf_num;

    @BindView(R2.id.ll_ifbct_other)
    public LinearLayout ll_ifbct_other;

    @BindView(R2.id.tv_ifbct_other_num)
    public TextView tv_ifbct_other_num;

    public FileBaseChildTypeHolder(View itemView, FileBaseListListener listener) {
        super(itemView);
        int partSize = Utils.getScreenWidth(itemView.getContext()) / 3;

        ll_ifbct_pic.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_voice.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_video.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_txt.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_excel.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_ppt.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_word.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_pdf.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));
        ll_ifbct_other.setOnClickListener(v -> listener.onChildTypeClickListener(v.getId()));

        ll_ifbct_pic.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));
        ll_ifbct_voice.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));
        ll_ifbct_video.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));

        ll_ifbct_txt.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));
        ll_ifbct_excel.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));
        ll_ifbct_ppt.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));

        ll_ifbct_word.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));
        ll_ifbct_pdf.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));
        ll_ifbct_other.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, partSize));
    }
}
