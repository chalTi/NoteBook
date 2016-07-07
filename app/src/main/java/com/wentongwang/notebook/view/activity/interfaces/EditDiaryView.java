package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;

import com.wentongwang.notebook.model.DiaryItem;

/**
 * Created by Wentong WANG on 2016/7/5.
 */
public interface EditDiaryView {
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
     * 完成任务后返回
     */
    void finishAndBack();

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

    /**
     * 从意图中获取日记
     * @return
     */
    DiaryItem getDiaryFromIntent();

    /**
     * 设置日记内容
     * @param content
     */
    void setDiary(String content, String title);


    /**
     * 开启编辑
     */
    void beginEdit();

    /**
     * 完成编辑
     */
    void finishEdit();

    /**
     * 判断是否是可编辑状态
     * @return
     */
    boolean canEdit();
}
