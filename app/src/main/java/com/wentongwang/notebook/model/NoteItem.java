package com.wentongwang.notebook.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 便签数据封装类
 * Created by Wentong WANG on 2016/6/3.
 */
public class NoteItem extends BmobObject implements Serializable {

    public static final String ID = "_id";
    public static final String NOTE_DATE = "note_date";
    public static final String NOTE_CONTENT = "note_content";
    public static final String NOTE_PRIORITY = "note_priority";
    public static final String NOTE_USER_ID = "note_user_id";


    private String note_id;
    private String note_date;
    private String note_content;
    private Integer note_priority;
    private String note_user_id;

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

    public Integer getNote_priority() {
        return note_priority;
    }

    public void setNote_priority(Integer note_priority) {
        this.note_priority = note_priority;
    }

    public String getNote_user_id() {
        return note_user_id;
    }

    public void setNote_user_id(String note_user_id) {
        this.note_user_id = note_user_id;
    }
}
