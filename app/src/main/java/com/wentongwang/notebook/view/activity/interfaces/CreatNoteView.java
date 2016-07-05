package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;

/**
 * Created by Wentong WANG on 2016/7/4.
 */
public interface CreatNoteView {

    /**
     * 获取当前界面的Context
     * @return context
     */
    Context getMyContext();

    /**
     * 获取日记内容
     * @return
     */
    String getNoteContent();


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



}
