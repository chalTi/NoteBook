package com.wentongwang.notebook.presenters;

import android.text.TextUtils;

import android.widget.Toast;

import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.model.business.NoteBiz;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.utils.AccountUtils;

import com.wentongwang.notebook.view.activity.interfaces.CreatNoteView;


/**
 * Created by Wentong WANG on 2016/7/4.
 */
public class CreatNotePresenter {

    private CreatNoteView creatNoteView;
    private NoteBiz noteBiz;

    public CreatNotePresenter(CreatNoteView creatNoteView) {
        this.creatNoteView = creatNoteView;
        noteBiz = new NoteBiz();
    }

    public void creatNote() {
        String note_content = creatNoteView.getNoteContent();
        String userId = AccountUtils.getUserId(creatNoteView.getMyContext());
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(creatNoteView.getMyContext(), "登录异常，请重新登录", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(note_content)) {
            Toast.makeText(creatNoteView.getMyContext(), "你还没写内容呢~", Toast.LENGTH_LONG).show();
            return;
        }

        noteBiz.creatNote(creatNoteView.getMyContext(), note_content, userId, new OnResponseListener() {
            /**
             * 成功
             *
             * @param response 回复的结果
             */
            @Override
            public void onResponse(Response response) {
                creatNoteView.hidePorgressBar();
                if (response.isSucces()) {
                    creatNoteView.goBack();
                } else {
                    Toast.makeText(creatNoteView.getMyContext(), "操作失败: " + response.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
