package com.wentongwang.notebook.presenters;

import android.text.TextUtils;

import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.model.business.NoteBiz;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.interfaces.EditNoteView;

/**
 *
 * Created by Wentong WANG on 2016/7/5.
 */
public class EditNotePresenter {

    private EditNoteView editNoteView;
    private NoteBiz noteBiz;
    public  EditNotePresenter(EditNoteView editNoteView) {
        this.editNoteView = editNoteView;
        noteBiz = new NoteBiz();
    }

    /**
     * 初始化
     */
    public void setNoteContent() {
        NoteItem noteItem = editNoteView.getNoteFromIntent();
        String content = noteItem.getNote_content();
        editNoteView.setNoteContent(content);
    }

    /**
     * 更新
     */
    public void edit(){

        if (!editNoteView.canEdit()) {
            //EditText可编辑状态
            editNoteView.beginEdit();
        } else {
            editNoteView.finishEdit();
            submitText();
        }
    }

    /**
     * 提交更改到服务器
     */
    private void submitText(){
        NoteItem noteItem = editNoteView.getNoteFromIntent();

        String note_content;
        note_content = editNoteView.getNoteContent();

        if (TextUtils.isEmpty(note_content)) {
            MyToast.showLong(editNoteView.getMyContext(), "你忘写内容啦！");
            return;
        }
        editNoteView.showPorgressBar();
        noteBiz.editNote(editNoteView.getMyContext(), noteItem, note_content, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                editNoteView.hidePorgressBar();
                if (response.isSucces()) {
                    editNoteView.finishAndBack();
                } else {
                    MyToast.showLong(editNoteView.getMyContext(), "操作失败: " + response.getMsg());
                }
            }
        });

    }
}
