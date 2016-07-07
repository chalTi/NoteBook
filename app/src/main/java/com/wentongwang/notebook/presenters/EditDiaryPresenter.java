package com.wentongwang.notebook.presenters;

import android.text.TextUtils;

import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.model.business.DiaryBiz;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.interfaces.EditDiaryView;

/**
 * Created by Wentong WANG on 2016/7/7.
 */
public class EditDiaryPresenter {

    private EditDiaryView editDiaryView;
    private DiaryBiz diaryBiz;

    public EditDiaryPresenter(EditDiaryView editDiaryView) {
        this.editDiaryView = editDiaryView;
        diaryBiz = new DiaryBiz();
    }


    /**
     * 初始化
     */
    public void initDiary() {
        DiaryItem diaryItem = editDiaryView.getDiaryFromIntent();
        String content = diaryItem.getDiary_content();
        String title = diaryItem.getDiary_title();
        editDiaryView.setDiary(content,title);
    }

    /**
     * 更新
     */
    public void edit(){
        if (!editDiaryView.canEdit()) {
            //EditText可编辑状态
            editDiaryView.beginEdit();
        } else {
            editDiaryView.finishEdit();
            submitText();
        }
    }

    /**
     * 提交更改到服务器
     */
    private void submitText(){
        DiaryItem diaryItem = editDiaryView.getDiaryFromIntent();

        String content;
        content = editDiaryView.getDiaryContent();

        if (TextUtils.isEmpty(content)) {
            MyToast.showLong(editDiaryView.getMyContext(), "你忘写内容啦！");
            return;
        }
        editDiaryView.showPorgressBar();
        diaryBiz.editDiary(editDiaryView.getMyContext(), diaryItem, content, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                editDiaryView.hidePorgressBar();
                if (response.isSucces()) {
                    editDiaryView.finishAndBack();
                } else {
                    MyToast.showLong(editDiaryView.getMyContext(), "操作失败: " + response.getMsg());
                }
            }
        });

    }
}
