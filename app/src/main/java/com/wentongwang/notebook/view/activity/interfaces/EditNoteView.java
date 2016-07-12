package com.wentongwang.notebook.view.activity.interfaces;

import android.content.Context;

import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.view.BaseView;

/**
 * Created by Wentong WANG on 2016/7/5.
 */
public interface EditNoteView extends BaseView{

    /**
     * 获取便签
     * @return
     */
    NoteItem getNoteFromIntent();
    /**
     * 设置便签内容
     * @return
     */
    void setNoteContent(String content);

    /**
     * 获取便签内容
     * @return
     */
    String getNoteContent();

    /**
     * 修改完成跳转
     */
    void finishAndBack();

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
