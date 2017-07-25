package com.z7dream.manager.base.mvp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.z7dream.lib.base.mvp.presenter.BasePresenter;
import com.z7dream.lib.tool.WeakHandler;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基本Fragment
 * Created by user on 2016/11/4.
 */

public abstract class BaseFragment<P extends BasePresenter> extends RxFragment {
    private BaseAppli mAppli;
    private WeakHandler mBaseHandler;//handler
    private View mFragmentView;
    private Unbinder mUnbinder;//用于butterKnife解绑
    private P mPresenter;//功能调用

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        before();
        if (mFragmentView == null && layoutID() > 0) {
            mFragmentView = inflater.inflate(layoutID(), container, false);
        }
        after(mFragmentView);
        init(mFragmentView, savedInstanceState);
        data();
        return mFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mBaseHandler != null) {
            mBaseHandler.removeCallbacksAndMessages(null);
            mBaseHandler = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void before() {
        mAppli = (BaseAppli) getActivity().getApplication();
    }

    protected void after(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        mBaseHandler = new WeakHandler();
        mPresenter = createPresenter();
    }

    protected abstract int layoutID();

    protected abstract void init(View view, Bundle savedInstanceState);

    protected abstract P createPresenter();

    protected void data() {

    }

    /**
     * 获取application
     *
     * @return
     */
    protected BaseAppli getAppli() {
        return mAppli;
    }

    /**
     * 获取presenter
     *
     * @return
     */
    protected P getPresenter() {
        return mPresenter;
    }

    /**
     * 获取handler
     *
     * @return
     */
    protected WeakHandler getHandler() {
        return mBaseHandler;
    }


    /**
     * 打开activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    protected void userToolbar(Toolbar toolbar, String title, View.OnClickListener clickListener) {
        getCompatActivity().setSupportActionBar(toolbar);
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getCompatActivity().getSupportActionBar().setTitle(title);
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener(v -> getActivity().finish());
        } else {
            toolbar.setNavigationOnClickListener(clickListener);
        }
    }

    protected void closeToolbarBack() {
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    protected void userToolbar(Toolbar toolbar, int resId, View.OnClickListener clickListener) {
        getCompatActivity().setSupportActionBar(toolbar);
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getCompatActivity().getSupportActionBar().setTitle(resId);
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener(v -> getActivity().finish());
        } else {
            toolbar.setNavigationOnClickListener(clickListener);
        }
    }

    protected void userToolbar(Toolbar toolbar, String title) {
        userToolbar(toolbar, title, null);
    }

    protected void userToolbar(Toolbar toolbar, int resId) {
        userToolbar(toolbar, resId, null);
    }

    protected void userToolbar(Toolbar toolbar) {
        userToolbar(toolbar, null, null);
    }

    protected void setToolbarTitle(String title) {
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar().setTitle(title);
        }
    }

    protected void setToolbarTitle(int resId) {
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar().setTitle(resId);
        }
    }

    protected AppCompatActivity getCompatActivity() {
        return (AppCompatActivity) getActivity();
    }
}
