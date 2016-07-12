package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;

import com.wentongwang.notebook.view.BaseView;

/**
 * Created by Wentong WANG on 2016/7/4.
 */
public interface CreatDiaryView extends BaseView{
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

    boolean isLocked();

    void setLock(boolean lock);

    /**
     * 更换锁的图片
     */
    void updateBtnImg();
}
