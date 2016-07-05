package com.wentongwang.notebook.presenters;

import android.text.TextUtils;
import android.widget.Toast;

import com.wentongwang.notebook.model.business.DiaryBiz;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.view.activity.interfaces.CreatDiaryView;


/**
 *
 * Created by Wentong WANG on 2016/7/4.
 */
public class CreatDiaryPresenter {

    private CreatDiaryView creatDiaryView;
    private DiaryBiz diaryBiz;

    public CreatDiaryPresenter(CreatDiaryView creatDiaryView) {
        this.creatDiaryView = creatDiaryView;
        diaryBiz = new DiaryBiz();
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
        diaryBiz.creatDiary(creatDiaryView.getMyContext(), diary_content, diary_title, creatDiaryView.isLocked(), userId,
                new OnResponseListener() {
                    @Override
                    public void onSuccess(Object response) {
                        creatDiaryView.hidePorgressBar();
                        creatDiaryView.goBack();
                    }

                    @Override
                    public void onFailure(String msg) {
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
