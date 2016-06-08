package com.wentongwang.notebook.model;

/**
 * Created by Wentong WANG on 2016/6/6.
 */
public class UpdataEvent {
    public static final int UPDATE_NOTES = 0;
    public static final int UPDATE_DIARIES = 1;

    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
