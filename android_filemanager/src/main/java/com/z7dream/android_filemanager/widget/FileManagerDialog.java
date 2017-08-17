package com.z7dream.android_filemanager.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.z7dream.android_filemanager_lib.tool.Utils;
import com.z7dream.android_filemanager.R;


/**
 * Created by duCong on 2017/2/15.
 */

public class FileManagerDialog implements View.OnClickListener {
    private FileDialogClickListener listener;
    private TextView fileDialogSend;
    private TextView file_dialog_forward;
    private TextView file_dialog_download;
    private TextView fileDialogDelete;
    private TextView fileDialogRename;
    private TextView fileDialogStar;
    private PopupWindow window;
    private boolean isStarState;
    private View view;
    private Context mContext;
    private int bottomMargin;

    private OnFileSendListener mOnFileSendListener;
    private OnFileDeleteListener mOnFileDeleteListener;
    private OnFileRenameListener mOnFileRenameListener;
    private OnFileStarListener mOnFileStarListener;
    private OnForwardListener mOnForwardListener;
    private OnDownloadListener mOnDownloadListener;

    private boolean isShow;


    public FileManagerDialog(Context context, int bottomMargin) {
        mContext = context;
        this.bottomMargin = bottomMargin;
        create();
    }

    public FileManagerDialog(Context context, int bottomMargin, FileDialogClickListener listener) {
        mContext = context;
        this.listener = listener;
        this.bottomMargin = bottomMargin;
        create();
    }

    private void create() {
        view = View.inflate(mContext, R.layout.include_file_explorer_dialog, null);
        fileDialogSend = (TextView) view.findViewById(R.id.file_dialog_send);
        file_dialog_forward = (TextView) view.findViewById(R.id.file_dialog_forward);
        file_dialog_download = (TextView) view.findViewById(R.id.file_dialog_download);
        fileDialogDelete = (TextView) view.findViewById(R.id.file_dialog_delete);
        fileDialogRename = (TextView) view.findViewById(R.id.file_dialog_rename);
        fileDialogStar = (TextView) view.findViewById(R.id.file_dialog_star);

        fileDialogSend.setOnClickListener(this);
        fileDialogDelete.setOnClickListener(this);
        fileDialogRename.setOnClickListener(this);
        fileDialogStar.setOnClickListener(this);
        file_dialog_forward.setOnClickListener(this);
        file_dialog_download.setOnClickListener(this);


        window = new PopupWindow(view, -1, Utils.convertDipOrPx(mContext, 70));
    }

    public void createIMFile() {
        fileDialogSend.setVisibility(View.GONE);
        fileDialogRename.setVisibility(View.GONE);
        fileDialogStar.setVisibility(View.GONE);
        file_dialog_forward.setVisibility(View.VISIBLE);
        file_dialog_download.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.file_dialog_send) {
            if (listener != null)
                listener.onFileDialogSendClick(view);
            else if (mOnFileSendListener != null)
                mOnFileSendListener.onFileSendClick(view);

        } else if (view.getId() == R.id.file_dialog_delete) {
            if (listener != null)
                listener.onFileDialogDeleteClick(view);
            else if (mOnFileDeleteListener != null)
                mOnFileDeleteListener.onFileDeleteClick(view);

        } else if (view.getId() == R.id.file_dialog_rename) {
            if (listener != null)
                listener.onFileDialogRenameClick(view);
            else if (mOnFileRenameListener != null)
                mOnFileRenameListener.onFileRenameClick(view);

        } else if (view.getId() == R.id.file_dialog_star) {
            if (listener != null)
                if (isStarState) {
                    listener.onFileDialogStarClick(view);
                } else {
                    listener.onFileDialogCancelStarClick(view);
                }
            else if (mOnFileStarListener != null)
                mOnFileStarListener.onFileStarClick(view, isStarState);

        } else if (view.getId() == R.id.file_dialog_forward) {
            if (mOnForwardListener != null)
                mOnForwardListener.onForwardClick(view);

        } else if (view.getId() == R.id.file_dialog_download) {
            if (mOnDownloadListener != null)
                mOnDownloadListener.onDownloadClick(view);
        }
    }


    public void showPopup() {
        if (view != null && window != null && !window.isShowing()) {
            window.showAtLocation(view, Gravity.BOTTOM, 0, bottomMargin);
            isShow = !isShow;
        }
    }

    public void dismiss() {
        if (view != null && window != null && window.isShowing()) {
            window.dismiss();
            isShow = !isShow;
        }
    }

    public boolean isShow() {
        return view != null && window != null && window.isShowing();
    }

    public void show0dismiss() {
        if (view != null && window != null) {
            if (window.isShowing()) {
                dismiss();
            } else {
                showPopup();
            }
        }
    }

    /**
     * 星标状态
     *
     * @param isStarState true 星标 false 取消星标
     */
    public void setStarState(boolean isStarState) {
        this.isStarState = isStarState;
        if (isStarState) {
            fileDialogStar.setText(R.string.star_file_str);
        } else {
            fileDialogStar.setText(R.string.star_cancel_str);
        }
    }

    /**
     * 文件重命名焦点
     *
     * @param isFocus true 可点击 false 不可点击
     */
    public void setRenameFocus(boolean isFocus) {
        if (isFocus) {
            setViewFocus(fileDialogRename, isFocus, R.drawable.ic_button_rename_true);
        } else {
            setViewFocus(fileDialogRename, isFocus, R.drawable.ic_button_rename_false);
        }
    }

    public void destory() {
        dismiss();
        listener = null;
        mOnFileSendListener = null;
        mOnFileDeleteListener = null;
        mOnFileRenameListener = null;
        mOnFileStarListener = null;
    }

    private void setViewFocus(TextView view, boolean isFocus, int img) {
        int textColor;
        Drawable drawable = view.getContext().getResources().getDrawable(img);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);

        if (isFocus) {
            view.setOnClickListener(this);
            textColor = view.getContext().getResources().getColor(R.color.color_font_high);
        } else {
            view.setOnClickListener(null);
            textColor = view.getContext().getResources().getColor(R.color.color_font_middle);
        }

        view.setTextColor(textColor);
    }

    public void setOnFileSendListener(OnFileSendListener onFileSendListener) {
        mOnFileSendListener = onFileSendListener;
    }

    public void setOnFileDeleteListener(OnFileDeleteListener onFileDeleteListener) {
        mOnFileDeleteListener = onFileDeleteListener;
    }

    public void setOnFileRenameListener(OnFileRenameListener onFileRenameListener) {
        mOnFileRenameListener = onFileRenameListener;
    }

    public void setOnFileStarListener(OnFileStarListener onFileStarListener) {
        mOnFileStarListener = onFileStarListener;
    }

    public void setOnFileForwardListener(OnForwardListener onFileForwardListener) {
        mOnForwardListener = onFileForwardListener;
    }

    public void setOnFileDownloadListener(OnDownloadListener onFileDownloadListener) {
        mOnDownloadListener = onFileDownloadListener;
    }

    public interface OnFileSendListener {
        void onFileSendClick(View view);
    }

    public interface OnFileDeleteListener {
        void onFileDeleteClick(View view);
    }

    public interface OnFileRenameListener {
        void onFileRenameClick(View view);
    }

    public interface OnFileStarListener {
        void onFileStarClick(View view, boolean b);
    }

    public interface OnForwardListener {
        void onForwardClick(View view);
    }

    public interface OnDownloadListener {
        void onDownloadClick(View view);
    }

    public interface FileDialogClickListener {
        //发送
        void onFileDialogSendClick(View view);

        //删除
        void onFileDialogDeleteClick(View view);

        //重命名
        void onFileDialogRenameClick(View view);

        //星标
        void onFileDialogStarClick(View view);

        //取消星标
        void onFileDialogCancelStarClick(View view);
    }
}
