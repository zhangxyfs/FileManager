package com.z7dream.manager.base.mvp;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.z7dream.lib.R;
import com.z7dream.lib.tool.Utils;
import com.z7dream.lib.tool.WeakHandler;
import com.z7dream.lib.tool.rx.RxBus;
import com.z7dream.manager.base.mvp.presenter.BasePresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO 基础activity 默认开启透明导航条
 * Created by user on 2016/11/4.
 */

public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity {
    private BaseAppli mAppli;//applicaiton
    private WeakHandler mBaseHandler;//handler
    private Unbinder mUnbinder;//用于butterKnife解绑
    private P mPresenter;//功能调用
    private boolean mIsNeedAdapterPhone = true;
    private boolean mIsNeedGoneNavigationBar;

    private long mExitPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        before();
        if (layoutID() > 0)
            setContentView(layoutID());
        after();
        init(savedInstanceState);
        data();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void before() {
        mAppli = (BaseAppli) getApplication();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected abstract int layoutID();

    protected abstract void init(Bundle savedInstanceState);

    protected abstract P createPresenter();

    protected void after() {
        mUnbinder = ButterKnife.bind(this);
        mBaseHandler = new WeakHandler();
        if (mPresenter == null)
            mPresenter = createPresenter();

        if (mIsNeedAdapterPhone && !isNeedAdapterPhone()) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                //透明状态栏
                //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //透明导航栏
                //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        } else {
            android4Adapter();
        }
        if (mIsNeedGoneNavigationBar) {
            toHideNav();
        }
    }

    protected void data() {

    }

    protected void userToolbar(Toolbar toolbar, View.OnClickListener clickListener) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        } else {
            toolbar.setNavigationOnClickListener(clickListener);
        }
    }

    protected void userToolbar(Toolbar toolbar, String title, View.OnClickListener clickListener) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle(title);
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        } else {
            toolbar.setNavigationOnClickListener(clickListener);
        }
    }

    protected void userToolbar(Toolbar toolbar, int resId, View.OnClickListener clickListener) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(resId);
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener(v -> finish());
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void setToolbarTitle(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resId);
        }
    }

    protected void setToolbarTextViewInfo(Toolbar toolbar) {
        Class cls = toolbar.getClass();
        try {
            Field field = cls.getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            TextView tv = (TextView) field.get(toolbar);
            tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected ActionMenuView getToolbarMenuView(Toolbar toolbar) {
        ActionMenuView view = null;
        Class cls = toolbar.getClass();
        try {
            Field field = cls.getDeclaredField("mMenuView");
            field.setAccessible(true);
            view = (ActionMenuView) field.get(toolbar);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 设置是否需要适配手机（必须写在before里，默认为true）
     *
     * @return
     */
    protected void setIsNeedAdapterPhone(boolean b) {
        mIsNeedAdapterPhone = b;
    }

    /**
     * 设置是否需要不显示导航条（必须写子before里，默认为false）
     *
     * @return
     */
    protected void setIsNeedGoneNavigationBar(boolean b) {
        mIsNeedGoneNavigationBar = b;
    }

    /**
     * 获取夜间模式状态
     */
    protected boolean getIsNightTheme() {
        return false;
    }

    /**
     * 是否需要适配手机
     *
     * @return
     */
    protected boolean isNeedAdapterPhone() {
        if (Build.VERSION.SDK_INT > 21 || Build.MODEL.toLowerCase().contains("vivo")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * android5.0以下的适配
     */
    private void android4Adapter() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(getResources().getIdentifier("coordinatorLayout", "id", getPackageName()));
        if (coordinatorLayout != null && coordinatorLayout.getChildCount() > 0) {
            if (coordinatorLayout.getChildAt(0) instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) coordinatorLayout.getChildAt(0);
                if (appBarLayout.getChildCount() > 0 && appBarLayout.getChildAt(0) instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) appBarLayout.getChildAt(0);
                    AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                    lp.setMargins(0, Utils.getTop(this), 0, 0);
                    toolbar.setLayoutParams(lp);
                }
            }
        }
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

    protected void setPresenter() {
        if (mPresenter == null)
            mPresenter = createPresenter();
    }

    /**
     * 获取handler
     *
     * @return
     */
    protected WeakHandler getHandler() {
        return mBaseHandler;
    }


    protected void toHideNav() {
        mBaseHandler.post(mHideRunnable);
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            mBaseHandler.post(mHideRunnable); // hide the navigation bar
        });
    }

    protected void toShowNav() {
        mBaseHandler.removeCallbacks(mHideRunnable);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    protected Runnable mHideRunnable = () -> {
        // must be executed in main thread :)
        getWindow().getDecorView().setSystemUiVisibility(getHideFlags());
    };

    private int getHideFlags() {
        int flags;
        int curApiVersion = Build.VERSION.SDK_INT;
        // This work only for android 4.4+
        if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
            // This work only for android 4.4+
            // hide navigation bar permanently in android activity
            // touch the screen, the navigation bar will not show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

        } else {
            // touch the screen, the navigation bar will show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        return flags;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBaseHandler != null) {
            mBaseHandler.removeCallbacksAndMessages(null);
            mBaseHandler = null;
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    // 判断权限集合
    protected boolean needPermissions(String... permissions) {
        //判断版本是否兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        boolean isNeed;
        for (String permission : permissions) {
            isNeed = needsPermission(permission);
            if (isNeed) {
                return true;
            }
        }
        return false;
    }

    protected List<String> noPermissions(String... permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (needPermissions(permission)) {
                list.add(permission);
            }
        }
        return list;
    }

    // 判断是否缺少权限
    protected boolean needsPermission(String permission) {
        return ContextCompat.checkSelfPermission(getAppli().getContext(), permission) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 打开activity
     *
     * @param pClass
     * @param key
     * @param value
     */
    protected void openActivity(Class<?> pClass, String key, Object value) {
        Bundle bundle = new Bundle();
        if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        }
        openActivity(pClass, bundle);
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
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 双击退出。
     */
    protected boolean exitBy2Click() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mExitPressedTime) > 2000) {//比较两次按键时间差
            Toast.makeText(this, getString(R.string.nav_back_again_finish), Toast.LENGTH_SHORT).show();
            mExitPressedTime = mNowTime;
        } else {
            RxBus.get().destory();
            finish();
            exitExecute();
            return true;
        }
        return false;
    }

    /**
     * 退出时候执行
     */
    protected void exitExecute() {

    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
