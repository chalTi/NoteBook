package com.wentongwang.notebook.presenters;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.CreatDiaryView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Wentong WANG on 2016/7/4.
 */
public class CreatDiaryPresenter {

    private CreatDiaryView creatDiaryView;

    public CreatDiaryPresenter(CreatDiaryView creatDiaryView) {
        this.creatDiaryView = creatDiaryView;
    }

    public void creatDiary() {

        String userId = AccountUtils.getUserId(creatDiaryView.getMyContext());
        String diary_content = creatDiaryView.getDiaryContent();
        String diary_title = creatDiaryView.getDiaryTitle();
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(creatDiaryView.getMyContext(), "登录异常，请重新登录", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(diary_title)) {
            Toast.makeText(creatDiaryView.getMyContext(), "请填上标题", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(diary_content)) {
            Toast.makeText(creatDiaryView.getMyContext(), "你还没写内容呢~", Toast.LENGTH_LONG).show();
            return;
        }

        DiaryItem diaryItem = new DiaryItem();
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        diaryItem.setDiary_date(sdf.format(new Date()));
        diaryItem.setDiary_title(diary_title);
        diaryItem.setDiary_content(diary_content);
        diaryItem.setIsLocked(creatDiaryView.isLocked());
        diaryItem.setDiary_user_id(userId);
        creatDiaryView.showPorgressBar();
        diaryItem.save(creatDiaryView.getMyContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                creatDiaryView.hidePorgressBar();
                creatDiaryView.goBack();
            }

            @Override
            public void onFailure(int code, String msg) {
                creatDiaryView.hidePorgressBar();
                Toast.makeText(creatDiaryView.getMyContext(), "操作失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 日记上锁操作
     */
    public void lockDiary(){
        if (creatDiaryView.isLocked()) {
            creatDiaryView.setLock(false);
            creatDiaryView.updateBtnImg();
            Toast.makeText(creatDiaryView.getMyContext(), "日记已解锁", Toast.LENGTH_SHORT).show();
        } else {
            creatDiaryView.setLock(true);
            creatDiaryView.updateBtnImg();
            Toast.makeText(creatDiaryView.getMyContext(), "日记已上锁", Toast.LENGTH_SHORT).show();
        }
    }
}
