package com.wentongwang.notebook.presenters;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyActivityManager;
import com.wentongwang.notebook.view.activity.interfaces.CreatNoteView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Wentong WANG on 2016/7/4.
 */
public class CreatNotePresenter {

    private CreatNoteView creatNoteView;

    public CreatNotePresenter(CreatNoteView creatNoteView) {
        this.creatNoteView = creatNoteView;
    }

    public void creatNote(){
        String note_content= creatNoteView.getNoteContent();
        String userId = AccountUtils.getUserId(creatNoteView.getMyContext());
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(creatNoteView.getMyContext(), "登录异常，请重新登录", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(note_content)) {
            Toast.makeText(creatNoteView.getMyContext(), "你还没写内容呢~", Toast.LENGTH_LONG).show();
            return;
        }

        NoteItem noteItem = new NoteItem();
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        noteItem.setNote_date(sdf.format(new Date()));
        noteItem.setNote_content(note_content);
        noteItem.setNote_priority(0);
        noteItem.setNote_user_id(userId);
        creatNoteView.showPorgressBar();

        noteItem.save(creatNoteView.getMyContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                creatNoteView.hidePorgressBar();
                creatNoteView.goBack();
            }
            @Override
            public void onFailure(int code, String msg) {
                creatNoteView.hidePorgressBar();
                Toast.makeText(creatNoteView.getMyContext(), "操作失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });

    }
}
