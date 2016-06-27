package com.wentongwang.notebook.view.fragment.interfaces;

import android.app.Activity;

import com.wentongwang.notebook.model.NoteItem;

import java.util.List;

/**
 * Created by Wentong WANG on 2016/6/27.
 */
public interface NotesView {

    Activity getActivity();

    /**
     * 显示进度条
     */
    void showPorgressBar();

    /**
     * 隐藏进度条
     */
    void hidePorgressBar();

    void showNoDatas();

    void hideNoDatas();

    List<NoteItem> getNotesList();

    void updataList(List<NoteItem> list);

}
