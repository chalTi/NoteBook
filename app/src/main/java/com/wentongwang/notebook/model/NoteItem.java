package com.wentongwang.notebook.model;

import java.io.Serializable;

/**
 * 便签数据封装类
 * Created by Wentong WANG on 2016/6/3.
 */
public class NoteItem implements Serializable {

    public static final String ID = "_id";
    public static final String NOTE_DATE = "note_date";
    public static final String NOTE_CONTENT = "note_content";


    private String note_id;
    private String note_date;
    private String note_content;

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }
}
