package com.wentongwang.notebook.model;

import android.util.Log;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 日记数据封装类
 * Created by Wentong WANG on 2016/6/3.
 */
public class DiaryItem extends BmobObject implements Serializable{

    public static final String ID = "_id";
    public static final String DIARY_DATE = "diary_date";
    public static final String DIARY_TITLE = "diary_title";
    public static final String DIARY_CONTENT = "diary_content";
    public static final String DIARY_IS_LOCKED = "islocked";
    public static final String DIARY_USER_ID = "diary_user_id";

    private String diary_id;
    private String diary_date;
    private String diary_title;
    private String diary_content;
    private boolean isLocked;
    private String diary_user_id;


    public String getDiary_id() {
        return diary_id;
    }

    public void setDiary_id(String diary_id) {
        this.diary_id = diary_id;
    }

    public String getDiary_date() {
        return diary_date;
    }

    public void setDiary_date(String diary_date) {
        this.diary_date = diary_date;
    }

    public String getDiary_title() {
        return diary_title;
    }

    public void setDiary_title(String diary_title) {
        this.diary_title = diary_title;
    }

    public String getDiary_content() {
        return diary_content;
    }

    public void setDiary_content(String diary_content) {
        this.diary_content = diary_content;
    }

    public String isLocked() {
        return isLocked ? "1" : "0";
    }
    public Boolean isLockedInBoolean() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {

        if (isLocked.equals("1")) {
            this.isLocked = true;
        }else if (isLocked.equals("0")) {
            this.isLocked = false;
        } else {
            Log.e("DiaryItem error", "数据内容有误");
            this.isLocked = true;
        }

    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getDiary_user_id() {
        return diary_user_id;
    }

    public void setDiary_user_id(String diary_user_id) {
        this.diary_user_id = diary_user_id;
    }
}
