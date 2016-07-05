package com.wentongwang.notebook.model.business;


import android.content.Context;

import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.utils.MyToast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 便签的业务逻辑
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
                    Response response = new Response();
                    response.setIsSucces(true);
                    listener.onResponse(response);
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    Response response = new Response();
                    response.setIsSucces(false);
                    response.setMsg(msg);
                    listener.onResponse(response);
                }
            }
        });

    }

    /**
     * 更改便签
     *
     * @param context
     * @param noteItem
     * @param newContent
     * @param listener
     */
    public void editNote(Context context, NoteItem noteItem, String newContent, final OnResponseListener listener){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        noteItem.setNote_date(sdf.format(new Date()));
        noteItem.setNote_content(newContent);

        noteItem.update(context, noteItem.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    Response response = new Response();
                    response.setIsSucces(true);
                    listener.onResponse(response);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    Response response = new Response();
                    response.setIsSucces(false);
                    response.setMsg(msg);
                    listener.onResponse(response);
                }
            }
        });
    }

    /**
     * 删除便签
     * @param context
     * @param id
     */
    public void deleteNote(Context context, String id, final OnResponseListener listener){
        NoteItem item = new NoteItem();
        item.setObjectId(id);
        item.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    Response response = new Response();
                    response.setIsSucces(true);
                    listener.onResponse(response);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    Response response = new Response();
                    response.setIsSucces(false);
                    response.setMsg(msg);
                    listener.onResponse(response);
                }
            }
        });
    }

}
