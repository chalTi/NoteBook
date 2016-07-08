package com.wentongwang.notebook.model.business;

import android.content.Context;

import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 日记的业务逻辑
 * Created by Wentong WANG on 2016/7/5.
 */
public class DiaryBiz {


    /**
     * 创建新的日记
     *
     * @param context       上下文
     * @param diary_content 日记内容
     * @param diary_title   日记标题
     * @param isLocked      是否上锁
     * @param userId        用户id
     * @param listener      回调监听
     */
    public void creatDiary(Context context, String diary_content, String diary_title,
                           Boolean isLocked, String userId, final OnResponseListener listener) {
        DiaryItem diaryItem = new DiaryItem();
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        diaryItem.setDiary_date(sdf.format(new Date()));
        diaryItem.setDiary_title(diary_title);
        diaryItem.setDiary_content(diary_content);
        diaryItem.setIsLocked(isLocked);
        diaryItem.setDiary_user_id(userId);

        diaryItem.save(context, new SaveListener() {
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
     * 更改日记
     *
     * @param context
     * @param diaryItem
     * @param newContent
     * @param listener
     */
    public void editDiary(Context context, DiaryItem diaryItem, String newContent, final OnResponseListener listener) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        diaryItem.setDiary_date(sdf.format(new Date()));
        diaryItem.setDiary_content(newContent);

        diaryItem.update(context, diaryItem.getObjectId(), new UpdateListener() {
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
     * 删除日记
     * @param context
     * @param id
     */
    public void deleteDiary(Context context, String id, final OnResponseListener listener){
        DiaryItem item = new DiaryItem();
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

