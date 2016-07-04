package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;

/**
 * Created by Wentong WANG on 2016/7/4.
 */
public interface CreatDiaryView {
    /**
     * 获取当前界面的Context
     * @return context
     */
    Context getMyContext();

    /**
     * 获取日记内容
     * @return
     */
    String getDiaryContent();

    /**
     * 获取日记标题
     * @return
     */
    String getDiaryTitle();

    /**
     * 返回
     */
    void goBack();

    /**
     * 显示进度条
     */
    void showPorgressBar();

    /**
     * 隐藏进度条
     */
    void hidePorgressBar();

    boolean isLocked();

    void setLock(boolean lock);

    /**
     * 更换锁的图片
     */
    void updateBtnImg();
}
