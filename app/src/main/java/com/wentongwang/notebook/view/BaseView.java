package com.wentongwang.notebook.view;

import android.content.Context;

/**
 * Created by Wentong WANG on 2016/7/12.
 */
public interface BaseView {

    /**
     * 获取当前界面的Context
     * @return context
     */
    Context getMyContext();

    /**
     * 显示进度条
     */
    void showPorgressBar();

    /**
     * 隐藏进度条
     */
    void hidePorgressBar();

    /**
     * 返回
     */
    void goBack();
}
