package com.wentongwang.notebook.model.business;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.utils.AccountUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Wentong WANG on 2016/7/5.
 */
public class NoteBiz {

    /**
     * 创建便签
     * @param context
     * @param note_content
     * @param userId
     * @param listener
     */
    public void creatNote(Context context, String note_content, String userId, final OnResponseListener listener){

        NoteItem noteItem = new NoteItem();
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        noteItem.setNote_date(sdf.format(new Date()));
        noteItem.setNote_content(note_content);
        noteItem.setNote_priority(0);
        noteItem.setNote_user_id(userId);

        noteItem.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    listener.onSuccess(null);
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    listener.onFailure(msg);
                }
            }
        });

    }




}
