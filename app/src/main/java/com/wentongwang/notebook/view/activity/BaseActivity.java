package com.wentongwang.notebook.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.wentongwang.notebook.managers.MyActivityManager;


/**
 * 基础的Activity
 * Created by Wentong WANG on 2016/6/21.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager.getInstance().addActivity(this);
        this.setContentView(getLayoutId());
        this.initDatas();
        this.initViews();
        this.initEvents();
    }

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     * 初始化View
     */
    protected abstract void initViews();

    /**
     * 初始化View的事件
     */
    protected abstract void initEvents();

    /**
     * 获取布局
     * @return 布局界面的Id
     */
    protected abstract int getLayoutId();

}
