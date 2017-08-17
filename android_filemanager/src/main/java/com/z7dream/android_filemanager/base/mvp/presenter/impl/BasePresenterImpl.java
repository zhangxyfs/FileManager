package com.z7dream.android_filemanager.base.mvp.presenter.impl;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.z7dream.android_filemanager.base.mvp.view.BaseView;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by user on 2016/11/4.
 */

public abstract class BasePresenterImpl<V extends BaseView> {
    private V view;
    private Context mContext;
    private Toast toast = null;
    private long oneTime;
    private String oldMsg;

    private WeakReference<CompositeDisposable> mCompositeDisposable;

    public BasePresenterImpl(@NonNull Context context, @NonNull V view) {
        this.view = view;
        this.mContext = context;
        mCompositeDisposable = new WeakReference<>(new CompositeDisposable());
    }

    protected V getView() {
        return view;
    }

    protected Context getContext() {
        return mContext;
    }

    public void detachView() {
        this.view = null;
        onUnsubscribe();
    }

    //订阅
    protected void addSubscription(Disposable disposable) {
        if (mCompositeDisposable == null || mCompositeDisposable.get() == null) {
            mCompositeDisposable = new WeakReference<>(new CompositeDisposable());
        }
        mCompositeDisposable.get().add(disposable);
    }

    //RXjava取消注册，以避免内存泄露
    private void onUnsubscribe() {
        if (mCompositeDisposable != null && mCompositeDisposable.get() != null && !mCompositeDisposable.get().isDisposed()) {
            mCompositeDisposable.get().dispose();
            mCompositeDisposable.clear();
        }
        mCompositeDisposable = null;
        mContext = null;
    }

    protected void showToast(int resId) {
        if (getContext() == null) {
            return;
        }
        String s = getContext().getResources().getString(resId);
        showToast(s);
    }

    protected void showToast(String s) {
        if (s == null || TextUtils.isEmpty(s) || getContext() == null)
            return;
        if (s.contains("failed to connect to") || s.contains("502") || s.contains("404") || s.contains("failed to connect to"))
            s = "网络不给力，请重新尝试";
        if (toast == null) {
            toast = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            long twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
            oneTime = twoTime;
        }
    }

}
